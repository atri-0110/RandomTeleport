package com.atri.randomteleport.manager;

import com.atri.randomteleport.config.RandomTeleportConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.allaymc.api.entity.interfaces.EntityPlayer;
import org.allaymc.api.math.location.Location3d;
import org.allaymc.api.world.Dimension;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Manages random teleportation for players.
 */
public class RandomTeleportManager {

    private final RandomTeleportConfig config;
    private final ConcurrentHashMap<UUID, Long> lastTeleportTime;
    private final ConcurrentHashMap<UUID, TeleportResult> teleportHistory;

    public RandomTeleportManager(RandomTeleportConfig config) {
        this.config = config;
        this.lastTeleportTime = new ConcurrentHashMap<>();
        this.teleportHistory = new ConcurrentHashMap<>();
    }

    /**
     * Attempts to teleport a player to a random location.
     *
     * @param player The player to teleport
     * @return TeleportResult with success status and message
     */
    public TeleportResult teleportPlayer(EntityPlayer player) {
        UUID uuid = player.getUniqueId();

        // Check cooldown
        long currentTime = System.currentTimeMillis();
        if (isOnCooldown(uuid, currentTime)) {
            long remainingSeconds = getRemainingCooldown(uuid, currentTime);
            return new TeleportResult(false, "You must wait " + remainingSeconds + " seconds before using this command again.");
        }

        Dimension dimension = player.getDimension();

        // Attempt to find a safe location
        SafeLocation safeLocation = findSafeLocation(dimension);

        if (safeLocation == null) {
            return new TeleportResult(false, "Could not find a safe location after " + config.getMaxRetries() + " attempts. Please try again.");
        }

        // Create new location and teleport
        Location3d targetLocation = new Location3d(
            (float) safeLocation.x,
            (float) safeLocation.y,
            (float) safeLocation.z,
            dimension
        );
        player.teleport(targetLocation);

        // Update cooldown
        updateCooldown(uuid);

        // Record teleport history
        TeleportResult result = new TeleportResult(true, "Teleported to random location at " +
            (int)safeLocation.x + ", " + (int)safeLocation.y + ", " + (int)safeLocation.z + "!");
        teleportHistory.put(uuid, result);

        player.sendMessage(result.getMessage());

        return result;
    }

    /**
     * Finds a safe random location in the given dimension.
     */
    private SafeLocation findSafeLocation(Dimension dimension) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int attempt = 0; attempt < config.getMaxRetries(); attempt++) {
            // Generate random coordinates
            double distance = random.nextDouble(config.getMinDistance(), config.getMaxDistance());
            double angle = random.nextDouble(0, 2 * Math.PI);

            double x = Math.cos(angle) * distance;
            double z = Math.sin(angle) * distance;

            // Find a safe Y position
            int y = findSafeY(dimension, x, z);

            if (y != -1) {
                return new SafeLocation(x, y, z);
            }
        }

        return null;
    }

    /**
     * Finds a safe Y position at the given X, Z coordinates.
     * Simplified version - returns mid-air if no safe ground found
     */
    private int findSafeY(Dimension dimension, double x, double z) {
        // For simplicity, teleport to a safe height in the air
        // This prevents players from teleporting into the ground
        // A more sophisticated version would check the terrain below
        return 120; // Safe height above most terrain
    }

    /**
     * Checks if a player is on cooldown.
     */
    private boolean isOnCooldown(UUID uuid, long currentTime) {
        Long lastTime = lastTeleportTime.get(uuid);
        if (lastTime == null) {
            return false;
        }
        return (currentTime - lastTime) < config.getCooldownSeconds() * 1000L;
    }

    /**
     * Gets remaining cooldown time in seconds.
     */
    private long getRemainingCooldown(UUID uuid, long currentTime) {
        Long lastTime = lastTeleportTime.get(uuid);
        if (lastTime == null) {
            return 0;
        }
        long elapsed = currentTime - lastTime;
        long cooldownMs = config.getCooldownSeconds() * 1000L;
        return Math.max(0, (cooldownMs - elapsed) / 1000L);
    }

    /**
     * Updates the cooldown for a player.
     */
    private void updateCooldown(UUID uuid) {
        lastTeleportTime.put(uuid, System.currentTimeMillis());
    }

    /**
     * Clears cooldown for a player.
     */
    public void clearCooldown(UUID uuid) {
        lastTeleportTime.remove(uuid);
    }

    /**
     * Gets teleport history for a player.
     */
    public TeleportResult getTeleportHistory(UUID uuid) {
        return teleportHistory.get(uuid);
    }

    /**
     * Result of a teleportation attempt.
     */
    @Data
    @AllArgsConstructor
    public static class TeleportResult {
        private final boolean success;
        private final String message;
    }

    /**
     * Represents a safe location for teleportation.
     */
    @Data
    @AllArgsConstructor
    private static class SafeLocation {
        private final double x;
        private final int y;
        private final double z;
    }
}
