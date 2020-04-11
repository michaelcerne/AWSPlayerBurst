package com.michaelcerne.awsplayerburst;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AwsPlayerBurstListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        AwsPlayerBurst.controller.triggerPlayerCheck();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        AwsPlayerBurst.controller.triggerPlayerCheck();
    }

}
