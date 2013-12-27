package com.cometsrv.network.http;

import com.sun.net.httpserver.HttpExchange;

public abstract class ManagementPage {
    public abstract boolean handle(HttpExchange e, String uri);
}
