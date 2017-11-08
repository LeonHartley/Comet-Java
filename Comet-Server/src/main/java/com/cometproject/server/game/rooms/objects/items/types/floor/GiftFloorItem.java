package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.api.game.catalog.ICatalogService;
import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerAvatar;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.RemoveFloorItemMessageComposer;
import com.cometproject.server.utilities.JsonUtil;


public class GiftFloorItem extends RoomItemFloor {
    private GiftData giftData;
    private boolean isOpened = false;

    public GiftFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) throws Exception {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);

        this.giftData = JsonUtil.getInstance().fromJson(data.split("GIFT::##")[1], GiftData.class);

        if (!ICatalogService.getInstance().getGiftBoxesNew().contains(giftData.getSpriteId()) && !ICatalogService.getInstance().getGiftBoxesOld().contains(giftData.getSpriteId())) {
            throw new Exception("some sad fucker used an exploit, bye bye gift.");
        }
    }

    @Override
    public void composeItemData(IComposer msg) {
        final GiftData giftData = this.getGiftData();
        final PlayerAvatar purchaser = PlayerManager.getInstance().getAvatarByPlayerId(giftData.getSenderId(), PlayerAvatar.USERNAME_FIGURE);

        msg.writeInt(giftData.getWrappingPaper() * 1000 + giftData.getDecorationType());
        msg.writeInt(1);
        msg.writeInt(6);
        msg.writeString("EXTRA_PARAM");
        msg.writeString("");
        msg.writeString("MESSAGE");
        msg.writeString(giftData.getMessage());
        msg.writeString("PURCHASER_NAME");
        msg.writeString(purchaser.getUsername());
        msg.writeString("PURCHASER_FIGURE");
        msg.writeString(purchaser.getFigure());
        msg.writeString("PRODUCT_CODE");
        msg.writeString("");
        msg.writeString("state");
        msg.writeString(this.isOpened() ? "1" : "0");
    }

    @Override
    public boolean onInteract(RoomEntity entity, int state, boolean isWiredTrigger) {
        this.isOpened = true;

        this.sendUpdate();
        this.getRoom().getEntities().broadcastMessage(new RemoveFloorItemMessageComposer(this.getVirtualId(), this.ownerId, 1200));
//        this.getRoom().getEntities().broadcastMessage(new SendFloorItemMessageComposer(this));

        this.isOpened = false;
        return true;
    }

    public GiftData getGiftData() {
        return giftData;
    }

    public boolean isOpened() {
        return isOpened;
    }
}
