package com.cometproject.server.game.groups.types.components.forum.threads;

public class ForumThreadMessage {
    private int groupId;
    private int threadId;
    private int timePosted;

    private int posterId;
    private int posterUsername;

    private boolean isDeleted;
    private String message;

    public ForumThreadMessage(int groupId, int threadId, int timePosted, int posterId, int posterUsername,
                              boolean isDeleted, String message) {
        this.groupId = groupId;
        this.threadId = threadId;
        this.timePosted = timePosted;
        this.posterId = posterId;
        this.posterUsername = posterUsername;
        this.isDeleted = isDeleted;
        this.message = message;
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

    public int getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(int timePosted) {
        this.timePosted = timePosted;
    }

    public int getPosterId() {
        return posterId;
    }

    public void setPosterId(int posterId) {
        this.posterId = posterId;
    }

    public int getPosterUsername() {
        return posterUsername;
    }

    public void setPosterUsername(int posterUsername) {
        this.posterUsername = posterUsername;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
