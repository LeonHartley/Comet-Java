package com.cometproject.server.game.rooms.objects.items.types.floor.banzai;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.games.banzai.BanzaiGame;
import com.cometproject.server.game.rooms.types.components.games.banzai.BanzaiSolver;
import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.List;

public class BanzaiTileFloorItem extends RoomItemFloor {
    private GameTeam gameTeam = GameTeam.NONE;
    private int points = 0;

    public BanzaiTileFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        this.setExtraData("0");
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        if(!(entity instanceof PlayerEntity) || ((PlayerEntity) entity).getGameTeam() == GameTeam.NONE || this.getRoom().getGame().getInstance() == null) {
            return;
        }

        if(this.points == 3) {
            // It's locked, what you doing?!?
            return;
        }

        if(((PlayerEntity) entity).getGameTeam() == this.gameTeam) {
            this.points++;
        } else {
            this.gameTeam = ((PlayerEntity) entity).getGameTeam();
            this.points = 1;
        }

        if(this.points == 3) {
            ((BanzaiGame) this.getRoom().getGame().getInstance()).increaseScore(this.gameTeam, 1);

            final List<BanzaiTileFloorItem> rectangle = buildBanzaiRectangle(this, this.getPosition().getX(), this.getPosition().getY(), 0, 0, -1, 4, gameTeam);

            if(rectangle != null) {
                for (RoomItemFloor floorItem : this.getRoom().getItems().getByInteraction("bb_patch")) {
                    BanzaiTileFloorItem tileItem = ((BanzaiTileFloorItem) floorItem);
                    if (tileItem.getPoints() == 3) continue;

                    final boolean[] borderCheck = new boolean[4];

                    for (BanzaiTileFloorItem rectangleItem : rectangle) {
                        if (rectangleItem.getPosition().getY() == floorItem.getPosition().getY()) {
                            if (rectangleItem.getPosition().getX() > floorItem.getPosition().getX()) {
                                borderCheck[0] = true;
                            } else {
                                borderCheck[1] = true;
                            }
                        } else if (rectangleItem.getPosition().getX() == floorItem.getPosition().getX()) {
                            if (rectangleItem.getPosition().getY() > floorItem.getPosition().getY()) {
                                borderCheck[2] = true;
                            } else {
                                borderCheck[3] = true;
                            }
                        }
                    }

                    if (borderCheck[0] && borderCheck[1] && borderCheck[2] && borderCheck[3]) {
                        tileItem.setPoints(3);
                        tileItem.setTeam(this.gameTeam);

                        ((BanzaiGame) this.getRoom().getGame().getInstance()).increaseScore(this.gameTeam, 1);
                        tileItem.updateTileData();
                    }
                }
            }
        }

        this.updateTileData();
    }

    public static List<BanzaiTileFloorItem> buildBanzaiRectangle(final BanzaiTileFloorItem triggerItem, final int x, final int y,
                                                                 final int goX, final int goY, final int currentDirection, final int turns, final GameTeam team) {
        final boolean[] directions = new boolean[4];

        if (goX == -1 || goX == 0) {
            directions[0] = true;
        }
        if (goX == 1 || goX == 0) {
            directions[2] = true;
        }
        if (goY == -1 || goY == 0) {
            directions[1] = true;
        }
        if (goY == 1 || goY == 0) {
            directions[3] = true;
        }

        if ((goX != 0 || goY != 0) && triggerItem.getPosition().getX() == x && triggerItem.getPosition().getY() == y) {
            return new LinkedList<>();
        }

        final Room room = triggerItem.getRoom();

        for (int i = 0; i < 4; ++i) {
            if (!directions[i]) {
                continue;
            }

            int nextXStep = 0, nextYStep = 0;

            if (i == 0 || i == 2) {
                nextXStep = (i == 0) ? 1 : -1;
            } else if (i == 1 || i == 3) {
                nextYStep = (i == 1) ? 1 : -1;
            }

            final int nextX = x + nextXStep;
            final int nextY = y + nextYStep;

            RoomItemFloor obj = room.getItems().getFloorItem(room.getMapping().getTile(nextX, nextY).getTopItem());

            if(obj instanceof BanzaiTileFloorItem) {
                final BanzaiTileFloorItem item = (BanzaiTileFloorItem) obj;

                if (item != null && item.getTeam() == team && item.getPoints() == 3) {
                    List<BanzaiTileFloorItem> foundPatches = null;
                    if (currentDirection != i && currentDirection != -1) {
                        if (turns > 0) {
                            foundPatches = buildBanzaiRectangle(
                                    triggerItem, nextX, nextY,
                                    (nextXStep == 0) ? (goX * -1) : (nextXStep * -1),
                                    (nextYStep == 0) ? (goY * -1) : (nextYStep * -1),
                                    i, (turns - 1), team
                            );
                        }
                    } else {
                        foundPatches = buildBanzaiRectangle(
                                triggerItem, nextX, nextY,
                                (nextXStep == 0) ? goX : (nextXStep * -1),
                                (nextYStep == 0) ? goY : (nextYStep * -1),
                                i, turns, team
                        );
                    }
                    if (foundPatches != null) {
                        foundPatches.add(item);
                        return foundPatches;
                    }
                }
            }
        }
        return null;
    }

    private boolean needsChange = false;

    @Override
    public void onTick() {
        if(this.hasTicks()) {
            System.out.println("yes");
            if(needsChange) {
                this.setExtraData("1");
                this.sendUpdate();
                this.needsChange = false;
            } else {
                this.needsChange = true;
                this.updateTileData();
            }
        }
    }

    public void flash() {
        if(this.points == 3) {
            this.needsChange = true;
            this.setTicks(6);//3s
        }
    }

    public void onGameStarts() {
        this.gameTeam = GameTeam.NONE;
        this.points = 0;
        this.updateTileData();
    }

    public void updateTileData() {
        if(this.points != 0)
            this.setExtraData(((this.points + (gameTeam.getTeamId() * 3) - 1) + ""));
        else
            this.setExtraData("1");
        this.sendUpdate();
    }

    public GameTeam getTeam() {
        return this.gameTeam;
    }

    public void setTeam(GameTeam gameTeam) {
        this.gameTeam = gameTeam;
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
