package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.server.game.players.components.types.MessengerSearchResult;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;

public class MessengerSearchResultsMessageComposer {
    public static Composer compose(List<MessengerSearchResult> currentFriends, List<MessengerSearchResult> otherPeople) {
        Composer msg = new Composer(Composers.SearchFriendsMessageComposer);

        msg.writeInt(currentFriends.size());

        for(MessengerSearchResult result : currentFriends) {
            result.compose(msg);
        }

        msg.writeInt(otherPeople.size());

        for(MessengerSearchResult result : otherPeople) {
            result.compose(msg);
        }

        return msg;
    }
}
