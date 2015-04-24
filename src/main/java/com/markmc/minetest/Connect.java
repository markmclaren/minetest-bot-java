package com.markmc.minetest;

import com.google.common.primitives.Bytes;

import static com.markmc.minetest.Constants.CLIENT_PROTOCOL_VERSION_MAX;
import static com.markmc.minetest.Constants.CLIENT_PROTOCOL_VERSION_MIN;
import static com.markmc.minetest.Constants.CONTROLTYPE_ACK;
import static com.markmc.minetest.Constants.CONTROLTYPE_ENABLE_BIG_SEND_WINDOW;
import static com.markmc.minetest.Constants.MAX_PASSWORD_SIZE;
import static com.markmc.minetest.Constants.MAX_PLAYER_NAME_SIZE;
import static com.markmc.minetest.Constants.PEER_ID_INEXISTENT;
import static com.markmc.minetest.Constants.PROTOCOL_ID;
import static com.markmc.minetest.Constants.SEQNUM_MAX;
import static com.markmc.minetest.Constants.SER_FMT_VER_HIGHEST_READ;
import static com.markmc.minetest.Constants.TYPE_CONTROL;
import static com.markmc.minetest.Constants.TYPE_ORIGINAL;
import static com.markmc.minetest.Constants.TYPE_RELIABLE;
import static com.markmc.minetest.Constants.VERSION_MAJOR;
import static com.markmc.minetest.Constants.VERSION_MINOR;
import static com.markmc.minetest.Constants.VERSION_PATCH;
import static com.markmc.minetest.Constants.VERSION_STRING;

/**
 * Connect.
 * <p>
 * @author Mark McLaren (mark.mclaren@bristol.ac.uk)
 */
public class Connect {

  private Bot bot;

  /**
   * Connect.
   * @param bot Bot
   */
  public Connect(final Bot bot) {
    this.bot = bot;
  }

  /**
   * Connect.
   * <p>
   * @throws Exception if something goes wrong
   */
  public void connect() throws Exception {
    System.out.println("Client: connect");
    byte[] msg = Bytes.concat(PROTOCOL_ID,
                              bot.getPeerId(),
                              Utils.u8(0),
                              TYPE_RELIABLE,
                              Utils.u16(bot.getOutgoingSeqnums()[0]),
                              TYPE_ORIGINAL
    );
    bot.getNetwork().send(msg);
  }

  /**
   * Acknowledge.
   * <p>
   * @param channelnum byte[]
   * @param seqnum     byte[]
   * <p>
   * @throws Exception if something goes wrong
   */
  public void acknowledge(final byte[] channelnum, final byte[] seqnum) throws Exception {
    int channel = Utils.toInteger(channelnum);
    System.out.println("Client: Ack " + Utils.toInteger(seqnum));
    byte[] msg = Bytes.concat(PROTOCOL_ID,
                              bot.getPeerId(),
                              channelnum,
                              TYPE_CONTROL,
                              CONTROLTYPE_ACK,
                              seqnum
    );
    bot.getNetwork().send(msg);
  }

  /**
   * Disable legacy.
   * <p>
   * @throws Exception if something goes wrong
   */
  public void disableLegacy() throws Exception {
    int channel = 0;
    System.out.println("Client: Disable Legaacy");
    byte[] msg = Bytes.concat(PROTOCOL_ID,
                              bot.getPeerId(),
                              Utils.u8(channel),
                              TYPE_RELIABLE,
                              Utils.u16(bot.getOutgoingSeqnums()[channel]),
                              TYPE_CONTROL,
                              CONTROLTYPE_ENABLE_BIG_SEND_WINDOW
    );
    incrementSeqnum(channel);
    bot.getNetwork().send(msg);
  }

  // TOSERVER_INIT_LEGACY
  // [0] u16 TOSERVER_INIT
  // [2] u8 SER_FMT_VER_HIGHEST_READ
  // [3] u8[20] playerName
  // [23] u8[28] password (new in some version)
  // [51] u16 minimum supported network protocol version (added sometime)
  // [53] u16 maximum supported network protocol version (added later than the previous one)
  /**
   * toserverInit.
   * <p>
   * @throws Exception if something goes wrong
   */
  public void toserverInit() throws Exception {
    ServerCommand command = ServerCommands.getServerCommand(
      ServerCommands.TOSERVER_INIT_LEGACY);
    int channel = command.getChannel();
    byte[] reliableBytes = new byte[0];
    if (command.isReliable()) {
      reliableBytes = getReliableBytes(channel);
    }
    //
    System.out.println("Client: INIT "
      + getReliableLabel(command.isReliable()));
    //
    byte[] msg = Bytes.concat(PROTOCOL_ID, // 4
                              bot.getPeerId(), // 2
                              command.getChannelBytes(), // channel, 1
                              reliableBytes,
                              TYPE_ORIGINAL, // 1
                              command.getCommandBytes(), // command, 2
                              SER_FMT_VER_HIGHEST_READ, // 1
                              Utils.makeByteArray(bot.getBotname().getBytes(), MAX_PLAYER_NAME_SIZE), // 20
                              Utils.makeByteArray("".getBytes(), MAX_PASSWORD_SIZE), // 28
                              CLIENT_PROTOCOL_VERSION_MIN, // 2
                              CLIENT_PROTOCOL_VERSION_MAX // 2
    );
    bot.getNetwork().send(msg);
  }

  // TOSERVER_INIT2
  // Sent as an ACK for TOCLIENT_INIT.
  // After this, the server can send data.
  //
  // [0] u16 TOSERVER_INIT2
  /**
   * toserverInit2.
   */
  public void toserverInit2() {
    ServerCommand command = ServerCommands.getServerCommand(
      ServerCommands.TOSERVER_INIT2);
    int channel = command.getChannel();
    byte[] reliableBytes = new byte[0];
    if (command.isReliable()) {
      reliableBytes = getReliableBytes(channel);
    }
    //
    System.out.println("Client: INIT2 "
      + getReliableLabel(command.isReliable()));
    //
    byte[] msg = Bytes.concat(PROTOCOL_ID, // 4
                              bot.getPeerId(), // 2
                              command.getChannelBytes(), // channel, 1
                              reliableBytes,
                              TYPE_ORIGINAL, // 1
                              command.getCommandBytes() // command, 2
    );
    bot.getNetwork().send(msg);
  }

  // TOSERVER_RECEIVED_MEDIA
  // u16 command
  /**
   * toserverMediaRecieved.
   */
  public void toserverMediaRecieved() {
    ServerCommand command = ServerCommands.getServerCommand(
      ServerCommands.TOSERVER_RECEIVED_MEDIA);
    int channel = command.getChannel();
    byte[] reliableBytes = new byte[0];
    if (command.isReliable()) {
      reliableBytes = getReliableBytes(channel);
    }
    //
    System.out.println("Client: RECEIVED_MEDIA "
      + getReliableLabel(command.isReliable()));
    //
    byte[] msg = Bytes.concat(PROTOCOL_ID, // 4
                              bot.getPeerId(), // 2
                              command.getChannelBytes(), // channel, 1
                              reliableBytes,
                              TYPE_ORIGINAL, // 1
                              command.getCommandBytes()
    );
    bot.getNetwork().send(msg);
  }

  // TOSERVER_CLIENT_READY
  // u8 major
  // u8 minor
  // u8 patch
  // u8 reserved
  // u16 len
  // u8[len] full_version_string
  /**
   * toserverClientReady.
   */
  public void toserverClientReady() {
    ServerCommand command = ServerCommands.getServerCommand(
      ServerCommands.TOSERVER_CLIENT_READY);
    int channel = command.getChannel();
    byte[] reliableBytes = new byte[0];
    if (command.isReliable()) {
      reliableBytes = getReliableBytes(channel);
    }
    //
    System.out.println("Client: CLIENT_READY "
      + getReliableLabel(command.isReliable()));
    //
    byte[] msg = Bytes.concat(PROTOCOL_ID, // 4
                              bot.getPeerId(), // 2
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
    bot.getNetwork().send(msg);
  }

  /**
   * getReliableBytes.
   * <p>
   * @param channel channel number
   * <p>
   * @return byte[]
   */
  private byte[] getReliableBytes(final int channel) {
    byte[] msg = Bytes.concat(TYPE_RELIABLE,
                              Utils.u16(bot.getOutgoingSeqnums()[channel])
    );
    if (!Utils.isEqual(bot.getPeerId(), PEER_ID_INEXISTENT)) {
      incrementSeqnum(channel);
    }
    return msg;
  }

  /**
   * incrementSeqnum.
   * <p>
   * @param channel channel number
   */
  private void incrementSeqnum(final int channel) {
    bot.getOutgoingSeqnums()[channel]++;
    if (bot.getOutgoingSeqnums()[channel] > SEQNUM_MAX) {
      bot.getOutgoingSeqnums()[channel] = 0;
    }
  }

  /**
   * getReliableLabel.
   * <p>
   * @param isReliable if command is reliable
   * <p>
   * @return String
   */
  private String getReliableLabel(final boolean isReliable) {
    String reliable;
    if (isReliable) {
      reliable = "(rel)";
    } else {
      reliable = "(unrel)";
    }
    return reliable;
  }

}
