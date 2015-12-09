package com.cometproject.server.game.commands.staff.bundles;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.bundles.RoomBundleManager;
import com.cometproject.server.game.rooms.bundles.types.RoomBundle;
import com.cometproject.server.game.rooms.bundles.types.RoomBundleItem;
import com.cometproject.server.game.rooms.models.types.DynamicRoomModelData;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.objects.items.types.floor.SoundMachineFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.catalog.CatalogPublishMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.rooms.BundleDao;

import java.util.ArrayList;
import java.util.List;

public class BundleCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2) {
            client.send(new AlertMessageComposer("Use :bundle create [alias] to create a bundle."));
            return;
        }

        String commandName = params[0];

        switch (commandName) {
            case "create": {
                final String alias = params[1];

                Room room = client.getPlayer().getEntity().getRoom();

                DynamicRoomModelData modelData = new DynamicRoomModelData(
                        room.getModel().getId(), room.getModel().getMap(),
                        room.getModel().getDoorX(), room.getModel().getDoorY(), room.getModel().getDoorZ(),
                        room.getModel().getDoorRotation(), room.getModel().getWallHeight());

                List<RoomBundleItem> bundleItems = new ArrayList<>();

                for (RoomItemFloor floorItem : room.getItems().getFloorItems().values()) {
                    if (floorItem instanceof SoundMachineFloorItem) {
                        continue;
                    }

                    bundleItems.add(new RoomBundleItem(floorItem.getItemId(),
                            floorItem.getPosition().getX(), floorItem.getPosition().getY(),
                            floorItem.getPosition().getZ(), null,
                            floorItem.getDataObject()
                    ));
                }

                for (RoomItemWall wallItem : room.getItems().getWallItems().values()) {
                    bundleItems.add(new RoomBundleItem(wallItem.getItemId(),
                            -1, -1, -1, wallItem.getWallPosition(),
                            wallItem.getExtraData()
                    ));
                }

                RoomBundle roomBundle = new RoomBundle(-1, room.getId(), alias, modelData, bundleItems);
                BundleDao.saveBundle(roomBundle);

                boolean updateCatalog = false;

                if(RoomBundleManager.getInstance().getBundle(alias) != null) {
                    updateCatalog = true;
                }

                RoomBundleManager.getInstance().addBundle(roomBundle);

                if(updateCatalog) {
                    CatalogManager.getInstance().loadItemsAndPages();

                    NetworkManager.getInstance().getSessions().broadcast(new CatalogPublishMessageComposer(true));
                }
                break;
            }

            case "destroy": {

                break;
            }
        }
    }

    @Override
    public String getPermission() {
        return "bundle_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.bundle.description");
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}