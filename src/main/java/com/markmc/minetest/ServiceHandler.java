package com.markmc.minetest;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.markmc.minetest.MinetestBot.State;

import static com.markmc.minetest.Constants.FOUR_BYTES;
import static com.markmc.minetest.Constants.ONE_BYTE;
import static com.markmc.minetest.Constants.TWO_BYTES;

/**
 * ServiceHandler.
 * Derived from networkprotocol.h
 * <p>
 * @author markmc
 */
public class ServiceHandler {

  public static Boolean init2Sent = Boolean.FALSE;

  public static Boolean receivedMediaSent = Boolean.FALSE;

  public static Boolean clientReadySent = Boolean.FALSE;

  public static final Map<ByteBuffer, CommandHandler> COMMAND_HANDLER = new HashMap<ByteBuffer, CommandHandler>();

  static {
    COMMAND_HANDLER.put(Utils.getByteBuffer("0002"), new AbstractCommandHandler() {

      public String getName() {
        return "TOCLIENT_HELLO";
      }

    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0003"), new AbstractCommandHandler() {

      public String getName() {
        return "TOCLIENT_AUTH_ACCEPT";
      }

    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("000A"), new AbstractCommandHandler() {

      public String getName() {
        return "TOCLIENT_ACCESS_DENIED";
      }

    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0010"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_INIT";
      }

      @Override
      public void run(final ByteBuffer data) {
        final int EIGHT_BYTES = 8;
        if (!init2Sent) {
          byte[] version = Utils.pop(data, ONE_BYTE);
          byte[] pos_x = Utils.pop(data, TWO_BYTES);
          byte[] pos_y = Utils.pop(data, TWO_BYTES);
          byte[] pos_z = Utils.pop(data, TWO_BYTES);
                    //System.out.println("x:" + Utils.toInteger(pos_x));
          //System.out.println("y:" + Utils.toInteger(pos_y));
          //System.out.println("z:" + Utils.toInteger(pos_z));
          byte[] map_seed = Utils.pop(data, EIGHT_BYTES);
          byte[] recommended_send_interval = Utils.pop(data, FOUR_BYTES);
          //System.out.println(toFloat(recommended_send_interval));
          MinetestBot.STATE = State.InitSent;
          MinetestBot.toserverInit2();
          MinetestBot.STATE = State.InitDone;
          init2Sent = Boolean.TRUE;
        }
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0020"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_BLOCKDATA";
      }

    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0021"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_ADDNODE";
      }

    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0022"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_REMOVENODE";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0027"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_INVENTORY";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0029"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_TIME_OF_DAY";
      }

      @Override
      public void run(final ByteBuffer data) {
        byte[] time = Utils.pop(data, TWO_BYTES);
        byte[] time_speed = Utils.pop(data, FOUR_BYTES);
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
    COMMAND_HANDLER.put(Utils.getByteBuffer("0030"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_CHAT_MESSAGE";
      }

      @Override
      public void run(final ByteBuffer data) {
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
    COMMAND_HANDLER.put(Utils.getByteBuffer("0031"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_ACTIVE_OBJECT_REMOVE_ADD";
      }

    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0032"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_ACTIVE_OBJECT_MESSAGES";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0033"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_HP";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0034"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_MOVE_PLAYER";
      }

      @Override
      public void run(final ByteBuffer data) {
        byte[] pos_x = Utils.pop(data, TWO_BYTES);
        byte[] pos_y = Utils.pop(data, TWO_BYTES);
        byte[] pos_z = Utils.pop(data, TWO_BYTES);
        byte[] pitch = Utils.pop(data, FOUR_BYTES);
        byte[] yaw = Utils.pop(data, FOUR_BYTES);

        System.out.println("x:" + Utils.toInteger(pos_x));
        System.out.println("y:" + Utils.toInteger(pos_y));
        System.out.println("z:" + Utils.toInteger(pos_z));

      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0035"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_ACCESS_DENIED_LEGACY";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0036"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_PLAYERITEM";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0037"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_DEATHSCREEN";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0038"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_MEDIA";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0039"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_TOOLDEF";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("003a"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_NODEDEF";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("003b"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_CRAFTITEMDEF";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("003c"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_ANNOUNCE_MEDIA";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("003d"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_ITEMDEF";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("003f"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_PLAY_SOUND";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0040"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_STOP_SOUND";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0041"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_PRIVILEGES";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0042"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_INVENTORY_FORMSPEC";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0043"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_DETACHED_INVENTORY";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0044"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_SHOW_FORMSPEC";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0045"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_MOVEMENT";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0046"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_SPAWN_PARTICLE";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0047"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_ADD_PARTICLESPAWNER";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0048"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_DELETE_PARTICLESPAWNER_LEGACY";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0049"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_HUDADD";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("004a"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_HUDRM";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("004b"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_HUDCHANGE";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("004c"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_HUD_SET_FLAGS";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("004d"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_HUD_SET_PARAM";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("004e"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_BREATH";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("004f"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_SET_SKY";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0050"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_OVERRIDE_DAY_NIGHT_RATIO";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0051"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_LOCAL_PLAYER_ANIMATIONS";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0052"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_EYE_OFFSET";
      }
    });
    COMMAND_HANDLER.put(Utils.getByteBuffer("0053"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_DELETE_PARTICLESPAWNER";
      }
    });
  }

}
