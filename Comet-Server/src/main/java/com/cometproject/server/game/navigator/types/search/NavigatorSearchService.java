package com.cometproject.server.game.navigator.types.search;

import com.cometproject.server.game.navigator.types.Category;
import com.cometproject.server.game.navigator.types.categories.NavigatorCategoryType;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.RoomPromotion;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        List<RoomData> rooms = new LinkedList<>();

        switch (category.getCategoryType()) {
            case MY_ROOMS:
                for (Integer roomId : new LinkedList<>(player.getRooms())) {
                    if (RoomManager.getInstance().getRoomData(roomId) == null) continue;

                    rooms.add(RoomManager.getInstance().getRoomData(roomId));
                }
                break;

            case POPULAR:
                rooms.addAll(order(RoomManager.getInstance().getRoomsByCategory(-1, expanded ? 50 : 12)));
                break;

            case CATEGORY:
                rooms.addAll(order(RoomManager.getInstance().getRoomsByCategory(category.getId(), expanded ? 50 : 12)));
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

                rooms.addAll(order(promotedRooms));
                break;
        }

        return rooms;
    }

    public static List<RoomData> order(List<RoomData> rooms) {
        try {
            Collections.sort(rooms, (room1, room2) -> {
                boolean is1Active = RoomManager.getInstance().isActive(room1.getId());
                boolean is2Active = RoomManager.getInstance().isActive(room2.getId());

                return ((!is2Active ? 0 : RoomManager.getInstance().get(room2.getId()).getEntities().playerCount()) -
                        (!is1Active ? 0 : RoomManager.getInstance().get(room1.getId()).getEntities().playerCount()));
            });
        } catch (Exception ignored) {

        }

        return rooms;
    }

    public static NavigatorSearchService getInstance() {
        if (searchServiceInstance == null) {
            searchServiceInstance = new NavigatorSearchService();
        }

        return searchServiceInstance;
    }
}
