package com.cometproject.server.network.messages.outgoing.user.permissions;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.game.permissions.types.Perk;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.Map;


public class AllowancesMessageComposer extends MessageComposer {
    private final int rank;

    public AllowancesMessageComposer(final int rank) {
        this.rank = rank;
    }

    @Override
    public short getId() {
        return Composers.PerkAllowancesMessageComposer;
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
            msg.writeString((perk.getValue().getRank() <= rank) ? "" : perk.getValue().getData());

            if (perk.getValue().doesOverride()) {
                msg.writeBoolean(perk.getValue().getDefault());
            } else {
                msg.writeBoolean(perk.getValue().getRank() <= rank);
            }
        }
    //    msg.writeInt(16); // Count
    //    msg.writeString("USE_GUIDE_TOOL");
    //    msg.writeString("");
    //    msg.writeBoolean(false);
    //    msg.writeString("GIVE_GUIdE_TOURS");
    //    msg.writeString("requirement.unfulfilled.helper_le");
    //    msg.writeBoolean(false);
    //    msg.writeString("JUDGE_CHAT_REVIEWS");
    //    msg.writeString(""); // ??
    //    msg.writeBoolean(true);
    //    msg.writeString("VOTE_IN_COMPETITIONS");
    //    msg.writeString(""); // ??
    //    msg.writeBoolean(true);
    //    msg.writeString("CALL_ON_HELPERS");
    //    msg.writeString(""); // ??
    //    msg.writeBoolean(false);
    //    msg.writeString("CITIZEN");
    //    msg.writeString(""); // ??
    //    msg.writeBoolean(true);
    //    msg.writeString("TRADE");
    //    msg.writeString(""); // ??
    //    msg.writeBoolean(true);
    //    msg.writeString("HEIGHTMAP_EDITOR_BETA");
    //    msg.writeString(""); // ??
    //    msg.writeBoolean(false);
    //    msg.writeString("EXPERIMENTAL_CHAT_BETA");
    //    msg.writeString("requirement.unfulfilled.helper_level_2");
    //    msg.writeBoolean(true);
    //    msg.writeString("EXPERIMENTAL_TOOLBAR");
    //    msg.writeString(""); // ??
    //    msg.writeBoolean(true);
    //    msg.writeString("BUILDER_AT_WORK");
    //    msg.writeString(""); // ??
    //    msg.writeBoolean(true);
    //    msg.writeString("NAVIGATOR_PHASE_ONE_2014");
    //    msg.writeString(""); // ??
    //    msg.writeBoolean(false);
    //    msg.writeString("CAMERA");
    //    msg.writeString(""); // ??
    //    msg.writeBoolean(true);
    //    msg.writeString("NAVIGATOR_PHASE_TWO_2014");
    //    msg.writeString(""); // ??
    //    msg.writeBoolean(true);
    //    msg.writeString("MOUSE_ZOOM");
    //    msg.writeString(""); // ??
    //    msg.writeBoolean(true);
    //    msg.writeString("NAVIGATOR_ROOM_THUMBNAIL_CAMERA");
    //    msg.writeString(""); // ??
    //    msg.writeBoolean(true);
    }
}
