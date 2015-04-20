package com.markmc.minetest;

/**
 * ServerCommand.
 * <p>
 * @author markmc
 */
public interface ServerCommand {

  byte[] getCommandBytes();

  int getChannel();

  byte[] getChannelBytes();

  Boolean isReliable();

}
