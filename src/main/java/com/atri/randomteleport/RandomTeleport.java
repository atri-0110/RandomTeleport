package com.atri.randomteleport;

import com.atri.randomteleport.command.RandomTeleportCommand;
import com.atri.randomteleport.config.RandomTeleportConfig;
import com.atri.randomteleport.listener.RandomTeleportListener;
import com.atri.randomteleport.manager.RandomTeleportManager;
import org.allaymc.api.plugin.Plugin;
import org.allaymc.api.registry.Registries;
import org.allaymc.api.server.Server;

public class RandomTeleport extends Plugin {

    private static RandomTeleport instance;
    private RandomTeleportManager teleportManager;
    private RandomTeleportConfig config;
    private RandomTeleportListener listener;

    @Override
    public void onLoad() {
        instance = this;
        this.pluginLogger.info("RandomTeleport is loading...");
    }

    @Override
    public void onEnable() {
        this.pluginLogger.info("RandomTeleport is enabling...");

        // Load config
        config = new RandomTeleportConfig();

        // Initialize teleport manager
        teleportManager = new RandomTeleportManager(config);

        // Register command
        Registries.COMMANDS.register(new RandomTeleportCommand(teleportManager));

        // Register event listener
        listener = new RandomTeleportListener(this);
        Server.getInstance().getEventBus().registerListener(listener);

        this.pluginLogger.info("RandomTeleport enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (listener != null) {
            Server.getInstance().getEventBus().unregisterListener(listener);
        }
        this.pluginLogger.info("RandomTeleport is disabled!");
    }

    public static RandomTeleport getInstance() {
        return instance;
    }

    public RandomTeleportManager getTeleportManager() {
        return teleportManager;
    }

    public RandomTeleportConfig getPluginConfig() {
        return config;
    }

    public void logInfo(String message) {
        this.pluginLogger.info(message);
    }
}
