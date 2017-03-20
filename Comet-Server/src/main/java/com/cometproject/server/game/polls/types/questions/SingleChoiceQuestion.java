package com.cometproject.server.game.polls.types.questions;

public class SingleChoiceQuestion extends MultipleChoiceQuestion {
    public SingleChoiceQuestion(String question, String questionData) {
        super(question, questionData);
    }

    @Override
    public int getType() {
        return 1;
    }
}
