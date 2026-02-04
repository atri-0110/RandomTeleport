package com.atri.randomteleport.config;

import java.util.Arrays;
import java.util.List;

/**
 * Configuration for RandomTeleport plugin.
 */
public class RandomTeleportConfig {

    // Teleport settings
    private final int minDistance;
    private final int maxDistance;
    private final int maxRetries;
    private final boolean teleportOnFirstJoin;
    private final int cooldownSeconds;

    // Safe teleport settings
    private final boolean requireSolidGround;
    private final int minAirSpaceAbove;
    private final boolean avoidWater;
    private final boolean avoidLava;

    // Biome preferences
    private final List<String> preferredBiomes;
    private final boolean useBiomeFilter;

    public RandomTeleportConfig() {
        // Default configuration values
        this.minDistance = 1000;
        this.maxDistance = 10000;
        this.maxRetries = 10;
        this.teleportOnFirstJoin = false;
        this.cooldownSeconds = 60;

        this.requireSolidGround = true;
        this.minAirSpaceAbove = 2;
        this.avoidWater = false;
        this.avoidLava = true;

        this.useBiomeFilter = false;
        this.preferredBiomes = Arrays.asList(
            "plains",
            "forest",
            "taiga",
            "savanna"
        );
    }

    public int getMinDistance() {
        return minDistance;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public boolean isTeleportOnFirstJoin() {
        return teleportOnFirstJoin;
    }

    public int getCooldownSeconds() {
        return cooldownSeconds;
    }

    public boolean isRequireSolidGround() {
        return requireSolidGround;
    }

    public int getMinAirSpaceAbove() {
        return minAirSpaceAbove;
    }

    public boolean isAvoidWater() {
        return avoidWater;
    }

    public boolean isAvoidLava() {
        return avoidLava;
    }

    public boolean isUseBiomeFilter() {
        return useBiomeFilter;
    }

    public List<String> getPreferredBiomes() {
        return preferredBiomes;
    }
}
