package com.markmc.minetest;

import java.nio.ByteBuffer;

import com.google.common.primitives.Bytes;

import static com.markmc.minetest.Constants.CLIENT_PROTOCOL_VERSION_MAX;
import static com.markmc.minetest.Constants.CLIENT_PROTOCOL_VERSION_MIN;
import static com.markmc.minetest.Constants.CONTROLTYPE_ACK;
import static com.markmc.minetest.Constants.CONTROLTYPE_DISCO;
import static com.markmc.minetest.Constants.CONTROLTYPE_ENABLE_BIG_SEND_WINDOW;
import static com.markmc.minetest.Constants.CONTROLTYPE_PING;
import static com.markmc.minetest.Constants.CONTROLTYPE_SET_PEER_ID;
import static com.markmc.minetest.Constants.DEFAULT_SERVER_PORT;
import static com.markmc.minetest.Constants.FOUR_BYTES;
import static com.markmc.minetest.Constants.MAX_PASSWORD_SIZE;
import static com.markmc.minetest.Constants.MAX_PLAYER_NAME_SIZE;
import static com.markmc.minetest.Constants.ONE_BYTE;
import static com.markmc.minetest.Constants.PEER_ID_INEXISTENT;
import static com.markmc.minetest.Constants.PROTOCOL_ID;
import static com.markmc.minetest.Constants.SEQNUM_INITIAL;
import static com.markmc.minetest.Constants.SEQNUM_MAX;
import static com.markmc.minetest.Constants.SER_FMT_VER_HIGHEST_READ;
import static com.markmc.minetest.Constants.TWO_BYTES;
import static com.markmc.minetest.Constants.TYPE_CONTROL;
import static com.markmc.minetest.Constants.TYPE_ORIGINAL;
import static com.markmc.minetest.Constants.TYPE_RELIABLE;
import static com.markmc.minetest.Constants.TYPE_SPLIT;
import static com.markmc.minetest.Constants.VERSION_MAJOR;
import static com.markmc.minetest.Constants.VERSION_MINOR;
import static com.markmc.minetest.Constants.VERSION_PATCH;
import static com.markmc.minetest.Constants.VERSION_STRING;

// https://systembash.com/a-simple-java-udp-server-and-udp-client/
// http://dev.minetest.net/Network_Protocol
// https://github.com/minetest/minetest/blob/master/doc/protocol.txt
// https://github.com/rarkenin/minetest-bot-php
// http://dev.minetest.net/Core_Architecture
/**
 * MinetestBot.
 * <p>
 * @author markmc
 */
public class MinetestBot {

  /**
   * States enum.
   */
  public enum State {

    Invalid,
    Disconnecting,
    Denied,
    Created,
    InitSent,
    InitDone,
    DefinitionsSent,
    Active

  }

  private static NetworkIOImpl network = new NetworkIOImpl("192.168.1.2", DEFAULT_SERVER_PORT);

  private static int[] outgoingSeqnums
    = new int[] { SEQNUM_INITIAL, SEQNUM_INITIAL, SEQNUM_INITIAL };

  private static byte[] peerId = PEER_ID_INEXISTENT;

  private static State state;

  /**
   * Private constructor.
   */
  private MinetestBot() {
  }

  /**
   * Minetest bot.
   * <p>
   * @param args command line arguments
   * <p>
   * @throws Exception if something goes wrong
   */
  public static void main(final String[] args) throws Exception {

    ByteBuffer data;

    byte[] protocolId;
    byte[] senderPeerId;
    int channel;
    byte[] channelBytes;
    byte[] type;
    byte[] seqnum;
    byte[] controltype;
    byte[] peerIdNew;
    byte[] command;

    byte[] chunkCount;
    byte[] chunkNum;

    connect();

    boolean pollServer = Boolean.TRUE;

    while (pollServer) {
      // Accept connections from server
      System.out.println("State: " + state);
      if (state == State.Created) {
        toserverInit();
        //STATE = State.InitSent;
      }
      //
      data = network.requestData();

      // read header
      protocolId = Utils.pop(data, FOUR_BYTES);
      senderPeerId = Utils.pop(data, TWO_BYTES);
      channelBytes = Utils.pop(data, ONE_BYTE);
      channel = Utils.toInteger(channelBytes);
      // read type
      type = Utils.pop(data, 1);
      //System.out.println(TYPES.get(ByteBuffer.wrap(type)));
      if (Utils.isEqual(type, TYPE_RELIABLE)) {
        seqnum = Utils.pop(data, 2);
        //System.out.println(Utils.toInteger(seqnum));
        acknowledge(channelBytes, seqnum);
        if (data.hasRemaining()) {
          type = Utils.pop(data, 1);
          //System.out.println(TYPES.get(ByteBuffer.wrap(type)));
        }
      }
      if (Utils.isEqual(type, TYPE_CONTROL)) {
        controltype = Utils.pop(data, ONE_BYTE);
        //System.out.println(CONTROLTYPES.get(ByteBuffer.wrap(controltype)));
        if (Utils.isEqual(controltype, CONTROLTYPE_ACK)) {
          seqnum = Utils.pop(data, TWO_BYTES);
          System.out.println("Server: Ack " + Utils.toInteger(seqnum));
        }
        if (Utils.isEqual(controltype, CONTROLTYPE_SET_PEER_ID)) {
          System.out.println("Server: SET_PEER_ID");
          peerIdNew = Utils.pop(data, TWO_BYTES);
          peerId = peerIdNew;
          state = State.Created;
          disableLegacy();
        }
        if (Utils.isEqual(controltype, CONTROLTYPE_PING)) {
          // Do nothing
        }
        if (Utils.isEqual(controltype, CONTROLTYPE_DISCO)) {
          // Do nothing
        }
      }
      if (Utils.isEqual(type, TYPE_ORIGINAL)) {
        //System.out.println("Remaining data: " + data.remaining());
        command = Utils.pop(data, TWO_BYTES);
        // System.out.println(Utils.toString(command));
        CommandHandler commandHandler = ServiceHandler.CMD_HANDLER.get(ByteBuffer.wrap(command));
        System.out.println("Server: " + commandHandler.getName());
        commandHandler.run(data);
//                System.out.println(toString(command));
//                byte[] msg = new byte[data.remaining()];
//                int i = 0;
//                while(data.hasRemaining()) {
//                    System.out.println(data.remaining());
//                    msg[i] = data.get();
//                    i++;
//                }
//                System.out.println(i);
//                System.out.println(new String(msg, "UTF-16"));
//                System.out.println(new String(msg, "UTF-16").length());
      }
      if (Utils.isEqual(type, TYPE_SPLIT)) {
        seqnum = Utils.pop(data, TWO_BYTES);
        chunkCount = Utils.pop(data, TWO_BYTES);
        chunkNum = Utils.pop(data, TWO_BYTES);
        System.out.println(String.format("Server: Split message chunk %s/%s", 1 + Utils.toInteger(
                                         chunkNum), Utils.toInteger(chunkCount)));
        if (0 == Utils.toInteger(chunkNum)) {
          command = Utils.pop(data, TWO_BYTES);
          // System.out.println(Utils.toString(command));
          CommandHandler commandHandler = ServiceHandler.CMD_HANDLER.get(ByteBuffer.wrap(command));
          //System.out.println(commandHandler.getName());
        }
      }
    }

  }

  /**
   * Connect.
   * <p>
   * @throws Exception if something goes wrong
   */
  public static void connect() throws Exception {
    System.out.println("Client: connect");
    byte[] msg = Bytes.concat(PROTOCOL_ID,
                              peerId,
                              Utils.u8(0),
                              TYPE_RELIABLE,
                              Utils.u16(outgoingSeqnums[0]),
                              TYPE_ORIGINAL
    );
    network.send(msg);
  }

  /**
   * Acknowledge.
   * <p>
   * @param channelnum byte[]
   * @param seqnum     byte[]
   * <p>
   * @throws Exception if something goes wrong
   */
  public static void acknowledge(final byte[] channelnum, final byte[] seqnum) throws Exception {
    int channel = Utils.toInteger(channelnum);
    System.out.println("Client: Ack " + Utils.toInteger(seqnum));
    byte[] msg = Bytes.concat(PROTOCOL_ID,
                              peerId,
                              channelnum,
                              TYPE_CONTROL,
                              CONTROLTYPE_ACK,
                              seqnum
    );
    network.send(msg);
  }

  /**
   * Disable legacy.
   * <p>
   * @throws Exception if something goes wrong
   */
  public static void disableLegacy() throws Exception {
    int channel = 0;
    System.out.println("Client: Disable Legaacy");
    byte[] msg = Bytes.concat(PROTOCOL_ID,
                              peerId,
                              Utils.u8(channel),
                              TYPE_RELIABLE,
                              Utils.u16(outgoingSeqnums[channel]),
                              TYPE_CONTROL,
                              CONTROLTYPE_ENABLE_BIG_SEND_WINDOW
    );
    incrementSeqnum(channel);
    network.send(msg);
  }

  // TOSERVER_INIT_LEGACY
  // [0] u16 TOSERVER_INIT
  // [2] u8 SER_FMT_VER_HIGHEST_READ
  // [3] u8[20] playerName
  // [23] u8[28] password (new in some version)
  // [51] u16 minimum supported network protocol version (added sometime)
  // [53] u16 maximum supported network protocol version (added later than the previous one)
  public static void toserverInit() throws Exception {
    ServerCommand command = ServerCommands.getServerCommand(
      ServerCommands.TOSERVER_INIT_LEGACY);
    int channel = command.getChannel();
    byte[] reliableBytes = new byte[0];
    if (command.isReliable()) {
      reliableBytes = getReliableBytes(channel);
    }
    //
    String reliable = (command.isReliable() ? "(rel)" : "(unrel)");
    System.out.println("Client: INIT " + reliable);
    //
    String playerName = "minetest-bot";
    byte[] msg = Bytes.concat(PROTOCOL_ID, // 4
                              peerId, // 2
                              command.getChannelBytes(), // channel, 1
                              reliableBytes,
                              TYPE_ORIGINAL, // 1
                              command.getCommandBytes(), // command, 2
                              SER_FMT_VER_HIGHEST_READ, // 1
                              Utils.makeByteArray(playerName.getBytes(), MAX_PLAYER_NAME_SIZE), // 20
                              Utils.makeByteArray("".getBytes(), MAX_PASSWORD_SIZE), // 28
                              CLIENT_PROTOCOL_VERSION_MIN, // 2
                              CLIENT_PROTOCOL_VERSION_MAX // 2
    );
    network.send(msg);
  }

  // TOSERVER_INIT2
  // Sent as an ACK for TOCLIENT_INIT.
  // After this, the server can send data.
  //
  // [0] u16 TOSERVER_INIT2
  public static void toserverInit2() {
    ServerCommand command = ServerCommands.getServerCommand(
      ServerCommands.TOSERVER_INIT2);
    int channel = command.getChannel();
    byte[] reliableBytes = new byte[0];
    if (command.isReliable()) {
      reliableBytes = getReliableBytes(channel);
    }
    //
    String reliable = (command.isReliable() ? "(rel)" : "(unrel)");
    System.out.println("Client: INIT2 " + reliable);
    //
    byte[] msg = Bytes.concat(PROTOCOL_ID, // 4
                              peerId, // 2
                              command.getChannelBytes(), // channel, 1
                              reliableBytes,
                              TYPE_ORIGINAL, // 1
                              command.getCommandBytes() // command, 2
    );
    network.send(msg);
  }

  // TOSERVER_RECEIVED_MEDIA
  // u16 command
  public static void toserverMediaRecieved() {
    ServerCommand command = ServerCommands.getServerCommand(
      ServerCommands.TOSERVER_RECEIVED_MEDIA);
    int channel = command.getChannel();
    byte[] reliableBytes = new byte[0];
    if (command.isReliable()) {
      reliableBytes = getReliableBytes(channel);
    }
    //
    String reliable;
    if (command.isReliable()) {
      reliable = "(rel)";
    } else {
      reliable = "(unrel)";
    }
    System.out.println("Client: RECEIVED_MEDIA " + reliable);
    //
    byte[] msg = Bytes.concat(PROTOCOL_ID, // 4
                              peerId, // 2
                              command.getChannelBytes(), // channel, 1
                              reliableBytes,
                              TYPE_ORIGINAL, // 1
                              command.getCommandBytes()
    );
    network.send(msg);
  }

  // TOSERVER_CLIENT_READY
  // u8 major
  // u8 minor
  // u8 patch
  // u8 reserved
  // u16 len
  // u8[len] full_version_string
  public static void toserverClientReady() {
    ServerCommand command = ServerCommands.getServerCommand(
      ServerCommands.TOSERVER_CLIENT_READY);
    int channel = command.getChannel();
    byte[] reliableBytes = new byte[0];
    if (command.isReliable()) {
      reliableBytes = getReliableBytes(channel);
    }
    //
    String reliable = (command.isReliable() ? "(rel)" : "(unrel)");
    System.out.println("Client: CLIENT_READY " + reliable);
    //
    byte[] msg = Bytes.concat(PROTOCOL_ID, // 4
                              peerId, // 2
                              command.getChannelBytes(), // channel, 0
                              reliableBytes,
                              TYPE_ORIGINAL, // 1
                              command.getCommandBytes(),
                              Utils.u8(VERSION_MAJOR),
                              Utils.u8(VERSION_MINOR),
                              Utils.u8(VERSION_PATCH),
                              Utils.u8(0),
                              Utils.u16(VERSION_STRING.length()),
                              Utils.makeByteArray(VERSION_STRING.getBytes(), VERSION_STRING.length())
    );
    network.send(msg);
  }

  /**
   * getReliableBytes.
   * <p>
   * @param channel channel number
   * <p>
   * @return byte[]
   */
  private static byte[] getReliableBytes(final int channel) {
    byte[] msg = Bytes.concat(TYPE_RELIABLE,
                              Utils.u16(outgoingSeqnums[channel])
    );
    if (!Utils.isEqual(peerId, PEER_ID_INEXISTENT)) {
      incrementSeqnum(channel);
    }
    return msg;
  }

  /**
   * incrementSeqnum.
   * <p>
   * @param channel channel number
   */
  private static void incrementSeqnum(final int channel) {
    outgoingSeqnums[channel]++;
    if (outgoingSeqnums[channel] > SEQNUM_MAX) {
      outgoingSeqnums[channel] = 0;
    }
  }

  /*
   * setState.
   */
  public static void setState(final State instate) {
    state = instate;
  }

}
