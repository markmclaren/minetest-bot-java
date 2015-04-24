package com.markmc.minetest.acceptor;

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
   * process. Method that processes the data returned by this command.
   * <p>
   * @param data ByteBuffer
   */
  void process(ByteBuffer data);

}
