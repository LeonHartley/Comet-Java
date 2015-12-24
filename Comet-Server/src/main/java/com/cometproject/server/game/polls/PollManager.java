package com.cometproject.server.game.polls;

import com.cometproject.server.game.polls.types.Poll;
import com.cometproject.server.game.polls.types.PollQuestion;
import com.cometproject.server.game.polls.types.PollQuestionType;
import com.cometproject.server.game.polls.types.questions.MultipleChoiceQuestion;
import com.cometproject.server.game.polls.types.questions.WordedPollQuestion;
import com.cometproject.server.utilities.Initializable;
import com.google.common.collect.Lists;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PollManager implements Initializable {
    private static PollManager pollManagerInstance;

    private final Map<Integer, Poll> polls;

    public PollManager() {
        this.polls = new ConcurrentHashMap<>();
    }

    @Override
    public void initialize() {
        this.polls.put(16, new Poll(1, 16, "Leon's poll", Lists.newArrayList(
                new WordedPollQuestion("What do you think of Comet?"),
                new MultipleChoiceQuestion("If you could add 1 feature, what would you add?", Lists.newArrayList("Horses", "More wired!!!", "Space lasers", "New renderer!!!!!!!!!")))));
    }

    public boolean roomHasPoll(int roomId) {
        return this.polls.containsKey(roomId);
    }

    public Poll getPoll(int roomId) {
        return this.polls.get(roomId);
    }

    public Map<Integer, Poll> getPolls() {
        return polls;
    }

    public static PollManager getInstance() {
        if(pollManagerInstance == null) {
            pollManagerInstance = new PollManager();
        }

        return pollManagerInstance;
    }
}
