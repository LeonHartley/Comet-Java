package com.cometproject.server.game.groups.types.components.forum.threads;

import java.util.Map;

public class ForumThread {
    private int groupId;
    private int threadId;

    private int author;
    private String authorUsername; // broom broom much fast very speed.

    private String threadTitle;
    private String threadMessage;
    private int timePosted;

    private Map<Integer, ForumThreadMessage> threadReplies;

    public ForumThread(int groupId, int threadId, int author, String authorUsername, String threadTitle,
                       String threadMessage, int timePosted, Map<Integer, ForumThreadMessage> threadReplies) {
        this.groupId = groupId;
        this.threadId = threadId;
        this.author = author;
        this.authorUsername = authorUsername;
        this.threadTitle = threadTitle;
        this.threadMessage = threadMessage;
        this.timePosted = timePosted;
        this.threadReplies = threadReplies;
    }

    public void dispose() {
        this.threadReplies.clear();
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getThreadTitle() {
        return threadTitle;
    }

    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle;
    }

    public String getThreadMessage() {
        return threadMessage;
    }

    public void setThreadMessage(String threadMessage) {
        this.threadMessage = threadMessage;
    }

    public int getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(int timePosted) {
        this.timePosted = timePosted;
    }

    public Map<Integer, ForumThreadMessage> getThreadReplies() {
        return threadReplies;
    }
}
