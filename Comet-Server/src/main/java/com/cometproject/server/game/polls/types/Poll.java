package com.cometproject.server.game.polls.types;

import java.util.List;

public class Poll {
    private int pollId;
    private int roomId;

    private String pollTitle;

    private List<PollQuestion> pollQuestions;

    public Poll(int pollId, int roomId, String pollTitle, List<PollQuestion> pollQuestions) {
        this.pollId = pollId;
        this.roomId = roomId;
        this.pollTitle = pollTitle;
        this.pollQuestions = pollQuestions;
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

    public List<PollQuestion> getPollQuestions() {
        return pollQuestions;
    }

    public void setPollQuestions(List<PollQuestion> pollQuestions) {
        this.pollQuestions = pollQuestions;
    }
}
