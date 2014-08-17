package com.cometproject.server.api;

import com.cometproject.server.api.rooms.RoomStats;
import com.cometproject.server.api.transformers.JsonTransformer;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import javolution.util.FastMap;
import org.apache.log4j.Logger;
import spark.Spark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class APIManager {
    /**
     * Logger
     */
    private static final Logger log = Logger.getLogger(APIManager.class.getName());

    /**
     * Create an array of config properties that are required before enabling the API
     * If none of these properties exist, the API will be automatically disabled
     */
    private static final String[] configProperties = new String[]{
            "comet.api.enabled",
            "comet.api.port",
            "comet.api.token"
    };

    /**
     * Is the API enabled?
     */
    private boolean enabled;

    /**
     * The port the API server will listen on
     */
    private int port;

    /**
     * The token used for authentication
     */
    private String authToken;


    /**
     * The transformer to convert objects into JSON formatted strings
     */
    private JsonTransformer jsonTransformer;

    /**
     * Construct the API manager
     */
    public APIManager() {
        this.initializeConfiguration();
        this.initializeSpark();
        this.initializeRouting();
    }

    /**
     * Initialize the configuration
     */
    private void initializeConfiguration() {
        for (String configProperty : configProperties) {
            if (!Comet.getServer().getConfig().containsKey(configProperty)) {
                log.warn("API configuration property not available: " + configProperty + ", API is disabled");
                this.enabled = false;

                return;
            }
        }

        this.enabled = Comet.getServer().getConfig().getProperty("comet.api.enabled").equals("true");
        this.port = Integer.parseInt(Comet.getServer().getConfig().getProperty("comet.api.port"));
        this.authToken = Comet.getServer().getConfig().getProperty("comet.api.token");
    }

    /**
     * Initialize the Spark web framework
     */
    private void initializeSpark() {
        if (!this.enabled)
            return;

        Spark.setPort(this.port);

        this.jsonTransformer = new JsonTransformer();
    }

    /**
     * Initialize the API routing
     */
    private void initializeRouting() {
        if (!this.enabled)
            return;

        Spark.before((request, response) -> {
            boolean authenticated = request.headers("authToken") != null && request.headers("authToken").equals(this.authToken);

            if (!authenticated) {
                log.error("Unauthenticated request from: " + request.ip() + "; " + request.contextPath());
                Spark.halt(401, "Invalid authentication");
            }
        });

        Spark.get("/", (request, response) -> {
            Spark.halt(404);
            return "Invalid request, if you believe you received this in error, please contact the server administrator!";
        });

        Spark.get("/rooms/active/all", (request, response) -> {
            response.type("application/json");

            List<RoomStats> activeRooms = new ArrayList<>();

            for (Room room : CometManager.getRooms().getRoomInstances().values()) {
                activeRooms.add(new RoomStats(room));
            }

            return activeRooms;
        }, jsonTransformer);

        Spark.get("/room/:id/:action", (request, response) -> {
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
        }, jsonTransformer);
    }
}
