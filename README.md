# BuildLabeler Plugin [1.20.1]

The BuildLabeler plugin is a custom Minecraft plugin developed for personal use, enabling players to engage in building sessions, submit their builds, and manage building areas with WorldEdit support. This plugin is designed for single-user usage and includes multilingual features to provide a localized experience for players.

## Features

- Building Sessions: Players can enter build mode to start a building session, during which they can create and modify their builds in designated areas.

- Theme Labeling: By default, players receive a random theme for their building session. However, they can use the `/label` command to set a specific theme for their build if they wish.

- Multilingual Support: The plugin offers multilingual functionality, allowing players to choose between English and Portuguese languages for messages and labels.

- WorldEdit Integration: Players can utilize WorldEdit commands within the building areas to enhance their building experience and efficiency.

- Clean and Submit Builds: After completing their builds, players can use the `/clean` command to reset the building area and remove any leftover structures. They can then submit their builds using the `/submit` command.

## Commands

- `/build`: Enter build mode and start a building session.

- `/label <label>`: Set a specific theme label for the current building session.

- `/randomlabel`: Receive a new random theme label for the current building session.

- `/end`: End the current building session.

- `/clean`: Clean the building area after submitting a build.

- `/submit`: Submit the completed build.

## Installation

1. Download the BuildLabeler plugin JAR file.
2. Place the JAR file in the `plugins` folder of your Minecraft server.
3. Restart or reload the server to enable the plugin.

## Configuration

The plugin creates lobbies from WorldGuard regions. To create a lobby, create a WorldGuard region with the name `lobby_<unique number>_building` with the desired building area. Set its parent region to `lobby_<unique number>`. Note that for it to work as intended, leave the building flag of the parent region as `deny` (or make it inherent from the __global__ region).
If you want WorldEdit support, install `WorldGuardExtraFlags` and set the `worldedit` flag of the building region to `allow`, also properly give the users worldedit permissions. 
The plugin uses a YAML file named `player_languages.yml` to store language preferences for individual players. This file should be placed in the `plugins/buildlabeler/` directory. The plugin automatically creates this file if it does not exist.

## Usage

1. Join the Minecraft server and log in as the player who wants to participate in building sessions.

2. Use the `/build` command to enter build mode and start a building session.

3. If you want a specific theme for your build, use the `/label <label>` command to set it. Otherwise, you can receive a random theme using `/randomlabel`.

4. Use WorldEdit commands to build and modify structures within the designated building area.

5. Once you are satisfied with your build, use the `/submit` command to submit it.

6. If you wish to make changes or start a new session, use the `/end` command to end the current session and then start a new session with `/build`.

7. After submitting your build, you can use the `/clean` command to clean the building area and remove any leftover structures.

## Development Notes

The BuildLabeler plugin is a custom plugin developed for personal usage and is not intended for public distribution. As such, it does not provide broad configuration options or support and no updates are planned for the plugin. 

## Support and Feedback

As this plugin is solely for personal usage, no formal support or updates are provided. However, you are welcome to explore, modify, and adapt the plugin to meet your specific needs.

For any feedback or questions, you can contact the plugin creator at [gwerneckpaiva@gmail.com].