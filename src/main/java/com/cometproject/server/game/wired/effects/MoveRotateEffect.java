package com.cometproject.server.game.wired.effects;

import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.AvatarEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.wired.data.WiredDataFactory;
import com.cometproject.server.game.wired.data.WiredDataInstance;
import com.cometproject.server.game.wired.types.WiredEffect;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.network.messages.types.Event;

import java.util.List;
import java.util.Random;

public class MoveRotateEffect extends WiredEffect {
    @Override
    public void onActivate(List<PlayerEntity> entities, FloorItem item) {
        WiredDataInstance data = WiredDataFactory.get(item);

        if(data.getItems().size() == 0) {
            return;
        }

        Room room = entities.get(0).getRoom();

        for(int itemId : data.getItems()) {
            boolean isCancelled = false;

            FloorItem itemInstance = room.getItems().getFloorItem(itemId);

            if(itemInstance == null)
                continue;

            Position3D previousPosition = new Position3D(itemInstance.getX(), itemInstance.getY(), itemInstance.getHeight());
            Position3D newPosition = handleMovement(new Position3D(itemInstance.getX(), itemInstance.getY(), itemInstance.getHeight()), data.getMovement());
            int rotation = handleRotation(itemInstance.getRotation(), data.getRotation());

            float height = 0;

            boolean isSameTile = true;
            if(previousPosition.getX() != newPosition.getX() || previousPosition.getY() != newPosition.getY()) {
                isSameTile = false;
                try {
                    height += (float) room.getModel().getSquareHeight()[newPosition.getX()][newPosition.getY()];
                    for(FloorItem stackItem : room.getItems().getItemsOnSquare(newPosition.getX(), newPosition.getY())) {
                        if(item.getId() != stackItem.getId()) {
                            if(stackItem.getDefinition().canStack) {
                                height += stackItem.getDefinition().getHeight();
                            } else {
                                isCancelled = true;
                                break;
                            }
                        }
                    }
                } catch(Exception e) {
                    isCancelled = true;
                }
            }

            if(!room.getMapping().isValidStep(previousPosition, newPosition, false)) {
                // We can't move here!
                isCancelled = true;
            };

            if(!isCancelled) {
                for(AvatarEntity entity : room.getEntities().getEntitiesAt(previousPosition.getX(), previousPosition.getY())) {
                    if(entity.hasStatus("sit") && !isSameTile) {
                        entity.removeStatus("sit");
                    }

                    entity.markNeedsUpdate();
                }

                for(AvatarEntity entity : room.getEntities().getEntitiesAt(newPosition.getX(), newPosition.getY())) {
                    if(entity.hasStatus("sit") && !isSameTile) {
                        entity.removeStatus("sit");
                    }

                    entity.markNeedsUpdate();
                }

                itemInstance.setRotation(rotation);
                itemInstance.setY(newPosition.getY());
                itemInstance.setX(newPosition.getX());
                itemInstance.setHeight(height);

                room.getEntities().broadcastMessage(UpdateFloorItemMessageComposer.compose(itemInstance, room.getData().getOwnerId()));
            }
        }
    }

    private Random random = new Random();

    private Position3D handleMovement(Position3D point, int movementType) {
        switch(movementType) {
            case 1:
                // Random
                int movement = random.nextInt((4 - 1) + 1 + 1);

                if(movement == 1) {
                    point = handleMovement(point, 4);
                } else if(movement == 2) {
                    point = handleMovement(point, 5);
                } else if(movement == 3) {
                    point = handleMovement(point, 6);
                } else {
                    point = handleMovement(point, 7);
                }
                break;

            case 2:
                // Left right
                int i = random.nextInt((2 - 1) + 1 + 1);

                if(i == 1) {
                    point = handleMovement(point, 7);
                } else {
                    point = handleMovement(point, 5);
                }
                break;

            case 3:
                // Up down
                int j = random.nextInt((2 - 1) + 1 + 1);

                if(j == 1) {
                    point = handleMovement(point, 4);
                } else {
                    point = handleMovement(point, 6);
                }
                break;

            case 4:
                // Up
                point.setY(point.getY() - 1);
                break;

            case 5:
                // Right
                point.setX(point.getX() + 1);
                break;

            case 6:
                // Down
                point.setY(point.getY() + 1);
                break;

            case 7:
                // Left
                point.setX(point.getX() - 1);
                break;
        }

        return point;
    }

    private int handleRotation(int rotation, int rotationType) {
        switch(rotationType) {
            case 1:
                // Clockwise
                rotation = rotation + 2;

                if(rotation > 6)
                    rotation = 0;
                break;

            case 2:
                // Counter clockwise
                rotation = rotation - 2;

                if(rotation > 6)
                    rotation = 6;
                break;

            case 3:
                // Random
                int i = random.nextInt((2 - 1) + 1 + 1);

                if(i == 1) {
                    rotation = handleRotation(rotation, 1);
                } else {
                    rotation = handleRotation(rotation, 2);
                }
                break;

        }

        return rotation;
    }

    @Override
    public void onSave(Event event, FloorItem item) {
        event.readInt(); // don't need this
        int movement = event.readInt();
        int rotation = event.readInt();

        event.readString();

        int itemCount = event.readInt();
        WiredDataInstance instance = WiredDataFactory.get(item);

        if(instance == null) {
            return;
        }

        instance.getItems().clear();

        for(int i = 0; i < itemCount; i++) {
            instance.addItem(event.readInt());
        }

        instance.setDelay(event.readInt());
        instance.setMovement(movement);
        instance.setRotation(rotation);

        WiredDataFactory.save(instance);
    }
}
