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

  private static final Map<String, byte[]> bytes = new HashMap<String, byte[]>();

  private static final Map<String, Integer> channels = new HashMap<String, Integer>();

  private static final Map<String, Boolean> reliables = new HashMap<String, Boolean>();

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
    bytes.put(TOSERVER_INIT, getByteArray("000F"));
    bytes.put(TOSERVER_INIT_LEGACY, getByteArray("0010"));
    bytes.put(TOSERVER_INIT2, getByteArray("0011"));
    bytes.put(TOSERVER_PLAYERPOS, getByteArray("0023"));
    bytes.put(TOSERVER_GOTBLOCKS, getByteArray("0024"));
    bytes.put(TOSERVER_DELETEDBLOCKS, getByteArray("0025"));
    bytes.put(TOSERVER_CLICK_OBJECT, getByteArray("0027"));
    bytes.put(TOSERVER_GROUND_ACTION, getByteArray("0028"));
    bytes.put(TOSERVER_RELEASE, getByteArray("0029"));
    bytes.put(TOSERVER_SIGNTEXT, getByteArray("0030"));
    bytes.put(TOSERVER_INVENTORY_ACTION, getByteArray("0031"));
    bytes.put(TOSERVER_CHAT_MESSAGE, getByteArray("0032"));
    bytes.put(TOSERVER_SIGNNODETEXT, getByteArray("0033"));
    bytes.put(TOSERVER_CLICK_ACTIVEOBJECT, getByteArray("0034"));
    bytes.put(TOSERVER_DAMAGE, getByteArray("0035"));
    bytes.put(TOSERVER_PASSWORD_LEGACY, getByteArray("0036"));
    bytes.put(TOSERVER_PLAYERITEM, getByteArray("0037"));
    bytes.put(TOSERVER_RESPAWN, getByteArray("0038"));
    bytes.put(TOSERVER_INTERACT, getByteArray("0039"));
    bytes.put(TOSERVER_REMOVED_SOUNDS, getByteArray("003a"));
    bytes.put(TOSERVER_NODEMETA_FIELDS, getByteArray("003b"));
    bytes.put(TOSERVER_INVENTORY_FIELDS, getByteArray("003c"));
    bytes.put(TOSERVER_PASSWORD, getByteArray("003d"));
    bytes.put(TOSERVER_REQUEST_MEDIA, getByteArray("0040"));
    bytes.put(TOSERVER_RECEIVED_MEDIA, getByteArray("0041"));
    bytes.put(TOSERVER_BREATH, getByteArray("0042"));
    bytes.put(TOSERVER_CLIENT_READY, getByteArray("0043"));
    //
    channels.put(TOSERVER_INIT, 1);
    channels.put(TOSERVER_INIT_LEGACY, 1);
    channels.put(TOSERVER_INIT2, 1);
    channels.put(TOSERVER_PLAYERPOS, 0);
    channels.put(TOSERVER_GOTBLOCKS, 2);
    channels.put(TOSERVER_DELETEDBLOCKS, 2);
    channels.put(TOSERVER_CLICK_OBJECT, 0);
    channels.put(TOSERVER_GROUND_ACTION, 0);
    channels.put(TOSERVER_RELEASE, 0);
    channels.put(TOSERVER_SIGNTEXT, 0);
    channels.put(TOSERVER_INVENTORY_ACTION, 0);
    channels.put(TOSERVER_CHAT_MESSAGE, 0);
    channels.put(TOSERVER_SIGNNODETEXT, 0);
    channels.put(TOSERVER_CLICK_ACTIVEOBJECT, 0);
    channels.put(TOSERVER_DAMAGE, 0);
    channels.put(TOSERVER_PASSWORD_LEGACY, 0);
    channels.put(TOSERVER_PLAYERITEM, 0);
    channels.put(TOSERVER_RESPAWN, 0);
    channels.put(TOSERVER_INTERACT, 0);
    channels.put(TOSERVER_REMOVED_SOUNDS, 1);
    channels.put(TOSERVER_NODEMETA_FIELDS, 0);
    channels.put(TOSERVER_INVENTORY_FIELDS, 0);
    channels.put(TOSERVER_PASSWORD, 0);
    channels.put(TOSERVER_REQUEST_MEDIA, 1);
    channels.put(TOSERVER_RECEIVED_MEDIA, 1);
    channels.put(TOSERVER_BREATH, 0);
    channels.put(TOSERVER_CLIENT_READY, 0);
    //
    reliables.put(TOSERVER_INIT, Boolean.FALSE);
    reliables.put(TOSERVER_INIT_LEGACY, Boolean.FALSE);
    reliables.put(TOSERVER_INIT2, Boolean.TRUE);
    reliables.put(TOSERVER_PLAYERPOS, Boolean.FALSE);
    reliables.put(TOSERVER_GOTBLOCKS, Boolean.TRUE);
    reliables.put(TOSERVER_DELETEDBLOCKS, Boolean.TRUE);
    reliables.put(TOSERVER_CLICK_OBJECT, Boolean.FALSE);
    reliables.put(TOSERVER_GROUND_ACTION, Boolean.FALSE);
    reliables.put(TOSERVER_RELEASE, Boolean.FALSE);
    reliables.put(TOSERVER_SIGNTEXT, Boolean.FALSE);
    reliables.put(TOSERVER_INVENTORY_ACTION, Boolean.TRUE);
    reliables.put(TOSERVER_CHAT_MESSAGE, Boolean.TRUE);
    reliables.put(TOSERVER_SIGNNODETEXT, Boolean.FALSE);
    reliables.put(TOSERVER_CLICK_ACTIVEOBJECT, Boolean.FALSE);
    reliables.put(TOSERVER_DAMAGE, Boolean.TRUE);
    reliables.put(TOSERVER_PASSWORD_LEGACY, Boolean.TRUE);
    reliables.put(TOSERVER_PLAYERITEM, Boolean.TRUE);
    reliables.put(TOSERVER_RESPAWN, Boolean.TRUE);
    reliables.put(TOSERVER_INTERACT, Boolean.TRUE);
    reliables.put(TOSERVER_REMOVED_SOUNDS, Boolean.TRUE);
    reliables.put(TOSERVER_NODEMETA_FIELDS, Boolean.TRUE);
    reliables.put(TOSERVER_INVENTORY_FIELDS, Boolean.TRUE);
    reliables.put(TOSERVER_PASSWORD, Boolean.TRUE);
    reliables.put(TOSERVER_REQUEST_MEDIA, Boolean.TRUE);
    reliables.put(TOSERVER_RECEIVED_MEDIA, Boolean.TRUE);
    reliables.put(TOSERVER_BREATH, Boolean.TRUE);
    reliables.put(TOSERVER_CLIENT_READY, Boolean.TRUE);
  }

  public static ServerCommand getServerCommand(final String serverCommand) {
    return new ServerCommand() {

      public byte[] getCommandBytes() {
        return bytes.get(serverCommand);
      }

      public int getChannel() {
        return channels.get(serverCommand);
      }

      public Boolean isReliable() {
        return reliables.get(serverCommand);
      }

      public byte[] getChannelBytes() {
        return Utils.U8(channels.get(serverCommand));
      }

    };
  }

}
