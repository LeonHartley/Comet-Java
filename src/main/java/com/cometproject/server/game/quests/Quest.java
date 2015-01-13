package com.cometproject.server.game.quests;

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
