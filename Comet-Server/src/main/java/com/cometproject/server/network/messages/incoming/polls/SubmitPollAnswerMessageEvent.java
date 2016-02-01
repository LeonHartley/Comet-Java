package com.cometproject.server.network.messages.incoming.polls;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.polls.PollManager;
import com.cometproject.server.game.polls.types.Poll;
import com.cometproject.server.game.polls.types.PollQuestion;
import com.cometproject.server.game.polls.types.questions.MultipleChoiceQuestion;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.polls.PollDao;

public class SubmitPollAnswerMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int pollId = msg.readInt();
        int questionId = msg.readInt();
        int count = msg.readInt();

        String answer = msg.readString();

        Poll poll = PollManager.getInstance().getPollbyId(pollId);

        if(poll != null) {
            PollQuestion pollQuestion = poll.getPollQuestions().get(questionId);

            if(pollQuestion == null) {
                return;
            }

            if(pollQuestion instanceof MultipleChoiceQuestion) {
                try {
                    int answerIndex = Integer.parseInt(answer);

                    if(answerIndex < 0 || answerIndex >= ((MultipleChoiceQuestion) pollQuestion).getChoices().size()) {
                        client.send(new AlertMessageComposer(Locale.getOrDefault("polls.invalid.answer", "Invalid answer!")));
                        return;
                    }

                    answer = ((MultipleChoiceQuestion) pollQuestion).getChoices().get(answerIndex);
                } catch(Exception e) {
                    client.send(new AlertMessageComposer(Locale.getOrDefault("polls.invalid.answer", "Invalid answer!")));
                    return;
                }
            }

            if(PollDao.hasAnswered(client.getPlayer().getId(), pollId, questionId)) {
                client.send(new AlertMessageComposer(Locale.getOrDefault("polls.already.answerered", "You've already answered this question!")));
                return;
            }

            PollDao.saveAnswer(client.getPlayer().getId(), pollId, questionId, answer);
        }
    }
}
