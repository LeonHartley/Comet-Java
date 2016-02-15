package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.google.common.collect.Lists;

import java.util.List;

public class CannonFloorItem extends RoomItemFloor {

    private List<PlayerEntity> entitiesToKick = Lists.newArrayList();

    public CannonFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (!isWiredTrigger) {
            double distance = entity.getPosition().distanceTo(this.getPosition());

            if (distance > 2) {
                entity.moveTo(this.getPosition().squareInFront(this.getRotation()));
                return false;
            }
        }

        Position kickTile = null;
        int rotationToFindTile = 6;

        switch (this.rotation) {
            case 2: {
                rotationToFindTile = 0;
                break;
            }

            case 4: {
                rotationToFindTile = 2;
                kickTile = this.getPosition().squareInFront(rotationToFindTile).squareInFront(rotationToFindTile);
                break;
            }

            case 6: {
                rotationToFindTile = 4;
                kickTile = this.getPosition().squareInFront(rotationToFindTile).squareInFront(rotationToFindTile);
                break;
            }

        }

        if(kickTile == null) {
            kickTile = this.getPosition().squareInFront(rotationToFindTile);
        }

        for (RoomEntity kickableEntity : this.getRoom().getMapping().getTile(kickTile).getEntities()) {
            if (kickableEntity instanceof PlayerEntity) {
                if (((PlayerEntity) kickableEntity).getPlayerId() != this.getRoom().getData().getOwnerId() && ((PlayerEntity) kickableEntity).getPlayer().getPermissions().getRank().roomKickable()) {
                    ((PlayerEntity) kickableEntity).getPlayer().getSession().send(new AdvancedAlertMessageComposer("Alert", Locale.getOrDefault("game.kicked", "You've been kicked!"), "room_kick_cannonball"));
                    this.entitiesToKick.add(((PlayerEntity) kickableEntity));
                }

                kickableEntity.applyEffect(new PlayerEffect(4, 5));
            }
        }

        this.setExtraData("1");
        this.sendUpdate();

        this.setTicks(RoomItemFactory.getProcessTime(1.5));
        return true;
    }

    @Override
    public void onTickComplete() {
        for(PlayerEntity entity : this.entitiesToKick) {
            entity.kick();
        }

        this.entitiesToKick.clear();

        this.setExtraData("0");
        this.sendUpdate();
    }
}
