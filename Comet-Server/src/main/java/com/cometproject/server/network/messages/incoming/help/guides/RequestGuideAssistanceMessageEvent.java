package com.cometproject.server.network.messages.incoming.help.guides;

import com.cometproject.server.game.guides.GuideManager;
import com.cometproject.server.game.guides.types.HelpRequest;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.help.guides.GuideSessionErrorMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class RequestGuideAssistanceMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final int type = msg.readInt();
        final String message = msg.readString();

        final HelpRequest existingHelpRequest = GuideManager.getInstance().getHelpRequestByCreator(client.getPlayer().getId());

        if (existingHelpRequest != null) {
            // Error to the user.
            client.send(new GuideSessionErrorMessageComposer(GuideSessionErrorMessageComposer.SOMETHING_WRONG_REQUEST));
            return;
        }

        if (GuideManager.getInstance().getActiveGuideCount() == 0) {
            client.send(new GuideSessionErrorMessageComposer(GuideSessionErrorMessageComposer.NO_HELPERS_AVAILABLE));
            return;
        }

        final HelpRequest helpRequest = new HelpRequest(client.getPlayer().getId(), type, message);

        GuideManager.getInstance().requestHelp(helpRequest);
    }
}
