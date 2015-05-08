![](http://i.imgur.com/Pu05U2K.png)

Comet Server
---

Comet is a high performance game server written in Java. 

Comet Server will be connected to a main manager, responsible for all config for every instance, removing the need for manual modification of the configuration files. The manager is powered by Spring and MongoDB.

The manager will provide functionality such as:
* Centralized TODO list and feature request system
* Bug reporting + support tickets 
* Logging (2 tiers: hotel and technical. The hotel errors will be visible to the instance owner, these errors will be related to their database, bans, in-game trades, catalog purchases, voucher redeems, room deletion, staff logs and lots more. The technical tier will only be visible by a developer of Comet and these will be things like lag detections, memory management, errors within the emulator, caching statistics and much more.)
* Starting and stopping the comet server instances
* Changelog of each version, day-by-day.
* Statistics of every instance shown on 1 page (to the developed, to an instance owner, they can only see instances that they own or have permission to manage.)
* Perform actions on specific instances or all instances at the same time. All instances will always have a love connection to the manager.
* All config variables will also be modifiable via the manager.
