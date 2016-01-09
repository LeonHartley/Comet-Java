package com.cometproject.server.network.messages.incoming.navigator.updated;

import com.cometproject.server.game.navigator.NavigatorManager;
import com.cometproject.server.game.navigator.types.Category;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.navigator.updated.NavigatorSearchResultSetMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.google.common.collect.Lists;

import java.util.List;

public class NewNavigatorSearchMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        String category = msg.readString();
        String data = msg.readString();

        if(data.isEmpty()) {
            // send categories.
            List<Category> categoryList = Lists.newArrayList();

            for(Category navigatorCategory : NavigatorManager.getInstance().getCategories().values()) {
                if(navigatorCategory.getCategory().equals(category)) {
                    categoryList.add(navigatorCategory);
                }
            }

            client.send(new NavigatorSearchResultSetMessageComposer(category, data, categoryList, client.getPlayer()));
        }
    }
}
