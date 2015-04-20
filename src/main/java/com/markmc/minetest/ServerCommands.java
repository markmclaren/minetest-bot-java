package com.markmc.minetest;

import java.util.HashMap;
import java.util.Map;

import static com.markmc.minetest.Utils.getByteArray;

/**
 * ServerCommands.
 * <p>
 * @author markmc
 */
public class ServerCommands {

  private static final Map<String, byte[]> BYTES = new HashMap<String, byte[]>();

  private static final Map<String, Integer> CHANNELS = new HashMap<String, Integer>();

  private static final Map<String, Boolean> RELIABLES = new HashMap<String, Boolean>();

  public static final String TOSERVER_INIT = "TOSERVER_INIT";

  public static final String TOSERVER_INIT_LEGACY = "TOSERVER_INIT_LEGACY";

  public static final String TOSERVER_INIT2 = "TOSERVER_INIT2";

  public static final String TOSERVER_PLAYERPOS = "TOSERVER_PLAYERPOS";

  public static final String TOSERVER_GOTBLOCKS = "TOSERVER_GOTBLOCKS";

  public static final String TOSERVER_DELETEDBLOCKS = "TOSERVER_DELETEDBLOCKS";

  public static final String TOSERVER_CLICK_OBJECT = "TOSERVER_CLICK_OBJECT";

  public static final String TOSERVER_GROUND_ACTION = "TOSERVER_GROUND_ACTION";

  public static final String TOSERVER_RELEASE = "TOSERVER_RELEASE";

  public static final String TOSERVER_SIGNTEXT = "TOSERVER_SIGNTEXT";

  public static final String TOSERVER_INVENTORY_ACTION = "TOSERVER_INVENTORY_ACTION";

  public static final String TOSERVER_CHAT_MESSAGE = "TOSERVER_CHAT_MESSAGE";

  public static final String TOSERVER_SIGNNODETEXT = "TOSERVER_SIGNNODETEXT";

  public static final String TOSERVER_CLICK_ACTIVEOBJECT = "TOSERVER_CLICK_ACTIVEOBJECT";

  public static final String TOSERVER_DAMAGE = "TOSERVER_DAMAGE";

  public static final String TOSERVER_PASSWORD_LEGACY = "TOSERVER_PASSWORD_LEGACY";

  public static final String TOSERVER_PLAYERITEM = "TOSERVER_PLAYERITEM";

  public static final String TOSERVER_RESPAWN = "TOSERVER_RESPAWN";

  public static final String TOSERVER_INTERACT = "TOSERVER_INTERACT";

  public static final String TOSERVER_REMOVED_SOUNDS = "TOSERVER_REMOVED_SOUNDS";

  public static final String TOSERVER_NODEMETA_FIELDS = "TOSERVER_NODEMETA_FIELDS";

  public static final String TOSERVER_INVENTORY_FIELDS = "TOSERVER_INVENTORY_FIELDS";

  public static final String TOSERVER_PASSWORD = "TOSERVER_PASSWORD";

  public static final String TOSERVER_REQUEST_MEDIA = "TOSERVER_REQUEST_MEDIA";

  public static final String TOSERVER_RECEIVED_MEDIA = "TOSERVER_RECEIVED_MEDIA";

  public static final String TOSERVER_BREATH = "TOSERVER_BREATH";

  public static final String TOSERVER_CLIENT_READY = "TOSERVER_CLIENT_READY";

  static {
    BYTES.put(TOSERVER_INIT, getByteArray("000F"));
    BYTES.put(TOSERVER_INIT_LEGACY, getByteArray("0010"));
    BYTES.put(TOSERVER_INIT2, getByteArray("0011"));
    BYTES.put(TOSERVER_PLAYERPOS, getByteArray("0023"));
    BYTES.put(TOSERVER_GOTBLOCKS, getByteArray("0024"));
    BYTES.put(TOSERVER_DELETEDBLOCKS, getByteArray("0025"));
    BYTES.put(TOSERVER_CLICK_OBJECT, getByteArray("0027"));
    BYTES.put(TOSERVER_GROUND_ACTION, getByteArray("0028"));
    BYTES.put(TOSERVER_RELEASE, getByteArray("0029"));
    BYTES.put(TOSERVER_SIGNTEXT, getByteArray("0030"));
    BYTES.put(TOSERVER_INVENTORY_ACTION, getByteArray("0031"));
    BYTES.put(TOSERVER_CHAT_MESSAGE, getByteArray("0032"));
    BYTES.put(TOSERVER_SIGNNODETEXT, getByteArray("0033"));
    BYTES.put(TOSERVER_CLICK_ACTIVEOBJECT, getByteArray("0034"));
    BYTES.put(TOSERVER_DAMAGE, getByteArray("0035"));
    BYTES.put(TOSERVER_PASSWORD_LEGACY, getByteArray("0036"));
    BYTES.put(TOSERVER_PLAYERITEM, getByteArray("0037"));
    BYTES.put(TOSERVER_RESPAWN, getByteArray("0038"));
    BYTES.put(TOSERVER_INTERACT, getByteArray("0039"));
    BYTES.put(TOSERVER_REMOVED_SOUNDS, getByteArray("003a"));
    BYTES.put(TOSERVER_NODEMETA_FIELDS, getByteArray("003b"));
    BYTES.put(TOSERVER_INVENTORY_FIELDS, getByteArray("003c"));
    BYTES.put(TOSERVER_PASSWORD, getByteArray("003d"));
    BYTES.put(TOSERVER_REQUEST_MEDIA, getByteArray("0040"));
    BYTES.put(TOSERVER_RECEIVED_MEDIA, getByteArray("0041"));
    BYTES.put(TOSERVER_BREATH, getByteArray("0042"));
    BYTES.put(TOSERVER_CLIENT_READY, getByteArray("0043"));
    //
    CHANNELS.put(TOSERVER_INIT, 1);
    CHANNELS.put(TOSERVER_INIT_LEGACY, 1);
    CHANNELS.put(TOSERVER_INIT2, 1);
    CHANNELS.put(TOSERVER_PLAYERPOS, 0);
    CHANNELS.put(TOSERVER_GOTBLOCKS, 2);
    CHANNELS.put(TOSERVER_DELETEDBLOCKS, 2);
    CHANNELS.put(TOSERVER_CLICK_OBJECT, 0);
    CHANNELS.put(TOSERVER_GROUND_ACTION, 0);
    CHANNELS.put(TOSERVER_RELEASE, 0);
    CHANNELS.put(TOSERVER_SIGNTEXT, 0);
    CHANNELS.put(TOSERVER_INVENTORY_ACTION, 0);
    CHANNELS.put(TOSERVER_CHAT_MESSAGE, 0);
    CHANNELS.put(TOSERVER_SIGNNODETEXT, 0);
    CHANNELS.put(TOSERVER_CLICK_ACTIVEOBJECT, 0);
    CHANNELS.put(TOSERVER_DAMAGE, 0);
    CHANNELS.put(TOSERVER_PASSWORD_LEGACY, 0);
    CHANNELS.put(TOSERVER_PLAYERITEM, 0);
    CHANNELS.put(TOSERVER_RESPAWN, 0);
    CHANNELS.put(TOSERVER_INTERACT, 0);
    CHANNELS.put(TOSERVER_REMOVED_SOUNDS, 1);
    CHANNELS.put(TOSERVER_NODEMETA_FIELDS, 0);
    CHANNELS.put(TOSERVER_INVENTORY_FIELDS, 0);
    CHANNELS.put(TOSERVER_PASSWORD, 0);
    CHANNELS.put(TOSERVER_REQUEST_MEDIA, 1);
    CHANNELS.put(TOSERVER_RECEIVED_MEDIA, 1);
    CHANNELS.put(TOSERVER_BREATH, 0);
    CHANNELS.put(TOSERVER_CLIENT_READY, 0);
    //
    RELIABLES.put(TOSERVER_INIT, Boolean.FALSE);
    RELIABLES.put(TOSERVER_INIT_LEGACY, Boolean.FALSE);
    RELIABLES.put(TOSERVER_INIT2, Boolean.TRUE);
    RELIABLES.put(TOSERVER_PLAYERPOS, Boolean.FALSE);
    RELIABLES.put(TOSERVER_GOTBLOCKS, Boolean.TRUE);
    RELIABLES.put(TOSERVER_DELETEDBLOCKS, Boolean.TRUE);
    RELIABLES.put(TOSERVER_CLICK_OBJECT, Boolean.FALSE);
    RELIABLES.put(TOSERVER_GROUND_ACTION, Boolean.FALSE);
    RELIABLES.put(TOSERVER_RELEASE, Boolean.FALSE);
    RELIABLES.put(TOSERVER_SIGNTEXT, Boolean.FALSE);
    RELIABLES.put(TOSERVER_INVENTORY_ACTION, Boolean.TRUE);
    RELIABLES.put(TOSERVER_CHAT_MESSAGE, Boolean.TRUE);
    RELIABLES.put(TOSERVER_SIGNNODETEXT, Boolean.FALSE);
    RELIABLES.put(TOSERVER_CLICK_ACTIVEOBJECT, Boolean.FALSE);
    RELIABLES.put(TOSERVER_DAMAGE, Boolean.TRUE);
    RELIABLES.put(TOSERVER_PASSWORD_LEGACY, Boolean.TRUE);
    RELIABLES.put(TOSERVER_PLAYERITEM, Boolean.TRUE);
    RELIABLES.put(TOSERVER_RESPAWN, Boolean.TRUE);
    RELIABLES.put(TOSERVER_INTERACT, Boolean.TRUE);
    RELIABLES.put(TOSERVER_REMOVED_SOUNDS, Boolean.TRUE);
    RELIABLES.put(TOSERVER_NODEMETA_FIELDS, Boolean.TRUE);
    RELIABLES.put(TOSERVER_INVENTORY_FIELDS, Boolean.TRUE);
    RELIABLES.put(TOSERVER_PASSWORD, Boolean.TRUE);
    RELIABLES.put(TOSERVER_REQUEST_MEDIA, Boolean.TRUE);
    RELIABLES.put(TOSERVER_RECEIVED_MEDIA, Boolean.TRUE);
    RELIABLES.put(TOSERVER_BREATH, Boolean.TRUE);
    RELIABLES.put(TOSERVER_CLIENT_READY, Boolean.TRUE);
  }

  /**
   * getServerCommand.
   * @param serverCommand String
   * @return ServerCommand
   */
  public static ServerCommand getServerCommand(final String serverCommand) {
    return new ServerCommand() {

      public byte[] getCommandBytes() {
        return BYTES.get(serverCommand);
      }

      public int getChannel() {
        return CHANNELS.get(serverCommand);
      }

      public Boolean isReliable() {
        return RELIABLES.get(serverCommand);
      }

      public byte[] getChannelBytes() {
        return Utils.u8(CHANNELS.get(serverCommand));
      }

    };
  }

  /**
   * Private constructor.
   */
  private ServerCommands() {
  }

}
