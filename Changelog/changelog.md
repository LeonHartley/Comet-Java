Comet Pre-1.0 Changelog
=======================

## 08/08/2015
* Fixed some small issues to gifts, they should be more secure now.

## 07/08/2015
* Made some changes to trade that should improve reliability.

## 05/08/2015
* Room queue & spectator mode

## 04/08/2015
* Group Forums
* Badge displays will now show the creation date & username.

## 31/07/2015
* Fixed room auth issue causing players who are teleporting to enter a locked room.

## 30/07/2015
* New rank permissions system
* Fixed team effects removing when player walks off a the item which triggered the join effect.
* Fixed wf_act_toggle_state not allowing you to set a delay.
* Fixed the "One furni or all furni" option in wf_cnd_has_furni_on

## 28/07/2015
* Added Highscore classic "all time" scoreboard.

## 24/07/2015
* Idle players are no longer kicked from their own rooms.
* allow_trade in player settings now works as intended.

## 20/07/2015
* Fixed issue with permissions related to room settings.
* Fixed issue with pathfinding in rooms with stairs and rugs.

## 19/07/2015
* Fixed issue with the HTTP API causing player data to not reload correctly.

## 18/07/2015
* Banzai games can now be ended by picking up the timer
* Wired items will now show the animation

## 15/07/2015
* Fixed bug: Sitting on a chair which is stacked on a "adjustable_height" item will have issues
* Fixed issue with badges not giving correct badge (Users would get the level+1 badge, not the correct level)

## 14/07/2015
* Achievements is now ready to go live (Not all achievements added but a big chunk.)
* Lay & Transform commands can now be disabled via :disablecommand

## 13/07/2015
* Lots of work on achievements

## 12/07/2015
* Fixed searching "owner:" causing an error in the navigator.
* Added room idle time to the server com.cometproject.networking.api.config (comet.room.idleTimeMinutes)

## 07/07/2015
* Lots of work on achievements
* Bug fix with catalog purchasing
* Commands are now logged

## 03/07/2015
* Fixed issue causing items not to be saved properly when used by wf_act_match_to_sshot
* Fixed issue with buying catalog items via search
* Fixed player ID not being saved with messenger chatlog

## 02/07/2015
* Fixed issues with football

## 30/06/2015
* Mimic AI additions (speech commands and more)

## 17/06/2015
* IP banning a player will now also disconnect any other player using the same IP address

## 15/06/2015
* Camera
* Player rewards are now processed individually, allowing for configurable time, faster reward processing and reliability.
* Fixed random disconnection bug caused by invalid cached user data
* Fixed bug with catalog causing disconnections
* More player data caching

## 12/06/2015
* Catalog bundles 

## 09/05/2015
* New player data caching system
* Moved some of the more time consuming tasks to other threads (Player login, room loading and catalog purchasing)

## 07/05/2015
* Fixed walking interruptions (See: http://gyazo.com/54edee79b2298c26d3c7d62d17f9fc48.mp4)
* Walk-on trigger is now only triggered if the item is on top.

## 31/05/2015
* Lots of work on achievements
* Improvements with unseen inventory items (Bots, pets and badges now use this system too)
* Fixed bug with limited edition items not working properly on first purchase.

## 23/05/2015
* Minor optimizations to group membership list
* Fix modifying the settings of a room you're not currently in
* Ability to make certain ranks (without mod tool) un-ignorable.

## 22/05/2015
* Optimizations with inventory and room item loading
* Optimizations with friends list.

## 21/05/2015
* Fix teleport walking override glitch when entering another room before the player is teleported.

## 20/05/2015
* Messenger invites are now filtered
* Inventory limits

## 19/05/2015
* Support for fork-join event execution.
* HikariCP database connection pooling (Configurable BoneCP or HikariCP.. for testing)
* Fixed wired timer
* Fixed kick/ban from rooms

## 18/05/2015
* Fixed bug with following/summon which would not allow you enter the room (Password/Doorbell)
* Fixed dice rigging (stacking dice inside each other )
* Quests
* Messenger chats can now be logged.
* You can now redeem diamond furniture.

## 12/05/2015
* Re-enabled football (new system)
* Roller fixes
* Standing on the same tile as another player is now fixed.

## 11/05/2015
* :empty bots command will now update the inventory in-game.

## 10/05/2015
* Optimizations and stability improvements
* Lots of work on manager (Over the past week).
* String filter updates

## 05/05/2015
* Trax bug fixes
* Bug with sending a limited edition item as a gift causing a client crash fixed.
* Commands are no longer case-sensitive.
* Badges given via :roombadge are now saved to the database (Oops!)
* Strict filter updates

## 30/04/2015
* Soundmachine/Trax complete

## 29/04/2015
* Lots more module work

## 28/04/2015
* Work on modules

## 27/04/2015
* Created a timer interaction which can be interacted with via wired.

## 26/04/2015
* More work on sound machine-related stuff
* Fix a bug where a session would not correctly be disposed if the user logs in from 2 locations.

## 25/04/2015
* No longer possible for rooms to have multiple groups.
* Start initial work on sound machine.

## 24/04/2015
* Async command support (for heavy commands such as massbadge, masscoins etc.)
* Ability to enable and disable several commands in your rooms (Example: Push, Pull)
* Fixed issue where sometimes where the player would appear online even when "hideOnline" is '1'.
* Fixed client-side cloning with following friend in same room.

## 23/04/2015
* Fix item movement animation with wf_act_chase

## 22/04/2015
* Enable TCP_NODELAY for client connections
* Configurable idle timer (Cleaning up idle connections)
* wf_cnd_match_snapshot now works as intended.
* wf_cnd_not_match_snap now works as intended.
* Update entity grid when bot is placed.
* Update entity grid when pet is placed.
* Remove the ability to modify an active Banzai timer using wf_act_match_to_sshot

## 18/04/2015
* Threading changes
* Changes to room actor processing
* Move the client-side friend list limit to com.cometproject.networking.api.config (comet.game.messenger.maxFriends)

## 17/04/2015
* Restructure project into modules
* Implement a Module API
* Asynchronous module event execution
* Fixed: When a player is respected, the count is not updated in the database.

## 16/04/2015
* wf_act_chase is now working as intended.
* New item pathfinder implementation
* Disabled football temporarily

## 15/04/2015
* Asynchronous incoming packet handling is now segregated from any other thread pools.

## 14/04/2015
* Limit bots per room (Configurable)

## 12/04/2015
* Groups: When membership is accepted, it will now instantly show in profiles, no need to reload client.
* Improve performance of the processing of room actors
* Re-introduce the idle timer, now if players haven't sent a request to the server in a while, they'll be disconnected.
* You can now give a hand item to all players in the room via the roomaction command (:roomaction handitem <item ID>)

## 11/04/2015
* Load friend requests only when needed (Takes some stress away from initial login process)
* Fix rare problem where room actors would not be visible in room
* Show badges on profiles whether the player is online or not
* Group administrators can now accept membership requests

## 10/04/2015
* Rollers: Alter the default speed
* Rollers: Process items before entities
* Allow support for separate path finding for room entities and items.
* Configurable timeout for updating player figure

## 09/04/2015
* Fixed room authentication issue where players could perform steps to enter a room, which is locked, without permission.
* Vastly improve item performance, allowing for smooth gameplay in even the most complex rooms.

## 08/04/2015
* Fixed disconnection with clicking searched items in catalog
* New method of handling incoming packets (It's configurable right now but should improve performance)
* Bots and pets becoming idle has been removed.

## 06/04/2015
* Rollers will now only roll entities if the player is only standing on the roller, it will not roll them if they're standing on a piece of furniture.
* Rollers will only roll items if they can stack.

## 05/04/2015
* Fixed a problem where you would not be able to sit on an item if it's stacked on top of an item that you can't sit on.

## 03/04/2015
* Work on group forums

## 31/03/2015
* Bot Mimic AI (If the bot owner says something, the bot will echo)
* Spawn bots using roomaction
