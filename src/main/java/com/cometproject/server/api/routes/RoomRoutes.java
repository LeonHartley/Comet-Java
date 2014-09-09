package com.cometproject.server.api.routes;

import com.cometproject.server.api.rooms.RoomStats;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import javolution.util.FastMap;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomRoutes {
    public static Object getAllActiveRooms(Request request, Response response) {
        response.type("application/json");
        Map<String, Object> result = new FastMap<>();

        List<RoomStats> activeRooms = new ArrayList<>();

        for (Room room : CometManager.getRooms().getRoomInstances().values()) {
            activeRooms.add(new RoomStats(room));
        }

        result.put("response", activeRooms);
        return result;
    }

    public static Object roomAction(Request request, Response response) {
        Map<String, Object> result = new FastMap<>();

        int roomId = Integer.parseInt(request.params("id"));
        String action = request.params("action");

        if (!CometManager.getRooms().getRoomInstances().containsKey(roomId)) {
            result.put("active", false);
            return result;
        }

        Room room = CometManager.getRooms().get(roomId);

        result.put("id", roomId);

        switch (action) {
            default: {
                result.put("active", false);
                break;
            }

            case "alert": {
                String message = request.headers("message");
                if (message == null || message.isEmpty()) {
                    result.put("success", false);
                } else {
                    room.getEntities().broadcastMessage(AdvancedAlertMessageComposer.compose(message));
                    result.put("success", true);
                }
                break;
            }

            case "dispose": {
                String message = request.headers("message");

                if (message != null && !message.isEmpty()) {
                    room.getEntities().broadcastMessage(AdvancedAlertMessageComposer.compose(message));
                }

                room.setIdleNow();
                result.put("success", true);
                break;
            }

            case "data": {
                result.put("data", room.getData());
                break;
            }

            case "delete":
                result.put("message", "Feature not completed");
                break;
        }

        return result;
    }
}
