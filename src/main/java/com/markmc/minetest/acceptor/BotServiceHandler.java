package com.markmc.minetest.acceptor;

import com.markmc.minetest.connector.ClientState;
import com.markmc.minetest.bot.Bot;
import com.markmc.minetest.connector.Connect;
import com.markmc.minetest.utils.Utils;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static com.markmc.minetest.utils.Constants.EIGHT_BYTES;
import static com.markmc.minetest.utils.Constants.FOUR_BYTES;
import static com.markmc.minetest.utils.Constants.ONE_BYTE;
import static com.markmc.minetest.utils.Constants.TWO_BYTES;

/**
 * ServiceHandler.
 * Derived from networkprotocol.h
 * <p>
 * @author markmc
 */
public class BotServiceHandler {

  private Boolean init2Sent = Boolean.FALSE;

  private Boolean receivedMediaSent = Boolean.FALSE;

  private Boolean clientReadySent = Boolean.FALSE;

  public final Map<ByteBuffer, CommandHandler> CMD_HANDLER
    = new HashMap<ByteBuffer, CommandHandler>();

  public BotServiceHandler(final Bot bot, final Connect connect) {
  
    CMD_HANDLER.put(Utils.getByteBuffer("0002"), new AbstractCommandHandler() {

      public String getName() {
        return "TOCLIENT_HELLO";
      }

    });
    CMD_HANDLER.put(Utils.getByteBuffer("0003"), new AbstractCommandHandler() {

      public String getName() {
        return "TOCLIENT_AUTH_ACCEPT";
      }

    });
    CMD_HANDLER.put(Utils.getByteBuffer("000A"), new AbstractCommandHandler() {

      public String getName() {
        return "TOCLIENT_ACCESS_DENIED";
      }

    });
    CMD_HANDLER.put(Utils.getByteBuffer("0010"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_INIT";
      }

      @Override
      public void process(final ByteBuffer data) {
        if (!init2Sent) {
          byte[] version = Utils.pop(data, ONE_BYTE);
          byte[] posX = Utils.pop(data, TWO_BYTES);
          byte[] posY = Utils.pop(data, TWO_BYTES);
          byte[] posZ = Utils.pop(data, TWO_BYTES);
          //System.out.println("x:" + Utils.toInteger(posX));
          //System.out.println("y:" + Utils.toInteger(posY));
          //System.out.println("z:" + Utils.toInteger(posZ));
          byte[] mapSeed = Utils.pop(data, EIGHT_BYTES);
          byte[] recommendedSendInterval = Utils.pop(data, FOUR_BYTES);
          //System.out.println(toFloat(recommendedSendInterval));
          bot.setState(ClientState.InitSent);          
          connect.toserverInit2();
          bot.setState(ClientState.InitDone);
          init2Sent = Boolean.TRUE;
        }
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0020"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_BLOCKDATA";
      }

    });
    CMD_HANDLER.put(Utils.getByteBuffer("0021"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_ADDNODE";
      }

    });
    CMD_HANDLER.put(Utils.getByteBuffer("0022"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_REMOVENODE";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0027"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_INVENTORY";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0029"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_TIME_OF_DAY";
      }

      @Override
      public void process(final ByteBuffer data) {
        byte[] time = Utils.pop(data, TWO_BYTES);
        byte[] timeSpeed = Utils.pop(data, FOUR_BYTES);
        if (!receivedMediaSent) {
          connect.toserverMediaRecieved();
          receivedMediaSent = Boolean.TRUE;
        }
        if (!clientReadySent) {
          connect.toserverClientReady();
          bot.setState(ClientState.Active);
          clientReadySent = Boolean.TRUE;
        }
                //System.out.println(toInteger(time));
        //System.out.println(toFloat(timeSpeed));
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0030"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_CHAT_MESSAGE";
      }

      @Override
      public void process(final ByteBuffer data) {
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
    CMD_HANDLER.put(Utils.getByteBuffer("0031"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_ACTIVE_OBJECT_REMOVE_ADD";
      }

    });
    CMD_HANDLER.put(Utils.getByteBuffer("0032"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_ACTIVE_OBJECT_MESSAGES";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0033"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_HP";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0034"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_MOVE_PLAYER";
      }

      @Override
      public void process(final ByteBuffer data) {
        byte[] posX = Utils.pop(data, TWO_BYTES);
        byte[] posY = Utils.pop(data, TWO_BYTES);
        byte[] posZ = Utils.pop(data, TWO_BYTES);
        byte[] pitch = Utils.pop(data, FOUR_BYTES);
        byte[] yaw = Utils.pop(data, FOUR_BYTES);

        System.out.println("x:" + Utils.toInteger(posX));
        System.out.println("y:" + Utils.toInteger(posY));
        System.out.println("z:" + Utils.toInteger(posZ));

      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0035"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_ACCESS_DENIED_LEGACY";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0036"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_PLAYERITEM";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0037"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_DEATHSCREEN";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0038"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_MEDIA";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0039"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_TOOLDEF";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("003a"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_NODEDEF";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("003b"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_CRAFTITEMDEF";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("003c"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_ANNOUNCE_MEDIA";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("003d"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_ITEMDEF";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("003f"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_PLAY_SOUND";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0040"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_STOP_SOUND";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0041"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_PRIVILEGES";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0042"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_INVENTORY_FORMSPEC";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0043"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_DETACHED_INVENTORY";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0044"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_SHOW_FORMSPEC";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0045"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_MOVEMENT";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0046"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_SPAWN_PARTICLE";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0047"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_ADD_PARTICLESPAWNER";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0048"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_DELETE_PARTICLESPAWNER_LEGACY";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0049"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_HUDADD";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("004a"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_HUDRM";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("004b"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_HUDCHANGE";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("004c"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_HUD_SET_FLAGS";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("004d"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_HUD_SET_PARAM";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("004e"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_BREATH";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("004f"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_SET_SKY";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0050"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_OVERRIDE_DAY_NIGHT_RATIO";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0051"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_LOCAL_PLAYER_ANIMATIONS";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0052"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_EYE_OFFSET";
      }
    });
    CMD_HANDLER.put(Utils.getByteBuffer("0053"), new AbstractCommandHandler() {
      public String getName() {
        return "TOCLIENT_DELETE_PARTICLESPAWNER";
      }
    });
  }

}
