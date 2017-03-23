package com.cometproject.server.storage.queries.polls;

import com.cometproject.server.game.polls.types.Poll;
import com.cometproject.server.game.polls.types.PollQuestionType;
import com.cometproject.server.game.polls.types.questions.MultipleChoiceQuestion;
import com.cometproject.server.game.polls.types.questions.SingleChoiceQuestion;
import com.cometproject.server.game.polls.types.questions.WordedPollQuestion;
import com.cometproject.server.storage.SqlHelper;
import com.google.common.collect.Maps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class PollDao {
    public static Map<Integer, Poll> getAllPolls() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, Poll> data = Maps.newConcurrentMap();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM polls;", sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int pollId = resultSet.getInt("id");
                final String title = resultSet.getString("title");
                final int roomId = resultSet.getInt("room_id");
                final String thanksMessage = resultSet.getString("thanks_message");

                final String rewardBadge = resultSet.getString("reward_badge");
                final int rewardCredits = resultSet.getInt("reward_credits");
                final int rewardActivityPoints = resultSet.getInt("reward_activity_points");
                final int rewardDiamonds = resultSet.getInt("reward_vip_points");
                final int rewardAchievementPoints = resultSet.getInt("reward_achievement_points");

                data.put(pollId, new Poll(pollId, roomId, title, thanksMessage, rewardBadge, rewardCredits, rewardDiamonds, rewardActivityPoints, rewardAchievementPoints));
            }

            // close the stuff cos we gonna create new ones
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);

            preparedStatement = SqlHelper.prepare("SELECT * FROM polls_questions", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final int pollId = resultSet.getInt("poll_id");

                if (!data.containsKey(pollId)) {
                    continue;
                }

                final Poll poll = data.get(pollId);

                PollQuestionType questionType = PollQuestionType.valueOf(resultSet.getString("question_type"));

                switch (questionType) {
                    default:
                    case WORDED:
                        poll.addQuestion(resultSet.getInt("id"), new WordedPollQuestion(resultSet.getString("question")));
                        break;

                    case MULTIPLE_CHOICE:
                        poll.addQuestion(resultSet.getInt("id"), new MultipleChoiceQuestion(resultSet.getString("question"), resultSet.getString("options")));
                        break;

                    case SINGLE_CHOICE:
                        poll.addQuestion(resultSet.getInt("id"), new SingleChoiceQuestion(resultSet.getString("question"), resultSet.getString("options")));
                        break;
                }

            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return data;
    }

    public static void saveAnswer(int playerId, int pollId, int questionId, String answer) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT into polls_answers (player_id, poll_id, question_id, answer) VALUES(?, ?, ?, ?);", sqlConnection);

            preparedStatement.setInt(1, playerId);
            preparedStatement.setInt(2, pollId);
            preparedStatement.setInt(3, questionId);
            preparedStatement.setString(4, answer);

            preparedStatement.execute();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static boolean hasAnswered(int playerId, int pollId, int questionId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            String query = "SELECT NULL FROM polls_answers WHERE question_id = ? AND poll_id = ? AND player_id = ?;";
            preparedStatement = SqlHelper.prepare(query, sqlConnection);

            preparedStatement.setInt(1, questionId);
            preparedStatement.setInt(2, pollId);
            preparedStatement.setInt(3, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return false;
    }

    public static boolean hasAnswered(int playerId, int pollId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            String query = "SELECT NULL FROM polls_answers WHERE poll_id = ? AND player_id = ?;";
            preparedStatement = SqlHelper.prepare(query, sqlConnection);

            preparedStatement.setInt(1, pollId);
            preparedStatement.setInt(2, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return false;
    }
}
