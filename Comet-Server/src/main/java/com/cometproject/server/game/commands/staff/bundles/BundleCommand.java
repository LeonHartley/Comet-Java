package com.cometproject.server.game.commands.staff.bundles;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.commands.staff.TeleportCommand;
import com.cometproject.server.game.rooms.bundles.RoomBundleManager;
import com.cometproject.server.game.rooms.bundles.types.RoomBundle;
import com.cometproject.server.game.rooms.bundles.types.RoomBundleConfig;
import com.cometproject.server.game.rooms.bundles.types.RoomBundleItem;
import com.cometproject.server.game.rooms.models.CustomFloorMapData;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.objects.items.types.floor.SoundMachineFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.TeleporterFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredFloorItem;
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

                CustomFloorMapData modelData = new CustomFloorMapData(
                        room.getModel().getDoorX(), room.getModel().getDoorY(),
                        room.getModel().getDoorRotation(), room.getModel().getMap(), room.getModel().getWallHeight());

                List<RoomBundleItem> bundleItems = new ArrayList<>();

                for (RoomItemFloor floorItem : room.getItems().getFloorItems().values()) {
                    if (floorItem instanceof SoundMachineFloorItem || floorItem instanceof TeleporterFloorItem || floorItem instanceof WiredFloorItem) {
                        continue;
                    }

                    bundleItems.add(new RoomBundleItem(floorItem.getItemId(),
                            floorItem.getPosition().getX(), floorItem.getPosition().getY(),
                            floorItem.getPosition().getZ(), floorItem.getRotation(), null,
                            floorItem.getDataObject()
                    ));
                }

                for (RoomItemWall wallItem : room.getItems().getWallItems().values()) {
                    bundleItems.add(new RoomBundleItem(wallItem.getItemId(),
                            -1, -1, -1, -1, wallItem.getWallPosition(),
                            wallItem.getExtraData()
                    ));
                }

                RoomBundle roomBundle = new RoomBundle(-1, room.getId(), alias, modelData, bundleItems, 20, 0, 0, new RoomBundleConfig("%username%'s new room", room.getData().getDecorationString(), room.getData().getWallThickness(), room.getData().getFloorThickness(), room.getData().getHideWalls()));
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
