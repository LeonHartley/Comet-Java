package com.cometproject.server.game.polls.types;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Poll {
    private final int pollId;
    private final int roomId;

    private final String pollTitle;
    private final String thanksMessage;

    private final String rewardBadge;
    private final int rewardCredits;
    private final int rewardActivityPoints;
    private final int rewardVipPoints;
    private final int rewardAchievementPoints;

    private final Map<Integer, PollQuestion> pollQuestions;

    private final Set<Integer> playersAnswered;

    public Poll(int pollId, int roomId, String pollTitle, String thanksMessage, String rewardBadge, int rewardCredits, int rewardVipPoints, int rewardActivityPoints, int rewardAchievementPoints) {
        this.pollId = pollId;
        this.roomId = roomId;
        this.pollTitle = pollTitle;
        this.thanksMessage = thanksMessage;

        this.rewardBadge = rewardBadge;
        this.rewardCredits = rewardCredits;
        this.rewardVipPoints = rewardVipPoints;
        this.rewardAchievementPoints = rewardAchievementPoints;
        this.rewardActivityPoints = rewardActivityPoints;

        this.pollQuestions = new ConcurrentHashMap<>();
        this.playersAnswered = new HashSet<>();
    }

    public void onPlayerFinishedPoll(Player player) {
        this.getPlayersAnswered().add(player.getId());

        if (this.getRewardBadge() != null && !this.getRewardBadge().isEmpty()) {
            player.getInventory().addBadge(this.getRewardBadge(), true, true);
        }

        boolean save = false;

        if (this.rewardCredits != 0) {
            player.getData().increaseCredits(this.rewardCredits);
            player.getSession().send(new AlertMessageComposer(
                    Locale.getOrDefault("wired.reward.coins", "You received %s coin(s)!").replace("%s", this.rewardCredits + "")));

            player.getSession().send(player.composeCurrenciesBalance());

            save = true;
        }

        if (this.rewardActivityPoints != 0) {
            player.getData().increaseActivityPoints(this.rewardActivityPoints);
            player.getSession().send(new AlertMessageComposer(
                    Locale.getOrDefault("wired.reward.duckets", "You received %s ducket(s)!").replace("%s", this.rewardActivityPoints + "")));

            player.getSession().send(player.composeCurrenciesBalance());

            save = true;
        }

        if (this.rewardAchievementPoints != 0) {
            // reward achievements points
            player.getData().increaseAchievementPoints(this.rewardAchievementPoints);

            save = true;
        }

        if (this.rewardVipPoints != 0) {
            player.getData().increaseVipPoints(this.rewardVipPoints);
            player.getSession().send(player.composeCurrenciesBalance());

            player.getSession().send(new AlertMessageComposer(
                    Locale.getOrDefault("wired.reward.diamonds", "You received %s diamonds(s)!").replace("%s", this.rewardVipPoints + "")));
            save = true;
        }

        if (save) {
            player.getData().save();
        }
    }

    public void addQuestion(int questionId, PollQuestion pollQuestion) {
        this.pollQuestions.put(questionId, pollQuestion);
    }

    public boolean isFinalQuestion(final int questionId) {
        int index = 0;
        int count = this.pollQuestions.size();

        for (Map.Entry<Integer, PollQuestion> pollQuestion : this.pollQuestions.entrySet()) {
            index++;

            if (pollQuestion.getKey() == questionId && index >= count) {
                return true;
            }
        }

        return false;
    }

    public int getPollId() {
        return pollId;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getPollTitle() {
        return pollTitle;
    }

    public Map<Integer, PollQuestion> getPollQuestions() {
        return pollQuestions;
    }

    public String getThanksMessage() {
        return thanksMessage;
    }

    public Set<Integer> getPlayersAnswered() {
        return playersAnswered;
    }

    public String getRewardBadge() {
        return rewardBadge;
    }
}
