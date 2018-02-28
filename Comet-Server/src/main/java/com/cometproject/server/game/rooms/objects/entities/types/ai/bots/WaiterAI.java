package com.cometproject.server.game.rooms.objects.entities.types.ai.bots;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.entities.types.ai.AbstractBotAI;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.FloodFilterMessageComposer;

public class WaiterAI extends AbstractBotAI {
    public static final Drink[] drinks = {
            new Drink("tea", 1),
            new Drink("juice", 2),
            new Drink("milk", 5),
            new Drink("coffee", 8),
            new Drink("decaff", 9),
            new Drink("espresso", 13),
            new Drink("mocha", 11),
            new Drink("iced_coffee", 15),
            new Drink("cappuccino", 16),
            new Drink("java", 17),
            new Drink("coke", 19),
            new Drink("cola", 19),
            new Drink("bubble", 24),
            new Drink("potion", 25),
            new Drink("pink_champagne", 35)
    };

    public WaiterAI(RoomEntity entity) {
        super(entity);
    }

    @Override
    public boolean onTalk(PlayerEntity entity, String message) {

        String triggerMessage = message.toLowerCase();

        for (Drink drink : drinks) {
            if (triggerMessage.contains(Locale.get("drink." + drink.getTrigger()))) {
                if (entity.getPlayer().getRoomFloodTime() >= 1) {
                    entity.getPlayer().getSession().send(new FloodFilterMessageComposer(entity.getPlayer().getRoomFloodTime()));
                    return false;
                }

                if (entity.getPosition().distanceTo(this.getEntity().getPosition()) >= 4) {
                    this.getEntity().getRoom().getEntities().broadcastMessage(new TalkMessageComposer(this.getEntity().getId(), Locale.get("bots.chat.tooFar").replace("%username%", entity.getUsername()), RoomManager.getInstance().getEmotions().getEmotion(":("), 2));
                    return false;
                }

                entity.carryItem(drink.getHandItemId());

                this.getEntity().getRoom().getEntities().broadcastMessage(new TalkMessageComposer(this.getEntity().getId(), Locale.get("bots.chat.giveItemMessage").replace("%username%", entity.getUsername()), RoomManager.getInstance().getEmotions().getEmotion(":)"), 2));
                return true;
            }
        }

        return false;
    }

    public static class Drink {
        private String trigger;
        private int handItemId;

        private Drink(String trigger, int handItemId) {
            this.trigger = trigger;
            this.handItemId = handItemId;
        }

        public String getTrigger() {
            return trigger;
        }

        public int getHandItemId() {
            return handItemId;
        }
    }
}
