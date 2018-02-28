package com.cometproject.server.game.polls.types.questions;

import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.game.polls.types.PollQuestion;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceQuestion extends PollQuestion {
    private List<String> choices;

    public MultipleChoiceQuestion(String question, String questionData) {
        super(question);

        this.choices = JsonUtil.getInstance().fromJson(questionData, new TypeToken<ArrayList<String>>() {
        }.getType());
    }

    public List<String> getChoices() {
        return this.choices;
    }

    @Override
    public int getType() {
        return 2;
    }
}
