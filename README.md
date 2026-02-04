# RandomTeleport

A wilderness random teleportation plugin for AllayMC servers. Players can use `/rtp` to teleport to a safe random location in the wilderness, keeping spawn areas uncluttered.

## Features

- **Safe Teleportation**: Finds safe locations with solid ground and adequate air space
- **Configurable Distance**: Set minimum and maximum teleportation radius from spawn
- **Cooldown System**: Prevent spam with configurable cooldown (default: 60 seconds)
- **Smart Location Finding**: Searches for safe ground, avoiding water, lava, and dangerous blocks
- **Admin Commands**: Reload config and clear player cooldowns
- **Lightweight**: Simple, efficient implementation with minimal overhead

## Commands

| Command | Permission | Description |
|---------|-----------|-------------|
| `/rtp` | `randomteleport.use` | Teleport to a random safe location in the wilderness |
| `/rtp reload` | `randomteleport.admin` | Reload the plugin configuration |
| `/rtp cooldown clear <player>` | `randomteleport.admin` | Clear a player's teleport cooldown |

**Aliases**: `randomteleport`, `wild`

## Permissions

| Permission | Default | Description |
|------------|---------|-------------|
| `randomteleport.use` | `true` | Allows using the /rtp command |
| `randomteleport.admin` | `op` | Allows using admin commands |

## Configuration

The plugin uses default configuration values which can be customized in the config file (future update):

```yaml
# Teleport Settings
minDistance: 1000        # Minimum distance from spawn (blocks)
maxDistance: 10000       # Maximum distance from spawn (blocks)
maxRetries: 10           # Maximum attempts to find safe location
cooldownSeconds: 60      # Cooldown between teleports (seconds)

# Safety Settings
requireSolidGround: true  # Require solid ground for teleport
minAirSpaceAbove: 2       # Minimum air blocks above player
avoidWater: false         # Avoid teleporting into water
avoidLava: true           # Avoid teleporting near lava
```

## How It Works

1. When a player uses `/rtp`, the plugin generates a random location within the configured distance range
2. It searches from top to bottom to find a safe Y position with:
   - Solid ground beneath
   - Adequate air space above (default: 2 blocks)
   - No dangerous blocks (fire, magma)
3. If no safe location is found after `maxRetries` attempts, the player is notified
4. On success, the player is teleported and a cooldown is applied

## Installation

1. Download the latest `RandomTeleport-*.jar` from [Releases](https://github.com/daoge_cmd/RandomTeleport/releases)
2. Place the JAR file in your AllayMC server's `plugins` folder
3. Restart the server

## Building from Source

**Prerequisites**:
- Java 21 or higher
- Gradle (included in repository)

**Build Command**:
```bash
./gradlew shadowJar
```

The compiled JAR will be in `build/libs/`.

## Requirements

- AllayMC Server 0.24.0 or higher
- Java 21

## License

This plugin is licensed under the LGPL-2.1 license.

## Credits

Developed by daoge_cmd for the AllayMC community.

## Future Enhancements

- [ ] Config file support for easy customization
- [ ] Biome filtering (teleport to specific biome types)
- [ ] Per-world configuration
- [ ] Economy integration (cost per teleport)
- [ ] First-join automatic teleport
- [ ] Group-based permissions with different distance limits
