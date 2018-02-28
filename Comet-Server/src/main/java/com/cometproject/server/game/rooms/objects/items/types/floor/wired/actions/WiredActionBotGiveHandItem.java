package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;

public class WiredActionBotGiveHandItem extends WiredActionItem {
    private final static int PARAM_HANDITEM = 0;

    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param room     The instance of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredActionBotGiveHandItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 24;
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        if (this.getWiredData().getParams().size() != 1) {
            return;
        }

        if (this.getWiredData().getText().isEmpty()) {
            return;
        }

        if (event.entity == null || !(event.entity instanceof PlayerEntity)) return;

        int param = this.getWiredData().getParams().get(PARAM_HANDITEM);

        final String botName = this.getWiredData().getText();
        final BotEntity botEntity = this.getRoom().getBots().getBotByName(botName);

        if (botEntity != null) {
            this.getRoom().getEntities().broadcastMessage(new TalkMessageComposer(botEntity.getId(), Locale.get("bots.chat.giveItemMessage").replace("%username%", event.entity.getUsername()), RoomManager.getInstance().getEmotions().getEmotion(":)"), 2));
            event.entity.carryItem(param);
        }
    }
}
