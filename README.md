# DuncAFKer

A Fabric mod which allows you to afk click with a keybind, but locks all player movement.

**[Requires Fabric API](https://modrinth.com/mod/fabric-api)**

## Usage

The default settings are Attack at an interval of 30 ticks (1.5 seconds).
You can change this in the config through [Mod Menu](https://modrinth.com/mod/modmenu).

Set the keybind in the vanilla keybinds menu, and press the key in game to activate.
All movement will be locked and the player will start to click at the specified interval.
Chat or any other menu may be opened during usage and it will continue to click at the specified interval.

This mod works for both singleplayer and multiplayer.
On multiplayer, lag spikes on the server or client may interrupt the consistent flow of ticks.
Usage of the [Carpet Mod](https://github.com/gnembon/fabric-carpet/releases) on the server is recommended for a multiplayer setting where perfect timing is needed.
