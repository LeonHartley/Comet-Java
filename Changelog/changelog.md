Comet Pre-1.0 Changelog
==================

## 11/04/2015
* Many improvements made

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

## 31/04/2015
* Bot Mimic AI (If the bot owner says something, the bot will echo)
* Spawn bots using roomaction