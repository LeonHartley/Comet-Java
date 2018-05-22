package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.furniture.types.CrackableReward;
import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import org.apache.commons.lang.NumberUtils;

public class CrackableFloorItem extends RoomItemFloor {

    public CrackableFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);

        if (!NumberUtils.isNumber(this.getItemData().getData()))
            this.getItemData().setData("0");
    }

    @Override
    public boolean onInteract(RoomEntity entity, int state, boolean isWiredTrigger) {
        final CrackableReward crackableReward = ItemManager.getInstance().getCrackableRewards().get(this.getItemData().getItemId());

        if (crackableReward == null) {
            return false;
        }

        if (isWiredTrigger || !(entity instanceof PlayerEntity)) {
            return false;
        }

        final Player player = ((PlayerEntity) entity).getPlayer();

        int hits = Integer.parseInt(this.getItemData().getData());
        int maxHits = crackableReward.getHitRequirement();

        if (hits <= maxHits) {
            hits++;
        } else {
            // we're open!
            switch (crackableReward.getRewardType()) {
                case ITEM:
                    // we need to turn into this item!
                    final FurnitureDefinition itemDefinition = ItemManager.getInstance().getDefinition(crackableReward.getRewardDataInt());

                    if (itemDefinition != null) {
                        this.getRoom().getItems().removeItem(this, player.getSession(), false);

                        this.getRoom().getItems().placeFloorItem(new InventoryItem(this.getId(), itemDefinition.getId(), crackableReward.getRewardData()), this.getPosition().getX(), this.getPosition().getY(), this.getRotation(), player);
                    }
                    break;

                case COINS:
                    player.getData().increaseCredits(crackableReward.getRewardDataInt());
                    player.sendBalance();
                    player.getData().save();
                    break;

                case VIP_POINTS:
                    player.getData().increaseVipPoints(crackableReward.getRewardDataInt());
                    player.sendBalance();
                    player.getData().save();
                    break;

                case ACTIVITY_POINTS:
                    player.getData().increaseActivityPoints(crackableReward.getRewardDataInt());
                    player.sendBalance();
                    player.getData().save();
                    break;

                case BADGE:
                    player.getInventory().addBadge(crackableReward.getRewardData(), true, true);
            }
        }

        this.getItemData().setData(hits);
        this.sendUpdate();

        return true;
    }

    @Override
    public void composeItemData(IComposer msg) {
        msg.writeInt(0);
        msg.writeInt(7);

        int state = Integer.parseInt(this.getItemData().getData());
        final CrackableReward crackableReward = ItemManager.getInstance().getCrackableRewards().get(this.getItemData().getItemId());

        if (crackableReward != null) {
            msg.writeString(this.calculateState(crackableReward.getHitRequirement(), state));
            msg.writeInt(state);//state
            msg.writeInt(crackableReward.getHitRequirement());//max
        } else {
            msg.writeString(this.calculateState(20, state));
            msg.writeInt(state);//state
            msg.writeInt(20);//max
        }
    }

    private int calculateState(int maxHits, int currentHits) {
        return (int) Math.floor((1.0D / ((double) maxHits / (double) currentHits) * 14.0D));
    }
}
