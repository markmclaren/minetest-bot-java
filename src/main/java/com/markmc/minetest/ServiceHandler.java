package com.markmc.minetest;

import com.markmc.minetest.MinetestBot.State;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Derived from networkprotocol.h
 *
 * @author markmc
 */
public class ServiceHandler {

    public static Boolean init2Sent = Boolean.FALSE;
    public static Boolean receivedMediaSent = Boolean.FALSE;
    public static Boolean clientReadySent = Boolean.FALSE;

    public static final Map<ByteBuffer, CommandHandler> commandHandler = new HashMap<ByteBuffer, CommandHandler>();

    static {
        commandHandler.put(Utils.getByteBuffer("0002"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_HELLO";
            }

            public void run(ByteBuffer data) {

            }

        });
        commandHandler.put(Utils.getByteBuffer("0003"), new CommandHandler() {

            public String getName() {
                return "TOCLIENT_AUTH_ACCEPT";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("000A"), new CommandHandler() {

            public String getName() {
                return "TOCLIENT_ACCESS_DENIED";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0010"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_INIT";
            }

            public void run(ByteBuffer data) {
                if (!init2Sent) {
                    byte[] version = Utils.pop(data, 1);
                    byte[] pos_x = Utils.pop(data, 2);
                    byte[] pos_y = Utils.pop(data, 2);
                    byte[] pos_z = Utils.pop(data, 2);
                    //System.out.println("x:" + Utils.toInteger(pos_x));
                    //System.out.println("y:" + Utils.toInteger(pos_y));
                    //System.out.println("z:" + Utils.toInteger(pos_z));
                    byte[] map_seed = Utils.pop(data, 8);
                    byte[] recommended_send_interval = Utils.pop(data, 4);
                    //System.out.println(toFloat(recommended_send_interval));
                    MinetestBot.STATE = State.InitSent;
                    MinetestBot.toserverInit2();
                    MinetestBot.STATE = State.InitDone;
                    init2Sent = Boolean.TRUE;
                }
            }
        });
        commandHandler.put(Utils.getByteBuffer("0020"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_BLOCKDATA";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0021"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_ADDNODE";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0022"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_REMOVENODE";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0027"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_INVENTORY";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0029"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_TIME_OF_DAY";
            }

            public void run(ByteBuffer data) {
                byte[] time = Utils.pop(data, 2);
                byte[] time_speed = Utils.pop(data, 4);
                if (!receivedMediaSent) {
                    MinetestBot.toserverMediaRecieved();
                    receivedMediaSent = Boolean.TRUE;
                }
                if (!clientReadySent) {
                    MinetestBot.toserverClientReady();
                    MinetestBot.STATE = State.Active;
                    clientReadySent = Boolean.TRUE;
                }
                //System.out.println(toInteger(time));
                //System.out.println(toFloat(time_speed));
            }
        });
        commandHandler.put(Utils.getByteBuffer("0030"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_CHAT_MESSAGE";
            }

            public void run(ByteBuffer data) {
                byte[] length = Utils.pop(data, 2);
                int size = Utils.toInteger(length);
                size = size * 2; // 2 bytes per character
                byte[] msg = new byte[size];
                for (int i = 0; i < size; i++) {
                    msg[i] = data.get();
                }
                try {
                    String chatMessage = new String(msg, "UTF-16");
                    System.out.println(chatMessage);
                } catch (UnsupportedEncodingException ex) {
                }

            }
        });
        commandHandler.put(Utils.getByteBuffer("0031"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_ACTIVE_OBJECT_REMOVE_ADD";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0032"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_ACTIVE_OBJECT_MESSAGES";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0033"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_HP";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0034"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_MOVE_PLAYER";
            }

            public void run(ByteBuffer data) {
                byte[] pos_x = Utils.pop(data, 2);
                byte[] pos_y = Utils.pop(data, 2);
                byte[] pos_z = Utils.pop(data, 2);
                byte[] pitch = Utils.pop(data, 4);
                byte[] yaw = Utils.pop(data, 4);
                
                System.out.println("x:" + Utils.toInteger(pos_x));
                System.out.println("y:" + Utils.toInteger(pos_y));
                System.out.println("z:" + Utils.toInteger(pos_z));
                

            }
        });
        commandHandler.put(Utils.getByteBuffer("0035"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_ACCESS_DENIED_LEGACY";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0036"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_PLAYERITEM";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0037"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_DEATHSCREEN";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0038"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_MEDIA";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0039"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_TOOLDEF";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("003a"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_NODEDEF";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("003b"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_CRAFTITEMDEF";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("003c"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_ANNOUNCE_MEDIA";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("003d"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_ITEMDEF";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("003f"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_PLAY_SOUND";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0040"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_STOP_SOUND";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0041"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_PRIVILEGES";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0042"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_INVENTORY_FORMSPEC";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0043"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_DETACHED_INVENTORY";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0044"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_SHOW_FORMSPEC";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0045"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_MOVEMENT";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0046"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_SPAWN_PARTICLE";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0047"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_ADD_PARTICLESPAWNER";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0048"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_DELETE_PARTICLESPAWNER_LEGACY";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0049"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_HUDADD";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("004a"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_HUDRM";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("004b"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_HUDCHANGE";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("004c"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_HUD_SET_FLAGS";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("004d"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_HUD_SET_PARAM";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("004e"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_BREATH";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("004f"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_SET_SKY";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0050"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_OVERRIDE_DAY_NIGHT_RATIO";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0051"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_LOCAL_PLAYER_ANIMATIONS";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0052"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_EYE_OFFSET";
            }

            public void run(ByteBuffer data) {
            }
        });
        commandHandler.put(Utils.getByteBuffer("0053"), new CommandHandler() {
            public String getName() {
                return "TOCLIENT_DELETE_PARTICLESPAWNER";
            }

            public void run(ByteBuffer data) {
            }
        });
    }

}
