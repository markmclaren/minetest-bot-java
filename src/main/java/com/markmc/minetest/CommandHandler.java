package com.markmc.minetest;

import java.nio.ByteBuffer;

/**
 *
 * @author markmc
 */
public interface CommandHandler {
    
    String getName();
    void run(ByteBuffer data);
    
}
