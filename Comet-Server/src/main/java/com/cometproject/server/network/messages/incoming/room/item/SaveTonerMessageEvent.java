package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.data.BackgroundTonerData;
import com.cometproject.server.game.rooms.objects.items.types.floor.BackgroundTonerFloorItem;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class SaveTonerMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int virtualId = msg.readInt();

        long itemId = ItemManager.getInstance().getItemIdByVirtualId(virtualId);

        int hue = msg.readInt();
        int saturation = msg.readInt();
        int lightness = msg.readInt();

        RoomItemFloor item = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(itemId);

        if (item == null || !client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId()) && !client.getPlayer().getPermissions().getRank().roomFullControl() || !(item instanceof BackgroundTonerFloorItem)) {
            return;
        }

        BackgroundTonerData data = new BackgroundTonerData(hue, saturation, lightness);
        String stringData = BackgroundTonerData.get(data);

        item.getItemData().setData(stringData);

        item.getRoom().getEntities().broadcastMessage(new UpdateFloorItemMessageComposer(item));
        item.saveData();
    }
}
