package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.storage.api.StorageContext;
import com.cometproject.storage.api.data.Data;
import com.cometproject.storage.api.data.rewards.RewardData;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RewardCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        StorageContext.getCurrentContext().getRewardRepository().getActiveRewards((rewards) -> {
            if (params.length == 0) {
                // send alert with list of active rewards
                sendAlert(this.rewardList(client.getPlayer().getId(), rewards), client);
            } else {
                final String reward = params[0];

                if (!rewards.containsKey(reward)) {
                    sendAlert(this.rewardList(client.getPlayer().getId(), rewards), client);
                } else {
                    final RewardData rewardData = rewards.get(reward);

                    final Data<Boolean> received = Data.createEmpty();
                    StorageContext.getCurrentContext().getRewardRepository().playerRedeemedReward(client.getPlayer().getId(),
                            rewardData.getCode(), received::set);

                    if (!received.get()) {
                        client.getPlayer().getData().increaseVipPoints(rewardData.getDiamonds());
                        client.getPlayer().getData().increaseSeasonalPoints(rewardData.getSeasonal());
                        client.getPlayer().getInventory().addBadge(rewardData.getBadge(), false, true);
                        client.getPlayer().sendBalance();

                        StorageContext.getCurrentContext().getRewardRepository().redeemReward(client.getPlayer().getId(),
                                rewardData);

                        sendAlert(Locale.get("command.reward.redeemed").replace("%diamonds%", rewardData.getDiamonds() + "").replace("%badge_id%", rewardData.getBadge()), client);
                    }
                }
            }
        });
    }

    private String rewardList(int playerId, Map<String, RewardData> rewards) {
        final StringBuilder builder = new StringBuilder();

        final AtomicInteger counter = new AtomicInteger(0);

        for (RewardData rewardData : rewards.values()) {
            StorageContext.getCurrentContext().getRewardRepository().playerRedeemedReward(playerId, rewardData.getCode(), (received) -> {
                if (!received) {
                    counter.incrementAndGet();

                    builder.append("- " + rewardData.getCode() + "\n");
                }
            });
        }

        if (counter.get() == 0) {
            return Locale.get("command.reward.none");
        }

        return builder.toString();
    }

    @Override
    public String getPermission() {
        return "reward_command";
    }

    @Override
    public String getParameter() {
        return "%reward%";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.reward.description");
    }
}
