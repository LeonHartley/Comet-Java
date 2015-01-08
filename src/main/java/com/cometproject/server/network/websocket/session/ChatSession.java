package com.cometproject.server.network.websocket.session;

import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.storage.queries.webchat.WebChatDao.WebChatDao;
import com.corundumstudio.socketio.SocketIOClient;

public class ChatSession {
    private SocketIOClient webSocketClient;
    private PlayerData playerData;

    public ChatSession(String authTicket, SocketIOClient webSocketClient) {
        this.webSocketClient = webSocketClient;

        int playerId = WebChatDao.findPlayerIdByAuthTicket(authTicket);

        if(playerId != 0) {
            this.playerData = PlayerDao.getDataById(playerId);
        }
    }

    public void dispose() {

    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public SocketIOClient getWebSocketClient() {
        return webSocketClient;
    }
}
