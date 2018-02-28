package com.cometproject.server.game.rooms.types.components.games.freeze;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.freeze.FreezeBlockFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.freeze.FreezeExitFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.freeze.FreezeTileFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.freeze.FreezeTimerFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.games.GameType;
import com.cometproject.server.game.rooms.types.components.games.RoomGame;
import com.cometproject.server.game.rooms.types.components.games.freeze.types.FreezeBall;
import com.cometproject.server.game.rooms.types.components.games.freeze.types.FreezePlayer;
import com.cometproject.server.game.rooms.types.components.games.freeze.types.FreezePowerUp;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.messages.outgoing.room.avatar.ActionMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.freeze.UpdateFreezeLivesMessageComposer;
import com.cometproject.server.utilities.Direction;
import com.cometproject.server.utilities.RandomUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FreezeGame extends RoomGame {

    private static final Direction[] EXPLODE_NORMAL = new Direction[]{null, Direction.North, Direction.East, Direction.South, Direction.West};
    private static final Direction[] EXPLODE_DIAGONAL = new Direction[]{null, Direction.NorthEast, Direction.SouthEast, Direction.SouthWest, Direction.NorthWest};
    private static final Direction[] EXPLODE_MEGA = new Direction[]{null, Direction.North, Direction.NorthEast, Direction.East, Direction.SouthEast, Direction.South, Direction.SouthWest, Direction.West, Direction.NorthWest};
    private final Map<Integer, FreezePlayer> players = Maps.newConcurrentMap();
    private final Set<FreezeBall> activeBalls = Sets.newConcurrentHashSet();

    public FreezeGame(Room room) {
        super(room, GameType.FREEZE);
    }

    @Override
    public void tick() {
        for (RoomItemFloor item : room.getItems().getByClass(FreezeTimerFloorItem.class)) {
            item.getItemData().setData((gameLength - timer) + "");
            item.sendUpdate();
        }

        for (FreezePlayer freezePlayer : this.players.values()) {
            if (freezePlayer.getFreezeTimer() > 0) {
                freezePlayer.decrementFreezeTimer();

                if (freezePlayer.getFreezeTimer() <= 0 && !freezePlayer.getEntity().canWalk()) {
                    freezePlayer.getEntity().setCanWalk(true);
                }
            }

            if (freezePlayer.getShieldTimer() > 0) {
                freezePlayer.decrementShieldTimer();
            }
        }

        for (FreezeBall freezeBall : this.activeBalls) {
            if (freezeBall.getTicksUntilExplode() == FreezeBall.START_TICKS) {
                freezeBall.getSource().tempState(freezeBall.isMega() ? 6000 : (freezeBall.getRange() <= 4 ? freezeBall.getRange() : 4) * 1000);
            }

            if (freezeBall.getTicksUntilExplode() > 0) {
                freezeBall.decrementTicksUntilExplode();
                continue;
            }

            // kaboom
            dir:
            for (Direction direction : freezeBall.isMega() ? EXPLODE_MEGA : (freezeBall.isDiagonal() ? EXPLODE_DIAGONAL : EXPLODE_NORMAL)) {
                for (int i = 1; i <= (direction == null ? 1 : freezeBall.getRange()); i++) {
                    final RoomTile tile = direction == null ? freezeBall.getSource().getTile() : freezeBall.getSource().getRoom().getMapping().getTile(freezeBall.getSource().getPosition().getX() + (i * direction.modX), freezeBall.getSource().getPosition().getY() + (i * direction.modY));

                    if (tile != null) {
                        boolean hasFreezeTile = false;

                        for (RoomItemFloor floorItem : tile.getItems()) {
                            if (floorItem instanceof FreezeTileFloorItem) {
                                hasFreezeTile = true;

                                floorItem.tempState(11000 + ((i > 10 ? 9 : i - 1) * 100));
                            }
                        }

                        if (!hasFreezeTile) {
                            continue dir;
                        }

                        final Set<FreezeBlockFloorItem> blocks = new HashSet<>();

                        for (RoomItemFloor floorItem : tile.getItems()) {
                            if (floorItem instanceof FreezeBlockFloorItem) {
                                blocks.add(((FreezeBlockFloorItem) floorItem));
                            }
                        }

                        for (RoomEntity entity : tile.getEntities()) {
                            if (entity instanceof PlayerEntity) {
                                final PlayerEntity playerEntity = ((PlayerEntity) entity);

                                if (this.players.containsKey(playerEntity.getPlayerId())) {
                                    // we lost 10 points!
                                    this.getGameComponent().decreaseScore(playerEntity.getGameTeam(), 10);

                                    final FreezePlayer freezePlayer = this.freezePlayer(playerEntity.getPlayerId());

                                    if (freezePlayer.getLives() <= 0) {
                                        this.playerLost(freezePlayer);
                                        continue;
                                    }

                                    if (freezePlayer.getFreezeTimer() != 0 || freezePlayer.getShieldTimer() != 0) {
                                        continue;
                                    }

                                    freezePlayer.decrementLives();
                                    freezePlayer.setFreezeTimer(5);

                                    entity.applyEffect(new PlayerEffect(12, 10));//5sec

                                    entity.cancelWalk();
                                    entity.setCanWalk(false);
                                }
                            }
                        }

                        for (FreezeBlockFloorItem block : blocks) {
                            block.explode();
                        }

                        blocks.clear();
                    }
                }
            }

            final int launcherId = freezeBall.getPlayerId();

            if (this.players.containsKey(launcherId)) {
                this.players.get(launcherId).incrementBalls();
            }
            this.activeBalls.remove(freezeBall);
        }
    }

    private void playerLost(FreezePlayer freezePlayer) {
        final FreezeExitFloorItem exitItem = this.getExitTile();
        freezePlayer.getEntity().teleportToItem(exitItem);

        this.players.remove(freezePlayer.getEntity().getPlayerId());

        this.getGameComponent().getPlayers().remove(freezePlayer.getEntity());
        this.getGameComponent().removeFromTeam(freezePlayer.getEntity());

        int teams = 0;

        for (GameTeam team : this.getGameComponent().getTeams().keySet()) {
            if (this.getGameComponent().getTeams().get(team).size() > 0) {
                teams++;
            }
        }

        if (teams == 1) {
            this.gameComplete();
        }
    }

    private void gameComplete() {
        this.onGameEnds();

        final GameTeam winningTeam = this.getBestTeam();

        final FreezeExitFloorItem exitItem = this.getExitTile();

        if (winningTeam != null) {
            for (FreezePlayer freezePlayer : this.players.values()) {
                if (freezePlayer.getEntity().getGameTeam() == winningTeam) {
                    this.room.getEntities().broadcastMessage(new ActionMessageComposer(freezePlayer.getEntity().getId(), 1)); // wave o/
                }
            }
        }

        for (FreezePlayer freezePlayer : this.players.values()) {
            freezePlayer.getEntity().teleportToItem(exitItem);
        }
    }

    private GameTeam getBestTeam() {
        GameTeam best = null;

        for (Map.Entry<GameTeam, List<Integer>> team : this.getGameComponent().getTeams().entrySet()) {
            if (team.getValue().size() > 0 && this.getGameComponent().getScore(team.getKey()) > 0 &&
                    (best == null || this.getGameComponent().getScore(team.getKey()) > this.getGameComponent().getScore(best))) {
                best = team.getKey();
            }
        }

        // Detect draws
        if (best != null) {
            for (Map.Entry<GameTeam, List<Integer>> team : this.getGameComponent().getTeams().entrySet()) {
                if (team.getValue().size() > 0 && this.getGameComponent().getScore(team.getKey()) > 0 &&
                        this.getGameComponent().getScore(team.getKey()) == this.getGameComponent().getScore(best)) {
                    return null;
                }
            }
        }

        return best;
    }

    public void launchBall(FreezeTileFloorItem freezeTile, FreezePlayer freezePlayer) {
        int range = freezePlayer != null ? 2 : (RandomUtil.getRandomBool(0.10) ? 999 : RandomUtil.getRandomInt(1, 3));
        boolean diagonal = freezePlayer == null && (RandomUtil.getRandomBool(0.5));
        int playerId = freezePlayer == null ? -1 : freezePlayer.getEntity().getPlayerId();

        if (freezePlayer != null) {
            switch (freezePlayer.getPowerUp()) {
                case ExtraRange:
                    range += 2;
                    break;

                case DiagonalExplosion:
                    diagonal = true;
                    break;

                case MegaExplosion:
                    range = 999;
            }

            freezePlayer.powerUp(FreezePowerUp.None);
        }

        final FreezeBall freezeBall = new FreezeBall(playerId, freezeTile, range, diagonal);
        this.activeBalls.add(freezeBall);
    }

    @Override
    public void onGameStarts() {
        this.activeBalls.clear();

        // Everyone starts with 40 points & 3 lives.
        for (PlayerEntity playerEntity : this.room.getGame().getPlayers()) {
            this.players.put(playerEntity.getPlayerId(), new FreezePlayer(playerEntity));

            this.getGameComponent().increaseScore(playerEntity.getGameTeam(), 40);
            playerEntity.getRoom().getEntities().broadcastMessage(new UpdateFreezeLivesMessageComposer(playerEntity.getId(), 3));
        }

        for (FreezeBlockFloorItem blockItem : this.room.getItems().getByClass(FreezeBlockFloorItem.class)) {
            blockItem.reset();
        }

        for (FreezeExitFloorItem exitItem : this.room.getItems().getByClass(FreezeExitFloorItem.class)) {
            exitItem.getItemData().setData("1");
            exitItem.sendUpdate();
        }
    }

    public FreezePlayer freezePlayer(final int playerId) {
        return this.players.get(playerId);
    }

    @Override
    public void onGameEnds() {
        for (FreezeBlockFloorItem blockItem : this.room.getItems().getByClass(FreezeBlockFloorItem.class)) {
            blockItem.getItemData().setData("0");
            blockItem.sendUpdate();
        }

        for (FreezeExitFloorItem exitItem : this.room.getItems().getByClass(FreezeExitFloorItem.class)) {
            exitItem.getItemData().setData("0");
            exitItem.sendUpdate();
        }

        for (FreezeTimerFloorItem timer : this.room.getItems().getByClass(FreezeTimerFloorItem.class)) {
            timer.getItemData().setData("0");
            timer.sendUpdate();
        }

        for (PlayerEntity playerEntity : this.getGameComponent().getPlayers()) {
            playerEntity.setCanWalk(true);
        }

        // reset all scores to 0
        this.activeBalls.clear();
        this.getGameComponent().resetScores(true);
    }

    private FreezeExitFloorItem getExitTile() {
        for (FreezeExitFloorItem exitItem : this.room.getItems().getByClass(FreezeExitFloorItem.class)) {
            return exitItem;
        }

        return null;
    }
}
