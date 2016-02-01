package com.cometproject.server.game.polls.types;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Poll {
    private int pollId;
    private int roomId;

    private String pollTitle;
    private String thanksMessage;

    private Map<Integer, PollQuestion> pollQuestions;

    public Poll(int pollId, int roomId, String pollTitle, String thanksMessage) {
        this.pollId = pollId;
        this.roomId = roomId;
        this.pollTitle = pollTitle;
        this.thanksMessage = thanksMessage;
        this.pollQuestions = new ConcurrentHashMap<>();
    }

    public void addQuestion(int questionId, PollQuestion pollQuestion) {
        this.pollQuestions.put(questionId, pollQuestion);
    }

    public int getPollId() {
        return pollId;
    }

    public void setPollId(int pollId) {
        this.pollId = pollId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getPollTitle() {
        return pollTitle;
    }

    public void setPollTitle(String pollTitle) {
        this.pollTitle = pollTitle;
    }

    public Map<Integer, PollQuestion> getPollQuestions() {
        return pollQuestions;
    }

    public String getThanksMessage() {
        return thanksMessage;
    }
}
