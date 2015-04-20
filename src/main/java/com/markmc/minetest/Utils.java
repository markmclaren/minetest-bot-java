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

  public static float toFloat(final byte[] b) {
    return ByteBuffer.wrap(b).getFloat();
  }

  public static ByteBuffer getByteBuffer(final String hex) {
    try {
      return ByteBuffer.wrap(Hex.decodeHex(hex.toCharArray()));
    } catch (DecoderException ex) {
      System.out.println(ex.getMessage());
    }
    return null;
  }

  public static String toString(final byte[] b) {
    return Hex.encodeHexString(b);
  }

  public static int toInteger(final byte[] b) {
    return new BigInteger(BaseEncoding.base16().encode(b), 16).intValue();
  }

  public static boolean isEqual(final byte[] b1, final byte[] b2) {
    return ByteBuffer.wrap(b1).equals(ByteBuffer.wrap(b2));
  }

  public static byte[] makeByteArray(final byte[] v, final int size) {
    return ByteBuffer.allocate(size).put(v).array();
  }

  public static byte[] getByteArray(final String hex) {
    try {
      return Hex.decodeHex(hex.toCharArray());
    } catch (DecoderException ex) {
    }
    return null;
  }

  public static byte[] pop(final ByteBuffer bbuff, final int pop) {
    byte[] dest = new byte[pop];
    bbuff.get(dest, 0, pop);
    return dest;
  }

  // U8 = 1 byte
  public static byte[] U8(final int v) {
    return ByteBuffer.allocate(1).put((byte) v).array();
  }

  // U16 = 2 bytes
  public static byte[] U16(final int v) {
    return ByteBuffer.allocate(2).putShort((short) v).array();
  }

  // U32 = 4 bytes
  public static byte[] U32(final int v) {
    return ByteBuffer.allocate(4).putInt(v).array();
  }

}
