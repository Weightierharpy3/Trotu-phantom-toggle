# Phantom Toggle

A server-side Fabric mod for Minecraft 1.21.5 that allows players to toggle phantom spawning on or off for themselves only.

## Features

- Each player can individually toggle phantom spawning for themselves with a simple command
- Player preferences are saved in an H2 database file for persistence across server restarts
- Completely server-side - no need for clients to install anything

## Commands

- `/phantom toggle` - Toggles phantom spawning on/off for the player who runs the command
- `/phantom status` - Checks the current phantom spawning status for the player
- `/phantom version` - Shows the current version of the mod
- `/phantom creator` - Shows the creator of the mod
- `/phantom info` - Shows general information about the mod including usage instructions

## Installation

1. Make sure you have Fabric server installed for Minecraft 1.21.5
2. Download the latest release of this mod
3. Place the mod jar file in your server's `mods` folder
4. Restart your server

## Building from Source

This mod uses Gradle for building:

```bash
./gradlew build
```

The built jar file will be in the `build/libs` directory.

## Configuration

Player preferences are stored in `config/phantom-toggle/phantom-toggle-db.mv.db`, which is an H2 database file.

## Requirements

- Minecraft 1.21.5
- Fabric Loader 0.16.14+
- Fabric API 0.123.2+
- Java 21+

## License

This mod is available under the MIT License. See the LICENSE file for more information.