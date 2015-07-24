package com.cometproject.server.game.groups.types.components.forum.threads;

public class ForumThreadReply {
    private int id;
    private String message;
    private int threadId;
    private int authorId;
    private int authorTimestamp;

    private boolean isHidden;


    public ForumThreadReply(int id, String message, int authorId, int authorTimestamp, boolean isHidden) {
        this.id = id;
        this.message = message;
        this.authorId = authorId;
        this.authorTimestamp = authorTimestamp;
        this.isHidden = isHidden;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getAuthorTimestamp() {
        return authorTimestamp;
    }

    public void setAuthorTimestamp(int authorTimestamp) {
        this.authorTimestamp = authorTimestamp;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setIsHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public int getThreadId() {
        return threadId;
    }
}
