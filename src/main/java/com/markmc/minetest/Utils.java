package com.markmc.minetest;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import com.google.common.io.BaseEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * Utils.
 * <p>
 * @author markmc
 */
public class Utils {

  private static final int ONE_BYTE = 1;
  private static final int TWO_BYTES = 2;
  private static final int FOUR_BYTES = 4;
  private static final int BASE16 = 16;

  /**
   * Private constructor.
   */
  private Utils(){
  }

  /**
   * toFloat.
   * <p>
   * @param b byte[]
   * <p>
   * @return float
   */
  public static float toFloat(final byte[] b) {
    return ByteBuffer.wrap(b).getFloat();
  }

  /**
   * getByteBuffer.
   * <p>
   * @param hex String
   * <p>
   * @return ByteBuffer
   */
  public static ByteBuffer getByteBuffer(final String hex) {
    try {
      return ByteBuffer.wrap(Hex.decodeHex(hex.toCharArray()));
    } catch (DecoderException ex) {
      System.out.println(ex.getMessage());
    }
    return null;
  }

  /**
   * toString.
   * <p>
   * @param b byte[]
   * <p>
   * @return String
   */
  public static String toString(final byte[] b) {
    return Hex.encodeHexString(b);
  }

  /**
   * toInteger.
   * <p>
   * @param b byte[]
   * <p>
   * @return int
   */
  public static int toInteger(final byte[] b) {
    return new BigInteger(BaseEncoding.base16().encode(b), BASE16).intValue();
  }

  /**
   * isEqual.
   * <p>
   * @param b1 byte[]
   * @param b2 byte[]
   * <p>
   * @return if byte arrays are equal
   */
  public static boolean isEqual(final byte[] b1, final byte[] b2) {
    return ByteBuffer.wrap(b1).equals(ByteBuffer.wrap(b2));
  }

  /**
   * makeByteArray.
   * <p>
   * @param v    byte[]
   * @param size size
   * <p>
   * @return byte[]
   */
  public static byte[] makeByteArray(final byte[] v, final int size) {
    return ByteBuffer.allocate(size).put(v).array();
  }

  /**
   * getByteArray.
   * <p>
   * @param hex String
   * <p>
   * @return byte[]
   */
  public static byte[] getByteArray(final String hex) {
    try {
      return Hex.decodeHex(hex.toCharArray());
    } catch (DecoderException ex) {
    }
    return null;
  }

  /**
   * pop.
   * <p>
   * @param bbuff ByteBuffer
   * @param pop int
   * <p>
   * @return byte[]
   */
  public static byte[] pop(final ByteBuffer bbuff, final int pop) {
    byte[] dest = new byte[pop];
    bbuff.get(dest, 0, pop);
    return dest;
  }

  /**
   * U8 = 1 byte.
   * <p>
   * @param v int
   * <p>
   * @return byte[]
   */
  public static byte[] u8(final int v) {
    return ByteBuffer.allocate(ONE_BYTE).put((byte) v).array();
  }

  /**
   * U16 = 2 bytes.
   * <p>
   * @param v int
   * <p>
   * @return byte[]
   */
  public static byte[] u16(final int v) {
    return ByteBuffer.allocate(TWO_BYTES).putShort((short) v).array();
  }

  /**
   * U32 = 4 bytes.
   * <p>
   * @param v int
   * <p>
   * @return byte[]
   */
  public static byte[] u32(final int v) {
    return ByteBuffer.allocate(FOUR_BYTES).putInt(v).array();
  }

}
