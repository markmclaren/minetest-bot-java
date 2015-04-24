package com.markmc.minetest;

import com.markmc.minetest.bot.BotRunnable;

/**
 * MultipleBotRunner.
 *
 * @author markmc
 */
public class MultipleBotRunner {

    public static void main(final String[] args) {
        String serverIp = "127.0.0.1";

        // Create 10 bots
        Thread thread;
        for (int i = 1; i <= 10; i++) {
            thread = new Thread(new BotRunnable(serverIp, "bot-" + i));
            thread.start();
        }

        // Just keep looping
        while (true) {
        }
    }

}
