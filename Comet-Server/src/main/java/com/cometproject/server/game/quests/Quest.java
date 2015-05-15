package com.cometproject.server.game.quests;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.types.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Quest {
    private final int id;
    private final String name;
    private final String category;
    private final int seriesNumber;

    private final int goalType;
    private final int goalData;

    private final int reward;
    private final String dataBit;

    private final QuestType questType;

    public Quest(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.name = data.getString("name");
        this.category = data.getString("category");
        this.seriesNumber = data.getInt("series_number");
        this.goalType = data.getInt("goal_type");
        this.goalData = data.getInt("goal_data");

        this.reward = data.getInt("reward");
        this.dataBit = data.getString("data_bit");

        this.questType = QuestType.getById(this.goalType);
    }
    
    public void compose(Player player, IComposer msg) {
        msg.writeString(this.getCategory());
        msg.writeInt(this.getSeriesNumber() - 1);
        msg.writeInt(QuestManager.getInstance().amountOfQuestsInCategory(this.getCategory()) - 1);
        msg.writeInt(3); // reward type (pixels)
        msg.writeInt(this.getId());
        msg.writeBoolean(player.getQuests().hasStartedQuest(this.getId())); // started
        msg.writeString(this.getType().getAction());
        msg.writeString(this.getDataBit());
        msg.writeInt(0);//reward
        msg.writeString(this.getName());
        msg.writeInt(player.getQuests().getProgress(this.getId())); // progress
        msg.writeInt(this.getGoalData()); // total steps to get goal
        msg.writeInt(0); // sort order
        msg.writeString("set_kuurna");
        msg.writeString("MAIN_CHAIN");
        msg.writeBoolean(true);// easy
    }

    public QuestType getType() {
        return this.questType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getSeriesNumber() {
        return seriesNumber;
    }

    public int getGoalType() {
        return goalType;
    }

    public int getGoalData() {
        return goalData;
    }

    public int getReward() {
        return reward;
    }

    public String getDataBit() {
        return dataBit;
    }
}
