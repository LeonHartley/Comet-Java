package com.cometsrv.game.rooms.entities;

import com.cometsrv.game.rooms.entities.types.BotEntity;
import com.cometsrv.game.rooms.entities.types.PetEntity;
import com.cometsrv.game.rooms.entities.types.PlayerEntity;

/**
 * Could be removed or may come in handy just put this here as an example for Leon
 */
public class RoomEntityCasting {
    public static PlayerEntity castToPlayer(GenericEntity genericEntity) {
        // This provides validation that the Entity is actually a player (an easy way to tell)
        if (genericEntity.getEntityType() != RoomEntityType.PLAYER) {
            return null;
        }

        // Cast the generic entity to a player entity.
        return (PlayerEntity) genericEntity;
    }

    public static BotEntity castToBot(GenericEntity genericEntity) {
        // This provides validation that the Entity is actually a bot (an easy way to tell)
        if (genericEntity.getEntityType() != RoomEntityType.BOT) {
            return null;
        }

        // Cast the generic entity to a bot entity.
        return (BotEntity) genericEntity;
    }

    public static PetEntity castToPet(GenericEntity genericEntity) {
        // This provides validation that the Entity is actually a pet (an easy way to tell)
        if (genericEntity.getEntityType() != RoomEntityType.PET) {
            return null;
        }

        // Cast the generic entity to a pet entity.
        return (PetEntity) genericEntity;
    }
}
