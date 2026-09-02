# Disable Villager Trade

[![Build](https://github.com/dodoflix/DisableVillagerTrade/actions/workflows/ci.yml/badge.svg)](https://github.com/dodoflix/DisableVillagerTrade/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/dodoflix/DisableVillagerTrade/graph/badge.svg)](https://codecov.io/gh/dodoflix/DisableVillagerTrade)
[![GitHub Release](https://img.shields.io/github/v/release/dodoflix/DisableVillagerTrade?label=release)](https://github.com/dodoflix/DisableVillagerTrade/releases)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/disable-villager-trade?logo=modrinth&label=modrinth)](https://modrinth.com/plugin/disable-villager-trade)
[![License](https://img.shields.io/github/license/dodoflix/DisableVillagerTrade)](LICENSE)

Disable villager trade on your Minecraft server! Now supports **multiple platforms**.

## 🎮 Supported Platforms

| Platform | Minecraft Version | Status |
|----------|-------------------|--------|
| **Bukkit/Spigot/Paper** | 1.14 - 26.2+ | ✅ Full Support |
| **Fabric** | 26.2 | ✅ Full Support |
| **Forge** | 26.2 | ✅ Logic Updated (Requires ForgeGradle Update) |
| **NeoForge** | 26.2 | ✅ Full Support |
| **Quilt** | 26.2 | ✅ Use Fabric version |

> **Note:** Quilt is compatible with Fabric mods. Simply use the Fabric version on Quilt servers/clients.

## ✨ Features

- 🚫 Prevent players from trading with villagers
- 🦙 Prevent players from trading with wandering traders (configurable)
- 💬 Configurable message when trading is blocked
- 🔓 Bypass permission for staff members or server operators (configurable)
- 🗣️ Villagers and wandering traders play the "No" sound and shake their heads when blocked
- 🌍 Per-world/dimension exclusion configuration
- 👨‍🌾 Villagers with no profession (unemployed) can still be interacted with
- 🔔 Automatic update checker with notifications
- ⚙️ Admin commands for management (all platforms)

## 📦 Installation

### Bukkit/Spigot/Paper
1. Download `DisableVillagerTrade-Bukkit-x.x.x.jar`
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. Configure in `plugins/DisableVillagerTrade/config.yml`

### Fabric
1. Download `DisableVillagerTrade-Fabric-x.x.x.jar`
2. Ensure you have [Fabric Loader](https://fabricmc.net/) and [Fabric API](https://modrinth.com/mod/fabric-api) installed
3. Place the JAR file in your `.minecraft/mods` folder (or `mods` for servers)
4. Start the game/server
5. Configure in `config/disablevillagertrade.json`

### Forge
1. Download `DisableVillagerTrade-Forge-x.x.x.jar`
2. Ensure you have [Forge](https://files.minecraftforge.net/) installed
3. Place the JAR file in your `.minecraft/mods` folder (or `mods` for servers)
4. Start the game/server
5. Configure in `config/disablevillagertrade-server.toml`

### NeoForge
1. Download `DisableVillagerTrade-NeoForge-x.x.x.jar`
2. Ensure you have [NeoForge](https://neoforged.net/) installed
3. Place the JAR file in your `.minecraft/mods` folder (or `mods` for servers)
4. Start the game/server
5. Configure in `config/disablevillagertrade-server.toml`

## ⚙️ Configuration

### Bukkit (config.yml)
```yaml
# Message settings
message:
  enabled: false
  text: "&cYou can't trade with villagers on this server."

# Worlds where villager trading is ALLOWED (not blocked)
disabled-worlds: []

# Update checker settings
update-checker:
  enabled: false
  check-interval: 24
  notify-on-join: false
  message: "&e[DisableVillagerTrade] &fA new version is available! &7(%current% -> %latest%)"

# Miscellaneous
enable-for-op: true
shake-head-enabled: true
enable-wandering-trader-trades: true
```

### Fabric (disablevillagertrade.json)
```json
{
  "messageEnabled": false,
  "message": "§cYou can't trade with villagers on this server.",
  "disabledDimensions": [],
  "updateCheckerEnabled": false,
  "updateCheckInterval": 24,
  "notifyOnJoin": false,
  "updateMessage": "§e[DisableVillagerTrade] §fA new version is available! §7(%current% → %latest%)",
  "enableForOp": true,
  "shakeHeadEnabled": true,
  "enableWanderingTraderTrades": true
}
```

### Forge/NeoForge (disablevillagertrade-server.toml)
```toml
[message]
enabled = false
text = "§cYou can't trade with villagers on this server."

[dimensions]
disabled_dimensions = []

[update_checker]
enabled = false
check_interval = 24
notify_on_join = false
message = "§e[DisableVillagerTrade] §fA new version is available! §7(%current% → %latest%)"

# Miscellaneous
enable_for_op = true
shake_head_enabled = true
enable_wandering_trader_trades = true
```

## 🔧 Commands

All platforms support the `/dvt` command. Bukkit additionally registers `/disabletrade` and `/tradetoggle` as aliases.

### Bukkit

| Command | Description | Permission |
|---------|-------------|------------|
| `/disabletrade reload` | Reload the plugin configuration | `disabletrade.admin` |
| `/disabletrade status` | Show plugin status and settings | `disabletrade.admin` |
| `/disabletrade toggle [player]` | Check bypass permission status | `disabletrade.admin` |
| `/disabletrade help` | Show help message | `disabletrade.admin` |

**Aliases:** `/dvt`, `/tradetoggle`

### Fabric / Forge / NeoForge

| Command | Description | Required Permission |
|---------|-------------|---------------------|
| `/dvt reload` | Reload the mod configuration | OP level 2 (`COMMANDS_GAMEMASTER`) |
| `/dvt status` | Show mod status and settings | OP level 2 (`COMMANDS_GAMEMASTER`) |
| `/dvt help` | Show help message | OP level 2 (`COMMANDS_GAMEMASTER`) |

## 🔐 Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `disabletrade.admin` | Access to all admin commands | OP |
| `disabletrade.bypass` | Allows the player to bypass trade block | OP |
| `disabletrade.update` | Receives update notifications on join | OP |

### Permission Support by Platform

| Platform | Permission System |
|----------|-------------------|
| Bukkit | Native + LuckPerms, etc. |
| Fabric | [Fabric Permissions API](https://github.com/lucko/fabric-permissions-api) (optional), falls back to OP level |
| Forge | OP level 2+ |
| NeoForge | OP level 2+ |

## 📥 Downloads

| Platform | Link |
|----------|------|
| GitHub Releases | [Download](https://github.com/dodoflix/DisableVillagerTrade/releases) |
| Modrinth | [Download](https://modrinth.com/plugin/disable-villager-trade) |

## 🏗️ Building from Source

This is a composite multi-module Gradle project. Each platform has its own Gradle wrapper.

```bash
# Clone the repository
git clone https://github.com/dodoflix/DisableVillagerTrade.git
cd DisableVillagerTrade

# Build all platforms
cd bukkit && ./gradlew shadowJar --no-daemon && cd ..
cd fabric && ./gradlew build --no-daemon && cd ..
cd forge && ./gradlew shadowJar --no-daemon && cd ..
cd neoforge && ./gradlew build --no-daemon && cd ..
```

Build outputs will be in:
- `bukkit/build/libs/`
- `fabric/build/libs/`
- `forge/build/libs/`
- `neoforge/build/libs/`

## 🤝 Contributing

Contributions are welcome! Please read [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines on:

- Commit message conventions
- Pull request process
- Development setup

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
