package com.cometproject.server.game.commands.development;

import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class ItemVirtualIdCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length == 0) {
            client.send(new AlertMessageComposer("There are currently " + ItemManager.getInstance().getItemIdToVirtualIds().size() + " item virtual IDs in memory."));
            return;
        }

        try {
            final int virtualId = Integer.parseInt(params[0]);
            final RoomItem roomItem = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(virtualId);

            client.send(new AlertMessageComposer("Virtual ID: " + virtualId + "\nReal ID: " + ItemManager.getInstance().getItemIdByVirtualId(virtualId) + (roomItem != null ? "\nBase ID: " + roomItem.getDefinition().getId() : "")));
        } catch (Exception e) {

        }
    }

    @Override
    public String getPermission() {
        return "dev";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
