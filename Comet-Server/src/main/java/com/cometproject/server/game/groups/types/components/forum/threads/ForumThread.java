package com.cometproject.server.game.groups.types.components.forum.threads;

import java.util.ArrayList;
import java.util.List;

public class ForumThread {
    private int id;
    private String title;
    private String message;
    private int authorId;
    private int authorTimestamp;
    private int state;
    private boolean isLocked;
    private boolean isHidden;

    private List<ForumThreadReply> replies;

    public ForumThread(int id, String title, String message, int authorId, int authorTimestamp, int state, boolean isLocked, boolean isHidden) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.authorId = authorId;
        this.authorTimestamp = authorTimestamp;
        this.state = state;
        this.isLocked = isLocked;
        this.isHidden = isHidden;
        this.replies = new ArrayList<>();
    }

    public void addReply(ForumThreadReply reply) {
        this.replies.add(reply);
    }

    public void dispose() {
        this.replies.clear();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ForumThreadReply> getReplies() {
        return replies;
    }

    public void setReplies(List<ForumThreadReply> replies) {
        this.replies = replies;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getAuthorTimestamp() {
        return authorTimestamp;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setIsHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
