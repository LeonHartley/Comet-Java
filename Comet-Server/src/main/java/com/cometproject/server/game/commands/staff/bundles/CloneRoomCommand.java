package com.cometproject.server.game.commands.staff.bundles;

import com.cometproject.api.game.rooms.objects.IRoomItemData;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.composers.catalog.BoughtItemMessageComposer;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.bundles.types.RoomBundle;
import com.cometproject.api.game.catalog.types.bundles.RoomBundleItem;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.settings.EnforceRoomCategoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.storage.api.StorageContext;
import com.google.common.collect.Sets;

import java.util.Set;

public class CloneRoomCommand extends ChatCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        final RoomBundle bundle = RoomBundle.create(client.getPlayer().getEntity().getRoom(), "");

        try {
            int roomId = RoomManager.getInstance().createRoom(bundle.getConfig().getRoomName().replace("%username%", client.getPlayer().getData().getUsername()), "", bundle.getRoomModelData(), 0, 20, 0, client, bundle.getConfig().getThicknessWall(), bundle.getConfig().getThicknessFloor(), bundle.getConfig().getDecorations(), bundle.getConfig().isHideWalls());

            final Set<IRoomItemData> itemData = Sets.newHashSet();
            final int playerId = client.getPlayer().getId();

            for (RoomBundleItem roomBundleItem : bundle.getRoomBundleData()) {
                final Position position = roomBundleItem.getWallPosition() == null ? new Position(roomBundleItem.getX(), roomBundleItem.getY(), roomBundleItem.getZ()) : null;

                itemData.add(new RoomItemData(-1, roomBundleItem.getItemId(), playerId, "", position, roomBundleItem.getRotation(), roomBundleItem.getExtraData(), roomBundleItem.getWallPosition(), null));
            }

            sendAlert(Locale.getOrDefault("command.cloneroom.cloned", "The room has been cloned successfully, it may take a few seconds."), client);
            StorageContext.getCurrentContext().getRoomItemRepository().placeBundle(roomId, itemData);

            client.send(new RoomForwardMessageComposer(roomId));
            client.send(new EnforceRoomCategoryMessageComposer());
            client.send(new BoughtItemMessageComposer(BoughtItemMessageComposer.PurchaseType.BADGE));

            client.getPlayer().setLastRoomCreated((int) Comet.getTime());

            this.logDesc = "El staff %s ha hecho cloneroom en la sala '%b'"
                    .replace("%s", client.getPlayer().getData().getUsername())
                    .replace("%b", client.getPlayer().getEntity().getRoom().getData().getName());

        } catch (Exception e) {
            client.send(new MotdNotificationMessageComposer("Invalid room bundle data, please contact an administrator."));
            client.send(new BoughtItemMessageComposer(BoughtItemMessageComposer.PurchaseType.BADGE));
        }
    }

    @Override
    public String getPermission() {
        return "cloneroom_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.cloneroom.description");
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String getLoggableDescription(){
        return this.logDesc;
    }

    @Override
    public boolean isLoggable(){
        return true;
    }
}
