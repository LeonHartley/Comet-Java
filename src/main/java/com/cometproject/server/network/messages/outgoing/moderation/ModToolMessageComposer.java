package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.moderation.types.actions.ActionCategory;
import com.cometproject.server.game.moderation.types.actions.ActionPreset;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class ModToolMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.LoadModerationToolMessageComposer);

        msg.writeInt(1); // issues

        {
            msg.writeInt(1);//id
            msg.writeInt(1);//state
            msg.writeInt(1);//cat
            msg.writeInt(1);
            msg.writeInt(11);
            msg.writeInt(1);
            msg.writeInt(0);
            msg.writeInt(2);
            msg.writeString("Entity");
            msg.writeInt(1);
            msg.writeString("Leon");
            msg.writeInt(3);
            msg.writeString("MOD-Leon");
            msg.writeString("Help me help me he touched my bottom");
            msg.writeInt(1);
            msg.writeInt(0);//chatmsg???
        }

        msg.writeInt(ModerationManager.getInstance().getUserPresets().size());

        for (String preset : ModerationManager.getInstance().getUserPresets()) {
            msg.writeString(preset);
        }

        msg.writeInt(ModerationManager.getInstance().getActionCategories().size());

        for (ActionCategory actionCategory : ModerationManager.getInstance().getActionCategories()) {
            msg.writeString(actionCategory.getCategoryName());
            msg.writeBoolean(false); // unused bool
            msg.writeInt(actionCategory.getPresets().size());

            for (ActionPreset preset : actionCategory.getPresets()) {
                msg.writeString(preset.getName());
                msg.writeString(preset.getMessage());
                msg.writeInt(preset.getBanLength());
                msg.writeInt(preset.getAvatarBanLength());
                msg.writeInt(preset.getMuteLength());
                msg.writeInt(preset.getTradeLockLength());
                msg.writeString(preset.getDescription());
                msg.writeBoolean(false); // show habbo way
            }
        }

        // Fuses
        msg.writeBoolean(true); // tickets
        msg.writeBoolean(true); // chatlog
        msg.writeBoolean(true); // message, caution, user info
        msg.writeBoolean(true); // kick fuse / user info
        msg.writeBoolean(true); // ban
        msg.writeBoolean(true); // room alert
        msg.writeBoolean(true); // room kick

        msg.writeInt(ModerationManager.getInstance().getRoomPresets().size());

        for (String preset : ModerationManager.getInstance().getRoomPresets()) {
            msg.writeString(preset);
        }

        msg.writeInt(0);


        return msg;
    }
}
