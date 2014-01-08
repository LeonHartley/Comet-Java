package com.cometsrv.game.rooms.types.components;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.items.interactions.InteractionAction;
import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.avatars.pathfinding.Square;
import com.cometsrv.game.rooms.entities.GenericEntity;
import com.cometsrv.game.rooms.entities.RoomEntityType;
import com.cometsrv.game.rooms.entities.types.BotEntity;
import com.cometsrv.game.rooms.entities.types.PetEntity;
import com.cometsrv.game.rooms.entities.types.PlayerEntity;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;
import com.cometsrv.tasks.CometTask;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ProcessComponent implements CometTask {
    private final int ROOM_DISPOSE_TIME = Integer.parseInt(Comet.getServer().getConfig().get("comet.room.dispose.cycles"));
    private int disposeCycles = 0;

    private FastList<GenericEntity> usersToUpdate;

    private Room room;

    private Logger log;
    private ScheduledFuture myFuture;
    private boolean active = false;

    public ProcessComponent(Room room) {
        this.room = room;
        this.log = Logger.getLogger("Room Process [" + room.getData().getName() + "]");
    }

    public void start() {
        if(this.active) {
            stop();
        }

        this.active = true;
        this.myFuture = Comet.getServer().getThreadManagement().executePeriodic(this, 500, 500, TimeUnit.MILLISECONDS);

        log.debug("Processing started");
    }

    public void stop() {
        if(this.myFuture != null) {
            this.active = false;
            this.myFuture.cancel(false);

            log.debug("Processing stopped");
        }
    }

    @Override
    public void run() {
        if (!this.active) {
            return;
        }

        // Dispose the room if it has been idle for a certain amount of time
        if (this.getRoom().getEntities().count() == 0) {
            if (this.disposeCycles >= ROOM_DISPOSE_TIME) {
                this.getRoom().dispose();
                return;
            }

            this.disposeCycles++;
        } else {
            if (this.disposeCycles >= 0) {
                this.disposeCycles = 0;
            }
        }

        long timeStart = System.currentTimeMillis();

        // Reset the users to update
        if (this.usersToUpdate == null) {
            this.usersToUpdate = new FastList<>();
        } else if (this.usersToUpdate.size() > 0) {
            this.usersToUpdate.clear();
        }

        Map<Integer, GenericEntity> entities = this.room.getEntities().getEntitiesCollection();

        for (GenericEntity entity : entities.values()) {
            // Process each entity as its own
            if (entity.getEntityType() == RoomEntityType.PLAYER) {
                processPlayerEntity((PlayerEntity) entity);
            } else if (entity.getEntityType() == RoomEntityType.BOT) {
                processBotEntity((BotEntity) entity);
            } else if (entity.getEntityType() == RoomEntityType.PET) {
                processPetEntity((PetEntity) entity);
            }

            // Process anything generic for all entities below this line

            // Handle signs
            if (entity.hasStatus("sign") && !entity.isDisplayingSign()) {
                entity.removeStatus("sign");
                entity.markNeedsUpdate();
            }

            if (entity.needsUpdate()) {
                entity.markNeedsUpdateComplete();

                this.getRoom().getEntities().broadcastMessage(AvatarUpdateMessageComposer.compose(entity));
            }
        }
    }

    protected void processPlayerEntity(PlayerEntity entity) {
        // Cleanup if the entity is offline
        if (entity.getPlayer() == null || entity.getRoom() == null) {
            this.room.getEntities().removeEntity(entity);
            this.getRoom().getEntities().broadcastMessage(AvatarUpdateMessageComposer.compose(entity));
        }

        if (entity.getPlayer().floodTime >= 0.5) {
            entity.getPlayer().floodTime -= 0.5;

            if (entity.getPlayer().floodTime < 0) {
                entity.getPlayer().floodTime = 0;
            }
        }

        if (entity.hasStatus("mv")) {
            entity.removeStatus("mv");
            entity.markNeedsUpdate();
        }

        // Check if we are wanting to walk to a location
        if (entity.getWalkingPath() != null) {
            entity.setProcessingPath(new ArrayList<Square>(entity.getWalkingPath()));

            // Clear the walking path now we have a goal set
            entity.getWalkingPath().clear();
            entity.setWalkingPath(null);
        }

        // We should store players which need removing from the room here and process it last
        List<PlayerEntity> leavingRoom = new ArrayList<>();

        // Do we have a position to set from previous moves?
        if (entity.getPositionToSet() != null) {
            if ((entity.getPositionToSet().getX() == this.room.getModel().getDoorX()) && (entity.getPositionToSet().getY() == this.room.getModel().getDoorY())) {
                leavingRoom.add(entity);
                return;
            }

            // Copy the current position before updating
            Position3D currentPosition = new Position3D(entity.getPosition());

            // Create the new position
            Position3D newPosition = new Position3D();
            newPosition.setX(entity.getPositionToSet().getX());
            newPosition.setY(entity.getPositionToSet().getY());
            newPosition.setZ(entity.getPositionToSet().getZ());

            entity.setPosition(newPosition);

            // We can also handle walk to + interact here in the future!
        }

        if (entity.isWalking()) {
            Square nextSq = entity.getProcessingPath().get(0);
            entity.getProcessingPath().remove(nextSq);

            boolean isLastStep = (entity.getProcessingPath().size() == 0);

            if (nextSq != null && entity.getPathfinder().checkSquare(nextSq.x, nextSq.y) && entity.getPathfinder().canWalk(nextSq.x, nextSq.y)) {
                Position3D currentPos = entity.getPosition() != null ? entity.getPosition() : new Position3D(0, 0, 0);
                entity.setBodyRotation(Position3D.calculateRotation(currentPos.getX(), currentPos.getY(), nextSq.x, nextSq.y, false));
                entity.setHeadRotation(entity.getBodyRotation());

                double height = this.getRoom().getModel().getSquareHeight()[nextSq.x][nextSq.y];

                for(FloorItem item : this.getRoom().getItems().getItemsOnSquare(nextSq.x, nextSq.y)) {
                    item.setNeedsUpdate(true, InteractionAction.ON_WALK, entity, 0);
                    height += item.getHeight();
                }

                entity.addStatus("mv", String.valueOf(nextSq.x).concat(",").concat(String.valueOf(nextSq.y)).concat(",").concat(String.valueOf(height)));

                if (entity.hasStatus("sit")) {
                    entity.removeStatus("sit");
                }

                if (entity.hasStatus("lay")) {
                    entity.removeStatus("lay");
                }

                entity.updateAndSetPosition(new Position3D(nextSq.x, nextSq.y, 0));
                entity.markNeedsUpdate();
            }
        }

        // Remove entities from room
        for (PlayerEntity entity0 : leavingRoom) {
            entity0.leaveRoom(false, false, true);
        }
    }

    protected void processBotEntity(BotEntity entity) {

    }

    protected void processPetEntity(PetEntity entity) {

    }

    private void broadcastEntityUpdate() {

    }

    private void removeFromRoom(GenericEntity entity) {
        this.room.getEntities().removeEntity(entity);
        this.getRoom().getEntities().broadcastMessage(AvatarUpdateMessageComposer.compose(entity));
    }

    //FastMap<Integer, Avatar> avatars = this.getRoom().getAvatars().getAvatars();

        /*for (Avatar avatar : avatars.values()) {
            if (checkLostAvatar(avatar)) { continue; }

            if (avatar.getPlayer().floodTime >= 0.5) {
                avatar.getPlayer().floodTime -= 0.5;

                if (avatar.getPlayer().floodTime < 0) {
                    avatar.getPlayer().floodTime = 0;
                }
            }

            // Handle the sign status
            if(avatar.hasStatus("sign")) {
                if(avatar.signTime != 0) {
                    avatar.signTime--;
                } else {
                    avatar.removeStatus("sign");
                    avatar.needsUpdate = true;
                }
            }

            // If the user is animating the walk status remove it (it will be reset below if the user is walking)
            if (avatar.hasStatus("mv")) {
                avatar.removeStatus("mv");
                avatar.setNeedsUpdate(true);
            }

            // Do we need to set a position?
            if (avatar.getPositionToSet() != null) {
                int posX = avatar.getPositionToSet().getX();
                int posY = avatar.getPositionToSet().getY();

                if (posX == this.getRoom().getModel().getDoorX() && posY == this.getRoom().getModel().getDoorY()) {
                    this.removeFromRoom(avatar);
                    continue;
                }


            }
        }*/

    /*protected void removeFromRoom(Avatar avatar) {
        this.getRoom().getAvatars().remove(avatar);
        this.getRoom().getAvatars().broadcast(AvatarUpdateMessageComposer.compose(avatar));
    }

    private boolean checkLostAvatar(Avatar avatar) {
        if(avatar.getPlayer() == null) {
            this.getRoom().getAvatars().remove(avatar);
            return true;
        }

        if(!avatar.inRoom || avatar.getRoom() == null) {
            this.getRoom().getAvatars().remove(avatar);
            return true;
        }

        return false;
    }*/

    /*@Override
    public void run() {
        try {
            if(!this.active) {
                return;
            }

            if(this.getRoom().getAvatars().getAvatars().size() == 0) {
                this.getRoom().dispose();
            }

            long timeStart = System.currentTimeMillis();

            FastList<Avatar> usersToUpdate = new FastList<>();

            synchronized (this.getRoom().getAvatars().getAvatars().values()) {
                for(Avatar avatar : this.getRoom().getAvatars().getAvatars().values()) {
                    if(avatar.getPlayer() == null) {
                        this.getRoom().getAvatars().remove(avatar);
                        continue;
                    }

                    if(!avatar.inRoom || avatar.getRoom() == null) {
                        this.getRoom().getAvatars().remove(avatar);
                    }

                    if(avatar.getPlayer().floodTime >= 0.5) {
                        avatar.getPlayer().floodTime -= 0.5;
                    }

                    if(avatar.isWarping) {
                        avatar.getPosition().setX(avatar.getGoalX());
                        avatar.getPosition().setY(avatar.getGoalY());
                        avatar.needsUpdate = true;
                    }

                    if(avatar.isMoving && avatar.getPath().size() > 0) {
                        handleWalk(avatar);
                    } else if(avatar.isMoving) {
                        if(avatar.hasStatus("mv")) {
                            avatar.removeStatus("mv");
                        }

                        avatar.needsUpdate = true;
                        avatar.isMoving = false;
                    }

                    if(avatar.hasStatus("sign")) {
                        if(avatar.signTime != 0) {
                            avatar.signTime--;
                        } else {
                            avatar.removeStatus("sign");
                            avatar.needsUpdate = true;
                        }
                    }

                    avatar.idleTime++;

                    if(!avatar.isIdle) {
                        if(avatar.idleTime >= 600) {
                            avatar.isIdle = true;

                            this.getRoom().getAvatars().broadcast(IdleStatusMessageComposer.compose(avatar.getPlayer().getId(), true));
                        }
                    }

                    if(avatar.idleTime >= 2400) {
                        avatar.dispose(false, true, true);
                        continue;
                    }

                    if(avatar.needsUpdate) {
                        if(avatar.hasStatus("mv") && !avatar.isMoving) {
                            avatar.removeStatus("mv");
                        }

                        for(FloorItem item : avatar.getRoom().getItems().getItemsOnSquare(avatar.getPosition().getX(), avatar.getPosition().getY())) {
                            if(item.getDefinition().canSit) {
                                avatar.isSitting = true;
                                avatar.setBodyRotation(item.getRotation());
                                avatar.setHeadRotation(item.getRotation());
                                avatar.getStatuses().put("sit", Double.toString(1.0));
                            }
                        }

                        usersToUpdate.add(avatar);
                        avatar.needsUpdate = false;
                    }
                }
            }

            if(usersToUpdate.size() != 0) {
                this.getRoom().getAvatars().broadcast(AvatarUpdateMessageComposer.compose(usersToUpdate));
                usersToUpdate.clear();
            }

            TimeSpan span = new TimeSpan(timeStart, System.currentTimeMillis());

            if(span.toMilliseconds() > 100) {
                log.debug("Process took: " + span.toMilliseconds() + "ms to execute.");
            }

        } catch(Exception e) {
            log.error("Error while processing room", e);
            this.getRoom().dispose();
        }
    }

    private void handleWalk(Avatar avatar) {
        Square next = (avatar.getIsTeleporting()) ? new Square(avatar.getGoalX(), avatar.getGoalY()) : avatar.getPath().poll();

        if(avatar.getIsTeleporting()) {
            avatar.getPath().clear();
        }

        if(next.x == room.getModel().getDoorX() && next.y == room.getModel().getDoorY()) {
            avatar.dispose(false, false, true);
            this.getRoom().getAvatars().broadcast(AvatarUpdateMessageComposer.compose(avatar));
            return;
        }

        double height = avatar.getRoom().getModel().getSquareHeight()[avatar.getPosition().getX()][avatar.getPosition().getY()];

        for(FloorItem item : avatar.getRoom().getItems().getItemsOnSquare(avatar.getPosition().getX(), avatar.getPosition().getY())) {
            item.setNeedsUpdate(true, InteractionAction.ON_WALK, avatar, 0);
            height += item.getHeight();
        }

        if(avatar.hasStatus("mv")) {
            avatar.removeStatus("mv");
        }

        if(avatar.hasStatus("sit")) {
            avatar.removeStatus("sit");
        }

        if(avatar.hasStatus("lay")) {
            avatar.removeStatus("lay");
        }

        for(FloorItem item : avatar.getRoom().getItems().getItemsOnSquare(next.x, next.y)) {
            if(item.getDefinition().getInteraction().equals("gate") && item.getExtraData().equals("0")) {
                avatar.getPath().clear();

                avatar.needsUpdate = true;
                avatar.isMoving = false;
                return;
            }
        }

        int rotation = Position.calculateRotation(avatar.getPosition().getX(), avatar.getPosition().getY(), next.x, next.y, avatar.isMoonwalking);
        avatar.setBodyRotation(rotation);
        avatar.setHeadRotation(rotation);

        avatar.getStatuses().put("mv", String.valueOf(next.x).concat(",").concat(String.valueOf(next.y)).concat(",").concat(String.valueOf(height)));

        this.getRoom().getAvatars().broadcast(AvatarUpdateMessageComposer.compose(avatar));

        avatar.getPosition().setX(next.x);
        avatar.getPosition().setY(next.y);

        for(FloorItem item : avatar.getRoom().getItems().getItemsOnSquare(avatar.getPosition().getX(), avatar.getPosition().getY())) {
            item.setNeedsUpdate(true, InteractionAction.ON_WALK, avatar, 1);
            height += Position.calculateHeight(item);
        }

        avatar.getPosition().setZ(height);

    }*/

    public void dispose() {
        this.active = false;
        this.myFuture.cancel(false);
    }

    public boolean isActive() {
        return this.active;
    }

    public Room getRoom() { return this.room; }
}
