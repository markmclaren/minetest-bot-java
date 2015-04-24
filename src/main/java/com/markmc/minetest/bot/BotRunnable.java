package com.markmc.minetest.bot;

import com.markmc.minetest.acceptor.BotServiceHandler;
import com.markmc.minetest.connector.ClientState;
import com.markmc.minetest.acceptor.CommandHandler;
import com.markmc.minetest.connector.Connect;
import com.markmc.minetest.utils.Utils;
import static com.markmc.minetest.utils.Constants.CONTROLTYPE_ACK;
import static com.markmc.minetest.utils.Constants.CONTROLTYPE_DISCO;
import static com.markmc.minetest.utils.Constants.CONTROLTYPE_PING;
import static com.markmc.minetest.utils.Constants.CONTROLTYPE_SET_PEER_ID;
import static com.markmc.minetest.utils.Constants.FOUR_BYTES;
import static com.markmc.minetest.utils.Constants.ONE_BYTE;
import static com.markmc.minetest.utils.Constants.TWO_BYTES;
import static com.markmc.minetest.utils.Constants.TYPE_CONTROL;
import static com.markmc.minetest.utils.Constants.TYPE_ORIGINAL;
import static com.markmc.minetest.utils.Constants.TYPE_RELIABLE;
import static com.markmc.minetest.utils.Constants.TYPE_SPLIT;
import java.nio.ByteBuffer;

/**
 * BotRunnable.
 * @author markmc
 */
public class BotRunnable implements Runnable {

    private final Bot bot;
    private final Connect connect;
    private final BotServiceHandler serviceHandler;

    public BotRunnable(final String serverIp, final String botName) {
        bot = new Bot(serverIp, botName);
        connect = new Connect(bot);
        serviceHandler = new BotServiceHandler(bot, connect);
    }

    public void run() {
        try {
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

            connect.connect();

            boolean pollServer = Boolean.TRUE;

            while (pollServer) {
                // Accept connections from server
                System.out.println("State: " + bot.getState());
                if (bot.getState() == ClientState.Created) {
                    connect.toserverInit();
                    //STATE = ClientState.InitSent;
                }
                //
                data = bot.getNetwork().requestData();

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
                    connect.acknowledge(channelBytes, seqnum);
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
                        bot.setPeerId(peerIdNew);
                        bot.setState(ClientState.Created);
                        connect.disableLegacy();
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
                    CommandHandler commandHandler = serviceHandler.CMD_HANDLER.get(ByteBuffer.wrap(command));
                    System.out.println("Server: " + commandHandler.getName());
                    commandHandler.process(data);
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
                        CommandHandler commandHandler = serviceHandler.CMD_HANDLER.get(ByteBuffer.wrap(command));
                        //System.out.println(commandHandler.getName());
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

}
