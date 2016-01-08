package com.cometproject.server.network.messages.outgoing.user.permissions;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.game.permissions.types.Perk;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.Map;


public class AllowancesMessageComposer extends MessageComposer {
    private final int rank;

    public AllowancesMessageComposer(final int rank) {
        this.rank = rank;
    }

    @Override
    public short getId() {
        return Composers.UserPerksMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        if (rank == -1) {
            msg.writeInt(0);
            return;
        }

        msg.writeInt(PermissionsManager.getInstance().getPerks().size());

        for (Map.Entry<Integer, Perk> perk : PermissionsManager.getInstance().getPerks().entrySet()) {
            msg.writeString(perk.getValue().getTitle());
            msg.writeString(perk.getValue().getData());

            if (perk.getValue().doesOverride()) {
                msg.writeBoolean(perk.getValue().getDefault());
            } else {
                msg.writeBoolean(perk.getValue().getRank() <= rank);
            }
        }
    }
}
