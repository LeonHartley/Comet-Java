package com.cometproject.server.game.groups.types.components.forum.threads;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.groups.types.components.forum.ForumComponent;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerAvatar;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class ForumThread {
    private int id;
    private String title;
    private int authorId;
    private int authorTimestamp;
    private int state;
    private boolean isLocked;
    private boolean isPinned;

    private List<ForumThreadReply> replies;

    public ForumThread(int id, String title, String message, int authorId, int authorTimestamp, int state, boolean isLocked, boolean isPinned) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.authorTimestamp = authorTimestamp;
        this.state = state;
        this.isLocked = isLocked;
        this.isPinned = isPinned;
        this.replies = new ArrayList<>();

        // Add the OP.
        this.replies.add(new ForumThreadReply(id, 0, message, this.id, authorId, authorTimestamp, 1));
    }
    
    public void compose(IComposer msg) {
        msg.writeInt(this.getId());

        final PlayerAvatar authorAvatar = PlayerManager.getInstance().getAvatarByPlayerId(this.getAuthorId(), PlayerAvatar.USERNAME_FIGURE);

        msg.writeInt(authorAvatar == null ? 0 : authorAvatar.getId());
        msg.writeString(authorAvatar == null ? "Unknown Player" : authorAvatar.getUsername());

        msg.writeString(this.getTitle());
        msg.writeBoolean(this.isPinned());
        msg.writeBoolean(this.isLocked());
        msg.writeInt((int) Comet.getTime() - this.getAuthorTimestamp());
        msg.writeInt(this.getReplies().size());
        msg.writeInt(0); // unread messages
        msg.writeInt(this.getMostRecentPost().getId());

        final PlayerAvatar replyAuthor = PlayerManager.getInstance().getAvatarByPlayerId(this.getMostRecentPost().getAuthorId(), PlayerAvatar.USERNAME_FIGURE);

        msg.writeInt(replyAuthor == null ? 0 : replyAuthor.getId());
        msg.writeString(replyAuthor == null ? "Unknown Player" : replyAuthor.getUsername());
        msg.writeInt((int) Comet.getTime() - this.getMostRecentPost().getAuthorTimestamp());
        msg.writeByte(this.getState());
        msg.writeInt(0); //admin id
        msg.writeString(""); // admin username
        msg.writeInt(0); // admin action time ago.
    }

    public List<ForumThreadReply> getReplies(int start) {
        List<ForumThreadReply> replies = Lists.newArrayList();

        for(int i = start; replies.size() < ForumComponent.MAX_MESSAGES_PER_PAGE; i++) {
            if(i >= this.replies.size())
                break;

            replies.add(this.replies.get(i));
        }

        return replies;
    }

    public ForumThreadReply getReplyById(final int id) {
        for(ForumThreadReply reply : this.replies) {
            if(reply.getId() == id) {
                return reply;
            }
        }

        return null;
    }

    public ForumThreadReply getMostRecentPost() {
        return this.replies.get(this.replies.size() - 1);
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setIsPinned(boolean isPinned) {
        this.isPinned = isPinned;
    }

}
