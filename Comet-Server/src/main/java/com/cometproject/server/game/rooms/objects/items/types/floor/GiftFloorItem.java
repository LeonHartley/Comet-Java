package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.catalog.types.ICatalogItem;
import com.cometproject.api.game.catalog.types.ICatalogPage;
import com.cometproject.api.game.furniture.types.GiftData;
import com.cometproject.api.game.furniture.types.LegacyGiftData;
import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.storage.api.StorageContext;

import java.util.HashMap;


public class GiftFloorItem extends RoomItemFloor {
    private final PlayerAvatar playerAvatar;
    private GiftData giftData;
    private boolean isOpened = false;

    public GiftFloorItem(RoomItemData roomItemData, Room room) throws Exception {
        super(roomItemData, room);

        this.giftData = this.loadGiftData(roomItemData.getData());
        if (!CatalogManager.getInstance().getGiftBoxesNew().contains(giftData.getSpriteId()) && !CatalogManager.getInstance().getGiftBoxesOld().contains(giftData.getSpriteId())) {
            throw new Exception("some sad fucker used an exploit, bye bye gift.");
        }

        if (this.giftData.showUsername() && giftData.getSenderId() != 0) {
            playerAvatar = PlayerManager.getInstance().getAvatarByPlayerId(giftData.getSenderId(), PlayerAvatar.USERNAME_FIGURE);
        } else {
            playerAvatar = null;
        }
    }

    private GiftData loadGiftData(String extraData) throws Exception {
        // Convert old gift data (catalogPageId, catalogItemId) to new gift data (itemDefinitionId)
        if (extraData.startsWith(LegacyGiftData.EXTRA_DATA_HEADER)) {
            final LegacyGiftData legacyGiftData = JsonUtil.getInstance().fromJson(extraData.split(LegacyGiftData.EXTRA_DATA_HEADER)[1], LegacyGiftData.class);
            final ICatalogItem catalogItem = CatalogManager.getInstance().getCatalogItem(legacyGiftData.getItemId());

            if (catalogItem == null) {
                throw new Exception(String.format("catalog item no longer exists, gift %s is invalid", this.getId()));
            }

            GiftData newGiftData = legacyGiftData.upgrade(catalogItem.getItems().get(0).getItemId());
            this.getItemData().setData(String.format("%s%s", GiftData.EXTRA_DATA_HEADER, JsonUtil.getInstance().toJson(newGiftData)));

            this.saveData();
            return newGiftData;
        }

        return JsonUtil.getInstance().fromJson(extraData.split(GiftData.EXTRA_DATA_HEADER)[1], GiftData.class);
    }

    @Override
    public void composeItemData(IComposer msg) {
        final GiftData giftData = this.getGiftData();
        final HashMap<String, String> itemData = this.getData();

        msg.writeInt(giftData.getWrappingPaper() * 1000 + giftData.getDecorationType());
        msg.writeInt(1);
        msg.writeInt(itemData.size());

        itemData.forEach((key, value) -> {
            msg.writeString(key);
            msg.writeString(value);
        });
    }

    private HashMap<String, String> getData() {
        final GiftData giftData = this.getGiftData();
        final boolean isGiftOpen = this.isOpened;

        final HashMap<String, String> data = new HashMap<String, String>() {{
            put("EXTRA_PARAM", "");
            put("MESSAGE", giftData.getMessage());
            put("PRODUCT_CODE", "");
            put("state", isGiftOpen ? "1" : "0");
        }};

        if (giftData.showUsername() && this.playerAvatar != null) {
            data.put("PURCHASER_NAME", this.playerAvatar.getUsername());
            data.put("PURCHASER_FIGURE", this.playerAvatar.getFigure());
        } else if (giftData.showUsername() && this.giftData.getSenderName() != null) {
            data.put("PURCHASER_NAME", this.giftData.getSenderName());
        }

        return data;
    }

    @Override
    public boolean onInteract(RoomEntity entity, int state, boolean isWiredTrigger) {
        this.isOpened = true;

        this.sendUpdate();
//        this.getRoom().getEntities().broadcastMessage(new RemoveFloorItemMessageComposer(this.getVirtualId(), this.getItemData().getOwnerId(), 1200));
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
