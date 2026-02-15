<p align="center"><img src="https://i.ibb.co/cc0CjQmw/568fe15e4c25bbdea989b29dccc3d215b8970f4e9b6d096819248b44f372ec15a8ecd8faa7643123da39a3ee5e6b4b0d3255.png" /></p>

A plugin for Velocity that reconnects players to the last server they were playing on

## Features

- Automatic reconnection to the last server on player login
- Configurable messages for reconnect success and server unavailable
- Multiple storage backends: YAML, SQLite, MySQL, MariaDB, PostgreSQL, LuckPerms
- Optional per-server permission (`velocity.reconnect.<servername>`)
- Server blacklist to prevent reconnection to specific servers
- Optional LiteBans integration to prevent reconnecting banned players
- Optional fallback prevention (block redirect to fallback server when kicked)
- **reload** command to reload configuration without restarting the proxy

## Commands & Permissions

| Command             | Description                     |
| ------------------- | ------------------------------- |
| /vreconnect reload  | Reloads the plugin configuration |

- **reload**: requires permission **`velocity.reconnect.reload`** (default: op)
  - Works in-game and from the proxy console

## Requirements

- Java 21 (LTS)
- Velocity proxy (tested with Velocity 3.x)

## Build

From the project root, run:

```bash
./gradlew build
```

The plugin JAR will be generated at:

`build/libs/SimpleReconnect-1.0.jar`

## Installation

1. Copy the built JAR to your Velocity server `plugins` folder
2. Start or restart the proxy
3. The `config.yml` file will be created automatically in `plugins/reconnect/` if it does not exist

## Configuration

Example configuration (defaults):

```yml
# For debugging purposes, recommended to disable
debug: false

# Should we check for the latest version?
checkUpdates: true

# Send players a message when they are reconnected
messageOnReconnect: true
reconnectMessage:
  - "<gray>You were reconnected to <white>%server%</white>."

# Send a message when their previous server isn't available
notAvailable: true
notAvailableMessage:
  - "<gray>Unable to reconnect you to your last server."

# Require velocity.reconnect.<servername> permission per server
perServerPermission: false

# Servers that players cannot reconnect to
blacklist: []

# Prevent connection to fallback servers when kicked
preventFallback: false
preventFallbackMessage: []

# LiteBans hook - prevents banned players from reconnecting to banned servers
liteBansHook: false

storage:
  method: "yaml"
  data:
    address: "localhost:3306"
    database: "reconnect.db"
    username: "root"
    password: "1234"
```

- **debug**: Enable debug logging
- **checkUpdates**: Check for new versions on startup; admins with `velocity.reconnect.admin` get update notifications
- **messageOnReconnect** / **reconnectMessage**: Message shown when player is reconnected. Placeholders: `%server%`, `%player%`
- **notAvailable** / **notAvailableMessage**: Message when the last server is offline or unreachable
- **perServerPermission**: If `true`, player needs `velocity.reconnect.<servername>` to reconnect to each server
- **blacklist**: List of server names where reconnection is never allowed
- **preventFallback** / **preventFallbackMessage**: When kicked, prevent redirect to fallback server (if not on blacklist)
- **liteBansHook**: Integrate with LiteBans to block reconnection to servers where the player is banned
- **storage.method**: Storage backend. Options: `yaml`, `sqlite`, `mysql`, `mariadb`, `postgresql`, `luckperms`
- **storage.data**: Connection settings for database backends. For SQLite/YAML, `database` is the file path

## Notes

- When using a forced host, the plugin does not override the initial server choice
- Storage location for YAML: `plugins/reconnect/data.yml`
- For LuckPerms storage, the last server is stored as user metadata

## License

Distributed under the MIT License
