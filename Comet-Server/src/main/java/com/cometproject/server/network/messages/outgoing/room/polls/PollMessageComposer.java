package com.cometproject.server.network.messages.outgoing.room.polls;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.polls.types.Poll;
import com.cometproject.server.game.polls.types.PollQuestion;
import com.cometproject.server.game.polls.types.questions.MultipleChoiceQuestion;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.Map;

public class PollMessageComposer extends MessageComposer {

    private final Poll poll;

    public PollMessageComposer(final Poll poll) {
        this.poll = poll;
    }

    @Override
    public short getId() {
        return Composers.PollMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.poll.getPollId());
        msg.writeString(this.poll.getPollTitle());
        msg.writeString(this.poll.getThanksMessage());
        msg.writeInt(this.poll.getPollQuestions().size());

        for (Map.Entry<Integer, PollQuestion> pollQuestion : this.poll.getPollQuestions().entrySet()) {
            msg.writeInt(pollQuestion.getKey());
            msg.writeInt(0);
            msg.writeInt(pollQuestion.getValue().getType());
            msg.writeString(pollQuestion.getValue().getQuestion());
            msg.writeInt(0);

            final int minimumSelections = pollQuestion.getValue() instanceof MultipleChoiceQuestion ? 1 : 0;
            final int optionSizes = pollQuestion.getValue() instanceof MultipleChoiceQuestion ? ((MultipleChoiceQuestion) pollQuestion.getValue()).getChoices().size() : 0;

            msg.writeInt(minimumSelections);
            msg.writeInt(optionSizes);

            if (optionSizes != 0) {
                for (int i = 0; i < optionSizes; i++) {
                    String choice = ((MultipleChoiceQuestion) pollQuestion.getValue()).getChoices().get(i);

                    msg.writeString(i + "");
                    msg.writeString(choice);
                    msg.writeInt(i);
                }
            }

            msg.writeInt(0);
        }

        msg.writeBoolean(true);
    }
}
