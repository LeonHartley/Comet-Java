package com.cometproject.server.game.groups.types.components.forum.threads;

import java.util.Map;

public class ForumThread {
    private int groupId;
    private int threadId;

    private int author;
    private String authorUsername; // broombroom much fast very speed.

    private String threadTitle;
    private String threadMessage;
    private int timePosted;

    private Map<Integer, ForumThreadMessage> threadReplies;
}
