package com.markmc.minetest.utils;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static com.markmc.minetest.utils.Utils.getByteArray;
import static com.markmc.minetest.utils.Utils.u16;
import static com.markmc.minetest.utils.Utils.u8;

/**
 * Constants.
 * <p>
 * @author markmc
 */
public final class Constants {

  /**
   * ONE_BYTE.
   */
  public static final int ONE_BYTE = 1;

  /**
   * TWO_BYTES.
   */
  public static final int TWO_BYTES = 2;

  /**
   * FOUR_BYTES.
   */
  public static final int FOUR_BYTES = 4;

  /**
   * EIGHT_BYTES.
   */
  public static final int EIGHT_BYTES = 8;

  /**
   * SEQNUM_MAX.
   */
  public static final int SEQNUM_MAX = 65535;

  /**
   * SEQNUM_INITIAL.
   */
  public static final int SEQNUM_INITIAL = 65500;

  /**
   * MAX_PLAYER_NAME_SIZE.
   */
  public static final int MAX_PLAYER_NAME_SIZE = 20;

  /**
   * MAX_PASSWORD_SIZE.
   */
  public static final int MAX_PASSWORD_SIZE = 28;

  /**
   * DEFAULT_SERVER_PORT.
   */
  public static final int DEFAULT_SERVER_PORT = 30000;

  /**
   * PROTOCOL_ID.
   */
  public static final byte[] PROTOCOL_ID = getByteArray("4f457403");

  /**
   * PEER_ID_INEXISTENT.
   */
  public static final byte[] PEER_ID_INEXISTENT = getByteArray("0000");

  /**
   * TYPE_CONTROL.
   */
  public static final byte[] TYPE_CONTROL = getByteArray("00");

  /**
   * TYPE_ORIGINAL.
   */
  public static final byte[] TYPE_ORIGINAL = getByteArray("01");

  /**
   * TYPE_SPLIT.
   */
  public static final byte[] TYPE_SPLIT = getByteArray("02");

  /**
   * TYPE_RELIABLE.
   */
  public static final byte[] TYPE_RELIABLE = getByteArray("03");

  /**
   * CONTROLTYPE_ACK.
   */
  public static final byte[] CONTROLTYPE_ACK = getByteArray("00");

  /**
   * CONTROLTYPE_SET_PEER_ID.
   */
  public static final byte[] CONTROLTYPE_SET_PEER_ID = getByteArray("01");

  /**
   * CONTROLTYPE_PING.
   */
  public static final byte[] CONTROLTYPE_PING = getByteArray("02");

  /**
   * CONTROLTYPE_DISCO.
   */
  public static final byte[] CONTROLTYPE_DISCO = getByteArray("03");

  /**
   * CONTROLTYPE_ENABLE_BIG_SEND_WINDOW.
   */
  public static final byte[] CONTROLTYPE_ENABLE_BIG_SEND_WINDOW
    = getByteArray("04");

  /**
   * SER_FMT_VER_HIGHEST_READ.
   */
  public static final byte[] SER_FMT_VER_HIGHEST_READ = u8(26);

  /**
   * LATEST_PROTOCOL_VERSION.
   */
  public static final byte[] LATEST_PROTOCOL_VERSION = u16(24);

  /**
   * CLIENT_PROTOCOL_VERSION_MIN.
   */
  public static final byte[] CLIENT_PROTOCOL_VERSION_MIN = u16(13);

  /**
   * CLIENT_PROTOCOL_VERSION_MAX.
   */
  public static final byte[] CLIENT_PROTOCOL_VERSION_MAX
    = LATEST_PROTOCOL_VERSION;

  /**
   * VERSION_MAJOR.
   */
  public static final int VERSION_MAJOR = 0;

  /**
   * VERSION_MINOR.
   */
  public static final int VERSION_MINOR = 4;

  /**
   * VERSION_PATCH.
   */
  public static final int VERSION_PATCH = 12;

  /**
   * VERSION_STRING.
   */
  public static final String VERSION_STRING
    = String.format("%s.%s.%s-Minetest-Bot", VERSION_MAJOR,
                    VERSION_MINOR, VERSION_PATCH);

  /**
   * TYPES.
   */
  public static final Map<ByteBuffer, String> TYPES
    = new HashMap<ByteBuffer, String>();

  /**
   * CONTROLTYPES.
   */
  public static final Map<ByteBuffer, String> CONTROLTYPES
    = new HashMap<ByteBuffer, String>();

  static {
    TYPES.put(ByteBuffer.wrap(TYPE_RELIABLE), "TYPE_RELIABLE");
    TYPES.put(ByteBuffer.wrap(TYPE_ORIGINAL), "TYPE_ORIGINAL");
    TYPES.put(ByteBuffer.wrap(TYPE_CONTROL), "TYPE_CONTROL");
    TYPES.put(ByteBuffer.wrap(TYPE_SPLIT), "TYPE_SPLIT");

    CONTROLTYPES.put(
      ByteBuffer.wrap(CONTROLTYPE_ACK), "CONTROLTYPE_ACK");
    CONTROLTYPES.put(
      ByteBuffer.wrap(CONTROLTYPE_SET_PEER_ID), "CONTROLTYPE_SET_PEER_ID");
    CONTROLTYPES.put(
      ByteBuffer.wrap(CONTROLTYPE_PING), "CONTROLTYPE_PING");
    CONTROLTYPES.put(
      ByteBuffer.wrap(CONTROLTYPE_DISCO), "CONTROLTYPE_DISCO");
  }

  /**
   * Private constructor.
   */
  private Constants() {
  }

}
