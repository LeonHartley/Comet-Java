package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.events.types.TeleportItemEvent;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.tasks.CometThreadManager;

import java.util.concurrent.TimeUnit;


public class TeleporterFloorItem extends RoomItemFloor {
    private boolean inUse = false;
    private RoomEntity outgoingEntity;
    private RoomEntity incomingEntity;

    private int state = -1;
    private long pairId = -1;
    boolean isDoor = false;

    public TeleporterFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        this.setExtraData("0");

        if (this.getDefinition().getInteraction().equals("teleport_door")) {
            this.isDoor = true;
        }
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (isWiredTrigger) return false;

        Position posInFront = this.getPosition().squareInFront(this.getRotation());

        if (entity.isOverriden()) return false;

        if (entity.getPosition().getX() != posInFront.getX() || entity.getPosition().getY() != posInFront.getY()) {
            entity.moveTo(posInFront.getX(), posInFront.getY());

            RoomTile tile = this.getRoom().getMapping().getTile(posInFront.getX(), posInFront.getY());

            if (tile != null) {
                tile.scheduleEvent(entity.getId(), (e) -> onInteract(e, requestData, false));
            }
            return false;
        }

//
        this.inUse = true;
        this.outgoingEntity = entity;
        this.outgoingEntity.setOverriden(true);

        this.state = 0;
        this.setTicks(RoomItemFactory.getProcessTime(1));

//        CometThreadManager.getInstance().executeSchedule(new TeleportItemEvent(this, entity), 1, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (this.inUse) {
            return;
        }

        if (this.incomingEntity != null && this.incomingEntity.getId() == entity.getId()) {
            return;
        }

        this.inUse = true;
        this.outgoingEntity = entity;
        this.outgoingEntity.setOverriden(true);

        this.state = 1;
        this.setTicks(RoomItemFactory.getProcessTime(0.01));
    }

    @Override
    public void onTickComplete() {
        switch (this.state) {
            case 0: {

                this.outgoingEntity.moveTo(this.getPosition().getX(), this.getPosition().getY());

                if (!(this instanceof TeleportPadFloorItem)) {
                    this.toggleDoor(true);
                }

                this.state = 1;
                this.setTicks(RoomItemFactory.getProcessTime(1));
                break;
            }

            case 1: {
                RoomItemFloor pairItem = this.getPartner(this.getPairId());

                if (pairItem == null) {
                    int roomId = ItemManager.getInstance().roomIdByItemId(pairId);

                    if (RoomManager.getInstance().get(roomId) == null) {
                        this.state = 8;
                        this.setTicks(RoomItemFactory.getProcessTime(0.5));
                        return;
                    }
                }

                if (!this.isDoor && !(this instanceof TeleportPadFloorItem))
                    this.toggleDoor(false);

                this.state = 2;
                this.setTicks(RoomItemFactory.getProcessTime(this instanceof TeleportPadFloorItem ? 0.1 : 0.5));
                break;
            }

            case 2: {
                if (!this.isDoor) {
                    this.toggleAnimation(true);

                    this.state = 3;
                    this.setTicks(RoomItemFactory.getProcessTime(1));
                } else {
                    this.state = 3;
                    this.setTicks(RoomItemFactory.getProcessTime(0.1));
                }
                break;
            }

            case 3: {
                long pairId = this.getPairId();

                if (pairId == 0) {
                    this.state = 8;
                    this.setTicks(RoomItemFactory.getProcessTime(0.5));
                    return;
                }

                RoomItemFloor pairItem = this.getPartner(pairId);

                if (pairItem == null) {
                    int roomId = ItemManager.getInstance().roomIdByItemId(pairId);

                    if (RoomManager.getInstance().get(roomId) != null) {
                        if (this.outgoingEntity instanceof PlayerEntity) {
                            PlayerEntity pEntity = (PlayerEntity) this.outgoingEntity;

                            if (pEntity.getPlayer() != null && pEntity.getPlayer().getSession() != null) {
                                pEntity.getPlayer().setTeleportId(pairId);
                                pEntity.getPlayer().setTeleportRoomId(roomId);
                                pEntity.getPlayer().getSession().send(new RoomForwardMessageComposer(roomId));
                            }

                            this.state = 7;
                            this.setTicks(RoomItemFactory.getProcessTime(0.5));
                        }
                    } else {
                        this.state = 8;
                        this.setTicks(RoomItemFactory.getProcessTime(0.5));
                        return;
                    }
                }

                if (!this.isDoor) {
                    this.state = 9;
                    this.setTicks(RoomItemFactory.getProcessTime(1));
                }

                TeleporterFloorItem teleItem = (TeleporterFloorItem) pairItem;

                if (teleItem != null)
                    teleItem.handleIncomingEntity(this.outgoingEntity, this);
                break;
            }

            case 5: {
                this.toggleAnimation(false);
                this.state = 6;
                this.setTicks(RoomItemFactory.getProcessTime(0.5));
                break;
            }

            case 6: {
                if (!(this instanceof TeleportPadFloorItem))
                    this.toggleDoor(true);

                if (this.incomingEntity != null) {
                    this.incomingEntity.moveTo(this.getPosition().squareInFront(this.getRotation()).getX(), this.getPosition().squareInFront(this.getRotation()).getY());
                }

                this.state = 7;
                this.setTicks(RoomItemFactory.getProcessTime(1));
                break;
            }

            case 7: {
                if (!(this instanceof TeleportPadFloorItem))
                    this.toggleDoor(false);

                if (this.incomingEntity != null) {
                    this.incomingEntity.setOverriden(false);
                    this.incomingEntity = null;
                }

                this.inUse = false;
                this.state = -1;
                break;
            }

            case 8: {
                if (!(this instanceof TeleportPadFloorItem))
                    this.toggleDoor(true);

                if (this.outgoingEntity != null) {
                    this.outgoingEntity.moveTo(this.getPosition().squareBehind(this.rotation).getX(), this.getPosition().squareBehind(this.rotation).getY());
                }

                this.state = 7;
                this.setTicks(RoomItemFactory.getProcessTime(1));
                break;
            }

            case 9: {
                this.endTeleporting();
            }
        }
    }

    @Override
    public void onPlaced() {
        this.setExtraData("0");
    }

    private long getPairId() {
        if (this.pairId == -1) {
            this.pairId = ItemManager.getInstance().getTeleportPartner(this.getId());
        }

        return this.pairId;
    }

    public void endTeleporting() {
        this.toggleAnimation(false);

        this.state = -1;
        this.outgoingEntity = null;
        this.inUse = false;
    }

    public void handleIncomingEntity(RoomEntity entity, TeleporterFloorItem otherItem) {
        if (otherItem != null)
            otherItem.endTeleporting();

        entity.updateAndSetPosition(this.getPosition().copy());
        this.toggleAnimation(true);


        this.incomingEntity = entity;

        if (!this.isDoor) {
            this.state = 5;
            this.setTicks(RoomItemFactory.getProcessTime(1));
        } else {
            this.state = 6;
            this.setTicks(RoomItemFactory.getProcessTime(0.1));
        }
    }

    protected void toggleDoor(boolean state) {
        if (state)
            this.setExtraData("1");
        else
            this.setExtraData("0");

        this.sendUpdate();
    }

    protected void toggleAnimation(boolean state) {
        if (state) {
            this.setExtraData("2");
        } else {
            this.setExtraData("0");
        }

        this.sendUpdate();
    }

    protected RoomItemFloor getPartner(long pairId) {
        return this.getRoom().getItems().getFloorItem(pairId);
    }
}
