package com.markmc.minetest.connector;

/**
 * ServerCommand.
 * <p>
 * @author markmc
 */
public interface ServerCommand {

  /**
   * getCommandBytes.
   * <p>
   * @return command in byte[] form
   */
  byte[] getCommandBytes();

  /**
   * getChannel.
   * <p>
   * @return channel number
   */
  int getChannel();

  /**
   * getChannelBytes.
   * <p>
   * @return byte[] version of channel number
   */
  byte[] getChannelBytes();

  /**
   * isReliable.
   * <p>
   * @return if msg sent should be encased in a reliable packet
   */
  Boolean isReliable();

}
