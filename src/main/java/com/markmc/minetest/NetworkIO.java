package com.markmc.minetest;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * NetworkIO interface.
 * @author Mark McLaren (mark.mclaren@bristol.ac.uk)
 */
public interface NetworkIO {

  /**
   * requestData.
   * <p>
   * @return ByteBuffer
   * <p>
   * @throws IOException if something goes wrong
   */
  ByteBuffer requestData() throws IOException;

  /**
   * send.
   * <p>
   * @param msg byte[]
   */
  void send(final byte[] msg);

}

