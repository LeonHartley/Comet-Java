package com.cometproject.server.network.messages.outgoing.navigator.updated;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.types.PlayerSettings;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class NavigatorPreferencesMessageComposer extends MessageComposer {

    private final PlayerSettings playerSettings;

    public NavigatorPreferencesMessageComposer(final PlayerSettings playerSettings) {
        this.playerSettings = playerSettings;
    }

    @Override
    public short getId() {
        return Composers.NavigatorPreferencesMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.playerSettings.getNavigatorX());
        msg.writeInt(this.playerSettings.getNavigatorY());
        msg.writeInt(this.playerSettings.getNavigatorWidth());
        msg.writeInt(this.playerSettings.getNavigatorHeight());
        msg.writeBoolean(this.playerSettings.getNavigatorShowSearches());

        msg.writeInt(0);//?
    }
}
