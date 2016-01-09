package com.cometproject.server.network.messages.outgoing.navigator.updated;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.navigator.types.Category;
import com.cometproject.server.game.navigator.types.categories.NavigatorSearchAllowance;
import com.cometproject.server.game.navigator.types.categories.NavigatorViewMode;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.List;

public class NavigatorSearchResultSetMessageComposer extends MessageComposer {

    private final String category;
    private final String data;
    private final List<Category> categories;
    private final Player player;

    public NavigatorSearchResultSetMessageComposer(String category, String data, List<Category> categories, Player player) {
        this.category = category;
        this.data = data;
        this.categories = categories;
        this.player = player;
    }

    @Override
    public short getId() {
        return Composers.NavigatorSearchResultSetMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.category);
        msg.writeString(this.data);
        msg.writeInt(this.categories.size());

        for(Category category : this.categories) {
            msg.writeString(category.getCategoryId());
            msg.writeString(category.getPublicName());

            msg.writeInt(NavigatorSearchAllowance.getIntValue(category.getSearchAllowance()));
            msg.writeBoolean(false);//is minimised
            msg.writeInt(category.getViewMode() == NavigatorViewMode.REGULAR ? 0 : category.getViewMode() == NavigatorViewMode.THUMBNAIL ? 1 : 0);
            msg.writeInt(0);// size of rooms found.
        }
    }

    @Override
    public void dispose() {
        this.categories.clear();
    }
}
