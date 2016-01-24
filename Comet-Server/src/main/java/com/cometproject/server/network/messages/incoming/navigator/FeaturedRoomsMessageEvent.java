//package com.cometproject.server.network.messages.incoming.navigator;
//
//import com.cometproject.server.game.navigator.NavigatorManager;
//import com.cometproject.server.network.messages.incoming.Event;
//import com.cometproject.server.network.messages.outgoing.navigator.FeaturedRoomsMessageComposer;
//import com.cometproject.server.protocol.messages.MessageEvent;
//import com.cometproject.server.network.sessions.Session;
//
//
//public class FeaturedRoomsMessageEvent implements Event {
//    @Override
//    public void handle(Session client, MessageEvent msg) {
//        client.send(new FeaturedRoomsMessageComposer(NavigatorManager.getInstance().getFeaturedRooms()));
//    }
//}
