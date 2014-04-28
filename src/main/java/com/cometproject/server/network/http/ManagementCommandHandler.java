package com.cometproject.server.network.http;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.network.messages.outgoing.messenger.FollowFriendMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import javolution.util.FastMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

public class ManagementCommandHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange e) throws IOException {
        String queryString;

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
            requestParameters.put(queryString.split("&")[i].split("=")[0], queryString.split("&")[i].split("=")[1]);
        }

      /*  if(!requestParameters.containsKey("auth")) {

        }
*/
        if(!requestParameters.containsKey("command")) {
            Comet.getServer().getNetwork().getManagement().sendResponse("Invalid request", e);
            return;
        }

        switch(requestParameters.get("command")) {
            case "senduser":
                int userId = Integer.parseInt(requestParameters.get("user_id"));
                int roomId = Integer.parseInt(requestParameters.get("room_id"));

                // Send user to room!
                Session user = Comet.getServer().getNetwork().getSessions().getByPlayerId(userId);

                if(user == null) {
                    Comet.getServer().getNetwork().getManagement().sendResponse(RequestError.INVALID_USER, e);
                    return;
                } else if(GameEngine.getRooms().get(roomId) == null) {
                    Comet.getServer().getNetwork().getManagement().sendResponse(RequestError.INVALID_ROOM, e);
                    return;
                }

                user.send(FollowFriendMessageComposer.compose(roomId));
                System.out.println("It worked!");
                break;

            case "disconnect":

                break;

            case "reload_data":

                break;

            default:
                Comet.getServer().getNetwork().getManagement().sendResponse(RequestError.INVALID_COMMAND, e);
                break;
        }

        Comet.getServer().getNetwork().getManagement().sendResponse(RequestError.REQUEST_COMPLETE, e);
    }

    public enum RequestError {
        INVALID_USER,
        INVALID_ROOM,
        INVALID_COMMAND,
        REQUEST_COMPLETE
    }
}