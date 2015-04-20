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
import static com.markmc.minetest.Constants.PEER_ID_INEXISTENT;
import static com.markmc.minetest.Constants.PROTOCOL_ID;
import static com.markmc.minetest.Constants.SEQNUM_INITIAL;
import static com.markmc.minetest.Constants.SER_FMT_VER_HIGHEST_READ;
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
 *
 * @author markmc
 */
public class MinetestBot {

    enum State {

        Invalid,
        Disconnecting,
        Denied,
        Created,
        InitSent,
        InitDone,
        DefinitionsSent,
        Active
    }

    private static final NetworkIO network = new NetworkIO("192.168.1.2", 30000);

    private static final int SEQNUM_MAX = 65535;
    public static int[] OUTGOING_SEQNUMS = new int[]{SEQNUM_INITIAL, SEQNUM_INITIAL, SEQNUM_INITIAL};

    private static byte[] PEER_ID = PEER_ID_INEXISTENT;
    public static State STATE;

    public static void main(final String[] args) throws Exception {

        ByteBuffer data;

        byte[] protocol_id;
        byte[] sender_peer_id;
        int channel;
        byte[] channelBytes;
        byte[] type;
        byte[] seqnum;
        byte[] controltype;
        byte[] peer_id_new;
        byte[] command;

        byte[] chunk_count;
        byte[] chunk_num;

        connect();

        boolean pollServer = Boolean.TRUE;

        while (pollServer) {
            // Accept connections from server
            System.out.println("State: " + STATE);
            if (STATE == State.Created) {
                toserverInit();
                //STATE = State.InitSent;
            }
            //
            data = network.requestData();

            // read header
            protocol_id = Utils.pop(data, 4);
            sender_peer_id = Utils.pop(data, 2);
            channelBytes = Utils.pop(data, 1);
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
                controltype = Utils.pop(data, 1);
                //System.out.println(CONTROLTYPES.get(ByteBuffer.wrap(controltype)));
                if (Utils.isEqual(controltype, CONTROLTYPE_ACK)) {
                    seqnum = Utils.pop(data, 2);
                    System.out.println("Server: Ack " + Utils.toInteger(seqnum));
                }
                if (Utils.isEqual(controltype, CONTROLTYPE_SET_PEER_ID)) {
                    System.out.println("Server: SET_PEER_ID");
                    peer_id_new = Utils.pop(data, 2);
                    PEER_ID = peer_id_new;
                    STATE = State.Created;
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
                command = Utils.pop(data, 2);
                // System.out.println(Utils.toString(command));
                CommandHandler commandHandler = ServiceHandler.commandHandler.get(ByteBuffer.wrap(command));
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
                seqnum = Utils.pop(data, 2);
                chunk_count = Utils.pop(data, 2);
                chunk_num = Utils.pop(data, 2);
                System.out.println(String.format("Server: Split message chunk %s/%s", 1 + Utils.toInteger(chunk_num), Utils.toInteger(chunk_count)));
                if (0 == Utils.toInteger(chunk_num)) {
                    command = Utils.pop(data, 2);
                    // System.out.println(Utils.toString(command));
                    CommandHandler commandHandler = ServiceHandler.commandHandler.get(ByteBuffer.wrap(command));
                    //System.out.println(commandHandler.getName());
                }
            }
        }

    }

    public static void connect() throws Exception {
        System.out.println("Client: connect");
        byte[] msg = Bytes.concat(
                PROTOCOL_ID,
                PEER_ID,
                Utils.U8(0),
                TYPE_RELIABLE,
                Utils.U16(OUTGOING_SEQNUMS[0]),
                TYPE_ORIGINAL
        );
        network.send(msg);
    }

    public static void acknowledge(final byte[] channelnum, final byte[] seqnum) throws Exception {
        int channel = Utils.toInteger(channelnum);
        System.out.println("Client: Ack " + Utils.toInteger(seqnum));
        byte[] msg = Bytes.concat(
                PROTOCOL_ID,
                PEER_ID,
                channelnum,
                TYPE_CONTROL,
                CONTROLTYPE_ACK,
                seqnum
        );
        network.send(msg);
    }

    public static void disableLegacy() throws Exception {
        int channel = 0;
        System.out.println("Client: Disable Legaacy");
        byte[] msg = Bytes.concat(
                PROTOCOL_ID,
                PEER_ID,
                Utils.U8(channel),
                TYPE_RELIABLE,
                Utils.U16(OUTGOING_SEQNUMS[channel]),
                TYPE_CONTROL,
                CONTROLTYPE_ENABLE_BIG_SEND_WINDOW
        );
        incrementSeqnum(channel);
        network.send(msg);
    }

    /*
     TOSERVER_INIT_LEGACY
     [0] u16 TOSERVER_INIT
     [2] u8 SER_FMT_VER_HIGHEST_READ
     [3] u8[20] player_name
     [23] u8[28] password (new in some version)
     [51] u16 minimum supported network protocol version (added sometime)
     [53] u16 maximum supported network protocol version (added later than the previous one)
     */
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
        String player_name = "minetest-bot";
        byte[] msg = Bytes.concat(
                PROTOCOL_ID, // 4
                PEER_ID, // 2
                command.getChannelBytes(), // channel, 1
                reliableBytes,
                TYPE_ORIGINAL, // 1
                command.getCommandBytes(), // command, 2
                SER_FMT_VER_HIGHEST_READ, // 1
                Utils.makeByteArray(player_name.getBytes(), 20), // 20
                Utils.makeByteArray("".getBytes(), 28), // 28
                CLIENT_PROTOCOL_VERSION_MIN, // 2
                CLIENT_PROTOCOL_VERSION_MAX // 2
        );
        network.send(msg);
    }

    /*
     TOSERVER_INIT2
     Sent as an ACK for TOCLIENT_INIT.
     After this, the server can send data.

     [0] u16 TOSERVER_INIT2
     */
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
        byte[] msg = Bytes.concat(
                PROTOCOL_ID, // 4
                PEER_ID, // 2
                command.getChannelBytes(), // channel, 1
                reliableBytes,
                TYPE_ORIGINAL, // 1
                command.getCommandBytes() // command, 2
        );
        network.send(msg);
    }

    /*
     TOSERVER_RECEIVED_MEDIA
     u16 command
     */
    public static void toserverMediaRecieved() {
        ServerCommand command = ServerCommands.getServerCommand(
                ServerCommands.TOSERVER_RECEIVED_MEDIA);
        int channel = command.getChannel();
        byte[] reliableBytes = new byte[0];
        if (command.isReliable()) {
            reliableBytes = getReliableBytes(channel);
        }
        //
        String reliable = (command.isReliable() ? "(rel)" : "(unrel)");
        System.out.println("Client: RECEIVED_MEDIA " + reliable);
        //
        byte[] msg = Bytes.concat(
                PROTOCOL_ID, // 4
                PEER_ID, // 2
                command.getChannelBytes(), // channel, 1
                reliableBytes,
                TYPE_ORIGINAL, // 1
                command.getCommandBytes()
        );
        network.send(msg);
    }

    /*
     TOSERVER_CLIENT_READY
     u8 major
     u8 minor
     u8 patch
     u8 reserved
     u16 len
     u8[len] full_version_string
     */
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
        byte[] msg = Bytes.concat(
                PROTOCOL_ID, // 4
                PEER_ID, // 2
                command.getChannelBytes(), // channel, 0
                reliableBytes,
                TYPE_ORIGINAL, // 1
                command.getCommandBytes(),
                Utils.U8(VERSION_MAJOR),
                Utils.U8(VERSION_MINOR),
                Utils.U8(VERSION_PATCH),
                Utils.U8(0),
                Utils.U16(VERSION_STRING.length()),
                Utils.makeByteArray(VERSION_STRING.getBytes(), VERSION_STRING.length())
        );
        network.send(msg);
    }

    private static byte[] getReliableBytes(final int channel) {
        byte[] msg = Bytes.concat(
                TYPE_RELIABLE,
                Utils.U16(OUTGOING_SEQNUMS[channel])
        );
        if (!Utils.isEqual(PEER_ID, PEER_ID_INEXISTENT)) {
            incrementSeqnum(channel);
        }
        return msg;
    }

    private static void incrementSeqnum(int channel) {
        OUTGOING_SEQNUMS[channel]++;
        if (OUTGOING_SEQNUMS[channel] > SEQNUM_MAX) {
            OUTGOING_SEQNUMS[channel] = 0;
        }
        // System.out.println(String.format("OUTGOING_SEQNUMS[%s] increased to %s", channel, OUTGOING_SEQNUMS[channel]));
    }

}
