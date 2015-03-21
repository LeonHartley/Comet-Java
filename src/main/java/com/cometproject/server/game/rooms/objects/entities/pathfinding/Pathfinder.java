package com.cometproject.server.game.rooms.objects.entities.pathfinding;

import com.cometproject.server.game.rooms.objects.RoomObject;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.google.common.collect.Lists;
import com.google.common.collect.MinMaxPriorityQueue;

import java.util.LinkedList;
import java.util.List;


public class Pathfinder {
    public static final byte DISABLE_DIAGONAL = 0;
    public static final byte ALLOW_DIAGONAL = 1;

    private static Pathfinder instance;

    public static Pathfinder getInstance() {
        if (instance == null)
            instance = new Pathfinder();

        return instance;
    }

    public List<Square> makePath(RoomObject roomObject, Position end) {
        return this.makePath(roomObject, end, ALLOW_DIAGONAL);
    }

    public List<Square> makePath(RoomObject roomObject, Position end, byte pathfinderMode) {
        LinkedList<Square> squares = new LinkedList<>();

        PathfinderNode nodes = makePathReversed(roomObject, end, pathfinderMode);

        if (nodes != null) {
            while (nodes.getNextNode() != null) {
                squares.add(new Square(nodes.getPosition().getX(), nodes.getPosition().getY()));
                nodes = nodes.getNextNode();
            }
        }

        return Lists.reverse(squares);
    }

    private PathfinderNode makePathReversed(RoomObject roomObject, Position end, byte pathfinderMode) {
        MinMaxPriorityQueue<PathfinderNode> openList = MinMaxPriorityQueue.maximumSize(256).create();

        PathfinderNode[][] map = new PathfinderNode[roomObject.getRoom().getMapping().getModel().getSizeX()][roomObject.getRoom().getMapping().getModel().getSizeY()];
        PathfinderNode node = null;
        Position tmp;

        int cost;
        int diff;

        PathfinderNode current = new PathfinderNode(roomObject.getPosition());
        current.setCost(0);

        PathfinderNode finish = new PathfinderNode(end);

        map[current.getPosition().getX()][current.getPosition().getY()] = current;
        openList.add(current);

        final boolean isFloorItem = roomObject instanceof RoomItemFloor;

        while (openList.size() > 0) {
            current = openList.pollFirst();
            current.setInClosed(true);

            for (int i = 0; i < (pathfinderMode == ALLOW_DIAGONAL ? diagonalMovePoints().length : movePoints().length); i++) {
                tmp = current.getPosition().add((pathfinderMode == ALLOW_DIAGONAL ? diagonalMovePoints() : movePoints())[i]);
                final boolean isFinalMove = (tmp.getX() == end.getX() && tmp.getY() == end.getY());

                if (roomObject.getRoom().getMapping().isValidStep(roomObject instanceof GenericEntity ? roomObject.getId() : 0,
                        new Position(current.getPosition().getX(), current.getPosition().getY(), current.getPosition().getZ()), tmp, isFinalMove, isFloorItem) ||
                        (roomObject instanceof GenericEntity && ((GenericEntity) roomObject).isOverriden())) {

                    try {
                        if (map[tmp.getX()][tmp.getY()] == null) {
                            node = new PathfinderNode(tmp);
                            map[tmp.getX()][tmp.getY()] = node;
                        } else {
                            node = map[tmp.getX()][tmp.getY()];
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }

                    if (!node.isInClosed()) {
                        diff = 0;

                        if (current.getPosition().getX() != node.getPosition().getX()) {
                            diff += 1;
                        }

                        if (current.getPosition().getY() != node.getPosition().getY()) {
                            diff += 1;
                        }

                        cost = current.getCost() + diff + node.getPosition().getDistanceSquared(end);

                        if (cost < node.getCost()) {
                            node.setCost(cost);
                            node.setNextNode(current);
                        }

                        if (!node.isInOpen()) {
                            if (node.getPosition().getX() == finish.getPosition().getX() && node.getPosition().getY() == finish.getPosition().getY()) {
                                node.setNextNode(current);
                                return node;
                            }

                            node.setInOpen(true);
                            openList.add(node);
                        }
                    }
                }
            }
        }

        return null;
    }

    private Position[] diagonalMovePoints() {
        return new Position[]{
                new Position(0, -1),
                new Position(0, 1),
                new Position(1, 0),
                new Position(-1, 0),
                new Position(1, -1),
                new Position(-1, 1),
                new Position(1, 1),
                new Position(-1, -1)
        };
    }

    private Position[] movePoints() {
        return new Position[]{
                new Position(0, -1),
                new Position(1, 0),
                new Position(0, 1),
                new Position(-1, 0)
        };
    }
}