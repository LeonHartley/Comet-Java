package com.cometproject.server.network.http;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.network.messages.outgoing.messenger.FollowFriendMessageComposer;
import com.cometproject.server.network.messages.outgoing.misc.MotdNotificationComposer;
import com.cometproject.server.network.sessions.Session;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

public class ManagementCommandHandler implements HttpHandler {

    private String authKey;
    private Logger log = Logger.getLogger(ManagementCommandHandler.class.getName());

    public ManagementCommandHandler() {
        this.authKey = Comet.getServer().getConfig().get("comet.network.http.authKey");
    }

    @Override
    public void handle(HttpExchange e) throws IOException {
        String queryString;
        String ipAddress = e.getRemoteAddress().getAddress().getHostAddress();

        try (InputStream in = e.getRequestBody()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte buf[] = new byte[in.available()];

            for (int n = in.read(buf); n > 0; n = in.read(buf)) {
                out.write(buf, 0, n);
            }

            queryString = new String(out.toByteArray(), Charset.defaultCharset());
        }

        if(!e.getRequestMethod().equals("POST")) {
            Comet.getServer().getNetwork().getManagement().sendResponse("Invalid request", e);
            return;
        }

        Map<String, String> requestParameters = new FastMap<>();

        for(int i = 0; i < queryString.split("&").length; i++) {
            requestParameters.put(queryString.split("&")[i].split("=")[0], java.net.URLDecoder.decode(queryString.split("&")[i].split("=")[1], "UTF-8"));
        }

        if(!requestParameters.containsKey("auth")) {
            Comet.getServer().getNetwork().getManagement().sendResponse(RequestError.AUTH_FAILED, e);
            return;
        }

        if(!requestParameters.get("auth").equals(authKey)) {
            log.error("Failed authentication from: " + ipAddress + " - Data: " + queryString);
            return;
        }

        if(!requestParameters.containsKey("command")) {
            Comet.getServer().getNetwork().getManagement().sendResponse(RequestError.INVALID_COMMAND, e);
            return;
        }

        switch(requestParameters.get("command")) {
            case "senduser": {
                int userId = Integer.parseInt(requestParameters.get("user_id"));
                int roomId = Integer.parseInt(requestParameters.get("room_id"));

                Session user = Comet.getServer().getNetwork().getSessions().getByPlayerId(userId);

                if (user == null) {
                    Comet.getServer().getNetwork().getManagement().sendResponse(RequestError.INVALID_USER, e);
                    return;
                } else if (GameEngine.getRooms().get(roomId) == null) {
                    Comet.getServer().getNetwork().getManagement().sendResponse(RequestError.INVALID_ROOM, e);
                    return;
                }

                user.send(FollowFriendMessageComposer.compose(roomId));
                break;
            }

            case "disconnect": {
                int userId = Integer.parseInt(requestParameters.get("user_id"));

                Session user = Comet.getServer().getNetwork().getSessions().getByPlayerId(userId);

                if (user == null) {
                    Comet.getServer().getNetwork().getManagement().sendResponse(RequestError.INVALID_USER, e);
                    return;
                }

                user.disconnect();
                break;
            }

            case "alert": {
                int userId = Integer.parseInt(requestParameters.get("user_id"));
                String alertMessage = requestParameters.get("message");

                Session user = Comet.getServer().getNetwork().getSessions().getByPlayerId(userId);

                if (user == null) {
                    Comet.getServer().getNetwork().getManagement().sendResponse(RequestError.INVALID_USER, e);
                    return;
                }

                user.send(MotdNotificationComposer.compose(alertMessage));
                break;
            }

            case "update_credits": {
                int userId = Integer.parseInt(requestParameters.get("user_id"));

                Session user = Comet.getServer().getNetwork().getSessions().getByPlayerId(userId);

                if (user == null) {
                    Comet.getServer().getNetwork().getManagement().sendResponse(RequestError.INVALID_USER, e);
                    return;
                }

                int userCredits = Integer.parseInt(Comet.getServer().getStorage().getString("SELECT `credits` FROM players WHERE id = " + userId));

                user.getPlayer().getData().setCredits(userCredits);
                user.getPlayer().sendBalance();
                break;
            }

            case "update_vip": {
                int userId = Integer.parseInt(requestParameters.get("user_id"));

                Session user = Comet.getServer().getNetwork().getSessions().getByPlayerId(userId);

                if (user == null) {
                    Comet.getServer().getNetwork().getManagement().sendResponse(RequestError.INVALID_USER, e);
                    return;
                }

                boolean isVip = Comet.getServer().getStorage().getString("SELECT `vip` FROM players WHERE id = " + userId).equals("1");

                user.getPlayer().getData().setVip(isVip);
                break;
            }
        }

        log.info("Completed remote command from " + ipAddress + " - Data: " + queryString);
        Comet.getServer().getNetwork().getManagement().sendResponse(RequestError.REQUEST_COMPLETE, e);
    }

    public enum RequestError {
        AUTH_FAILED,
        INVALID_USER,
        INVALID_ROOM,
        INVALID_COMMAND,
        REQUEST_COMPLETE
    }
}