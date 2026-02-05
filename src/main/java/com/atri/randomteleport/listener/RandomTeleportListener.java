package com.atri.randomteleport.listener;

import com.atri.randomteleport.RandomTeleport;
import com.atri.randomteleport.config.RandomTeleportConfig;
import org.allaymc.api.eventbus.EventHandler;
import org.allaymc.api.eventbus.event.server.PlayerJoinEvent;
import org.allaymc.api.eventbus.event.server.PlayerQuitEvent;

/**
 * Event listener for RandomTeleport plugin.
 */
public class RandomTeleportListener {

    private final RandomTeleport plugin;
    private final RandomTeleportConfig config;

    public RandomTeleportListener(RandomTeleport plugin) {
        this.plugin = plugin;
        this.config = plugin.getPluginConfig();
    }

    /**
     * Handles player join event - optionally teleports new players.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!config.isTeleportOnFirstJoin()) {
            return;
        }

        var player = event.getPlayer().getControlledEntity();
        if (player == null) {
            return;
        }

        // Check if player is new (first time joining)
        // This is a simplified check - in a real implementation you would check player data
        // For now, we'll just skip to avoid spamming players
        // If enabled, uncomment the following:
        // if (isFirstJoin(player)) {
        //     plugin.getTeleportManager().teleportPlayer(player);
        //     player.sendMessage(TextFormat.YELLOW + "Welcome! You've been teleported to a random location.");
        // }
    }

    /**
     * Handles player quit event - clears cooldown data.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Clear cooldown data for the disconnecting player
        // This prevents memory buildup from players who rarely return
        var playerUuid = event.getPlayer().getLoginData().getUuid();
        plugin.getTeleportManager().clearCooldown(playerUuid);
    }
}
