package com.cometproject.server.network.http;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.http.system.StatsPage;
import com.cometproject.server.network.http.users.UserObjectPage;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import javolution.util.FastMap;

import java.io.IOException;
import java.util.Map;

public class PageHandler implements HttpHandler {
    private Map<String, ManagementPage> pages;

    public PageHandler() {
        pages = new FastMap<>();

        pages.put("/user/?", new UserObjectPage());
        pages.put("/stats",  new StatsPage());
    }

    @Override
    public void handle(HttpExchange e) throws IOException {
        String request = e.getRequestURI().toString();

        if(request.equals("/favicon.ico")) {
            return;
        }

        ManagementPage page = null;

        for (Map.Entry<String, ManagementPage> entry : this.pages.entrySet()) {
            if(request.startsWith(entry.getKey().replace("?", ""))) {
                page = entry.getValue();
                break;
            }
        }

        if(page != null) {
            page.handle(e, request);
        } else {
            Comet.getServer().getNetwork().getManagement().sendResponse("Invalid request URI", e);
        }
    }
}