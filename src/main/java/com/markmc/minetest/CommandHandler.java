package com.markmc.minetest;

import java.nio.ByteBuffer;

/**
 * CommandHandler.
 * <p>
 * @author markmc
 */
public interface CommandHandler {

  /**
   * getName.
   * <p>
   * @return name of the command that this CommandHandler processes
   */
  String getName();

  /**
   * run. Method that processes the data returned by this command.
   * <p>
   * @param data ByteBuffer
   */
  void run(ByteBuffer data);

}
