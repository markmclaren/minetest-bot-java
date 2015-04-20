package com.markmc.minetest;

/**
 *
 * @author markmc
 */
public interface ServerCommand {

    byte[] getCommandBytes();
    int getChannel();
    byte[] getChannelBytes();
    Boolean isReliable();
    
}
