package com.markmc.minetest;

import com.markmc.minetest.bot.BotRunnable;

/**
 * SingleBotRunner.
 *
 * @author markmc
 */
public class SingleBotRunner {

    public static void main(final String[] args) {
        String serverIp = "127.0.0.1";

        BotRunnable botRunnable = new BotRunnable(serverIp, "bot-1");
        botRunnable.run();

        // Just keep looping
        while (true) {
        }

    }
}
