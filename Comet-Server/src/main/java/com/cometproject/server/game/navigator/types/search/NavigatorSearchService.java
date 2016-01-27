package com.cometproject.server.game.navigator.types.search;

import com.cometproject.server.game.navigator.NavigatorManager;
import com.cometproject.server.game.navigator.types.Category;
import com.cometproject.server.game.navigator.types.publics.PublicRoom;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.RoomPromotion;
import com.cometproject.server.tasks.CometTask;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NavigatorSearchService implements CometTask {
    private static NavigatorSearchService searchServiceInstance;

    public NavigatorSearchService() {
//        CometThreadManager.getInstance().executePeriodic(this, 0, 3000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        // TODO: Cache navigator search results.
    }

    public List<RoomData> search(Category category, Player player, boolean expanded) {
        List<RoomData> rooms = Lists.newCopyOnWriteArrayList();

        switch (category.getCategoryType()) {
            case MY_ROOMS:
                for (Integer roomId : new LinkedList<>(player.getRooms())) {
                    if (RoomManager.getInstance().getRoomData(roomId) == null) continue;

                    rooms.add(RoomManager.getInstance().getRoomData(roomId));
                }
                break;

            case POPULAR:
                rooms.addAll(order(RoomManager.getInstance().getRoomsByCategory(-1), expanded ? 50 : 12));
                break;

            case CATEGORY:
                rooms.addAll(order(RoomManager.getInstance().getRoomsByCategory(category.getId()), expanded ? 50 : 12));
                break;

            case TOP_PROMOTIONS:
                List<RoomData> promotedRooms = Lists.newArrayList();

                for (RoomPromotion roomPromotion : RoomManager.getInstance().getRoomPromotions().values()) {
                    if (roomPromotion != null) {
                        RoomData roomData = RoomManager.getInstance().getRoomData(roomPromotion.getRoomId());

                        if (roomData != null) {
                            promotedRooms.add(roomData);
                        }
                    }
                }

                rooms.addAll(order(promotedRooms, expanded ? 50 : 12));
                promotedRooms.clear();
                break;

            case PUBLIC:
                for (PublicRoom publicRoom : NavigatorManager.getInstance().getPublicRooms().values()) {
                    RoomData roomData = RoomManager.getInstance().getRoomData(publicRoom.getRoomId());

                    if (roomData != null) {
                        rooms.add(roomData);
                    }
                }
                break;

            case STAFF_PICKS:
                List<RoomData> staffPicks = Lists.newArrayList();

                for (int roomId : NavigatorManager.getInstance().getStaffPicks()) {
                    RoomData roomData = RoomManager.getInstance().getRoomData(roomId);

                    if (roomData != null) {
                        staffPicks.add(roomData);
                    }
                }

                rooms.addAll(order(staffPicks, expanded ? 50 : 12));
                staffPicks.clear();
                break;
        }

        return rooms;
    }

    public static List<RoomData> order(List<RoomData> rooms, int limit) {
        try {
            Collections.sort(rooms, (room1, room2) -> {
                boolean is1Active = RoomManager.getInstance().isActive(room1.getId());
                boolean is2Active = RoomManager.getInstance().isActive(room2.getId());

                return ((!is2Active ? 0 : RoomManager.getInstance().get(room2.getId()).getEntities().playerCount()) -
                        (!is1Active ? 0 : RoomManager.getInstance().get(room1.getId()).getEntities().playerCount()));
            });
        } catch (Exception ignored) {

        }

        List<RoomData> returnRooms = new LinkedList<>();

        for (RoomData roomData : rooms) {
            if (returnRooms.size() >= limit) {
                break;
            }

            returnRooms.add(roomData);
        }

        return returnRooms;
    }

    public static NavigatorSearchService getInstance() {
        if (searchServiceInstance == null) {
            searchServiceInstance = new NavigatorSearchService();
        }

        return searchServiceInstance;
    }
}
