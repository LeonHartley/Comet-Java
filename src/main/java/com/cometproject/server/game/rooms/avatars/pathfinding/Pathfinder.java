package com.cometproject.server.game.rooms.avatars.pathfinding;

import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.AvatarEntity;
import com.google.common.collect.Lists;
import com.google.common.collect.MinMaxPriorityQueue;

import java.util.LinkedList;
import java.util.List;

public class Pathfinder {
    protected AvatarEntity entity;

    public Pathfinder(AvatarEntity entity) {
        this.entity = entity;
    }

    public List<Square> makePath() {
        LinkedList<Square> squares = new LinkedList<>();

        PathfinderNode nodes = makePathReversed();

        if (nodes != null) {
            while (nodes.getNextNode() != null) {
                squares.add(new Square(nodes.getPosition().getX(), nodes.getPosition().getY()));
                nodes = nodes.getNextNode();
            }
        }

        return Lists.reverse(squares);
    }

    private PathfinderNode makePathReversed() {
        MinMaxPriorityQueue<PathfinderNode> openList = MinMaxPriorityQueue.maximumSize(256).create();

        PathfinderNode[][] map = new PathfinderNode[entity.getRoom().getMapping().getModel().getSizeX()][entity.getRoom().getMapping().getModel().getSizeY()];
        PathfinderNode node;
        Position3D tmp;

        int cost;
        int diff;

        PathfinderNode current = new PathfinderNode(entity.getPosition());
        current.setCost(0);

        Position3D end = entity.getWalkingGoal();
        PathfinderNode finish = new PathfinderNode(end);

        map[current.getPosition().getX()][current.getPosition().getY()] = current;
        openList.add(current);

        while (openList.size() > 0) {
            current = openList.pollFirst();
            current.setInClosed(true);

            for (int i = 0; i < movePoints().length; i++) {
                tmp = current.getPosition().add(movePoints()[i]);
                boolean isFinalMove = (tmp.getX() == end.getX() && tmp.getY() == end.getY());

                if (entity.getRoom().getMapping().isValidStep(new Position3D(current.getPosition().getX(), current.getPosition().getY(), current.getPosition().getZ()), tmp, isFinalMove) || entity.isInTeleporter()) {
                    if (map[tmp.getX()][tmp.getY()] == null) {
                        node = new PathfinderNode(tmp);
                        map[tmp.getX()][tmp.getY()] = node;
                    } else {
                        node = map[tmp.getX()][tmp.getY()];
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

    private Position3D[] movePoints() {
        return new Position3D[] {
                new Position3D(0, -1, 0),
                new Position3D(0, 1, 0),
                new Position3D(1, 0, 0),
                new Position3D(-1, 0, 0),
                new Position3D(1, -1, 0),
                new Position3D(-1, 1, 0),
                new Position3D(1, 1, 0),
                new Position3D(-1, -1, 0)
        };
    }
}