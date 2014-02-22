package com.cometproject.network.http.rooms;

import com.cometproject.network.http.ManagementPage;
import com.sun.net.httpserver.HttpExchange;

public class RoomObjectPage extends ManagementPage {
    @Override
    public boolean handle(HttpExchange e, String uri) {
        return false;
    }
}
