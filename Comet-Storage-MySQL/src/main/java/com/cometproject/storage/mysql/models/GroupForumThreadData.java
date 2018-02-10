package com.cometproject.storage.mysql.models;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.components.forum.IForumThread;
import com.cometproject.api.game.groups.types.components.forum.IForumThreadReply;
import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.networking.messages.IComposer;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class GroupForumThreadData implements IForumThread {
    private static final int MAX_MESSAGES_PER_PAGE = 20;

    private int id;
    private String title;
    private int authorId;
    private int authorTimestamp;
    private int state;
    private boolean isLocked;
    private boolean isPinned;

    private int adminId;
    private String adminUsername;

    private List<IForumThreadReply> replies;

    public GroupForumThreadData(int id, String title, String message, int authorId, int authorTimestamp, int state, boolean isLocked, boolean isPinned, int adminId, String adminUsername) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.authorTimestamp = authorTimestamp;
        this.state = state;
        this.isLocked = isLocked;
        this.isPinned = isPinned;
        this.adminId = adminId;
        this.adminUsername = adminUsername;

        this.replies = new ArrayList<>();

        // Add the OP.
        this.replies.add(new GroupForumThreadMessageData(id, 0, message, this.id, authorId, authorTimestamp, 1, this.adminId, this.adminUsername));
    }

    public GroupForumThreadData(int id, String title, int authorId, int authorTimestamp, int state, boolean isLocked, boolean isPinned, List<IForumThreadReply> replies, int adminId, String adminUsername) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.authorTimestamp = authorTimestamp;
        this.state = state;
        this.isLocked = isLocked;
        this.isPinned = isPinned;
        this.replies = replies;
        this.adminId = adminId;
        this.adminUsername = adminUsername;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.getId());

        final PlayerAvatar authorAvatar = GameContext.getCurrent().getPlayerService().getAvatarByPlayerId(this.getAuthorId(), PlayerAvatar.USERNAME_FIGURE);

        msg.writeInt(authorAvatar == null ? 0 : authorAvatar.getId());
        msg.writeString(authorAvatar == null ? "Unknown Player" : authorAvatar.getUsername());

        msg.writeString(this.getTitle());
        msg.writeBoolean(this.isPinned());
        msg.writeBoolean(this.isLocked());
        msg.writeInt((int) (System.currentTimeMillis() / 1000) - this.getAuthorTimestamp());
        msg.writeInt(this.getReplies().size());
        msg.writeInt(0); // unread messages
        msg.writeInt(this.getMostRecentPost().getId());

        final PlayerAvatar replyAuthor = GameContext.getCurrent().getPlayerService().getAvatarByPlayerId(this.getMostRecentPost().getAuthorId(), PlayerAvatar.USERNAME_FIGURE);

        msg.writeInt(replyAuthor == null ? 0 : replyAuthor.getId());
        msg.writeString(replyAuthor == null ? "Unknown Player" : replyAuthor.getUsername());
        msg.writeInt((int) (System.currentTimeMillis() / 1000) - this.getMostRecentPost().getAuthorTimestamp());
        msg.writeByte(this.getState());
        msg.writeInt(this.adminId); //admin id
        msg.writeString(this.adminUsername); // admin username
        msg.writeInt(0); // admin action time ago.
    }

    @Override
    public List<IForumThreadReply> getReplies(int start) {
        List<IForumThreadReply> replies = Lists.newArrayList();

        for (int i = start; replies.size() < MAX_MESSAGES_PER_PAGE; i++) {
            if (i >= this.replies.size())
                break;

            replies.add(this.replies.get(i));
        }

        return replies;
    }

    @Override
    public IForumThreadReply getReplyById(final int id) {
        for (IForumThreadReply reply : this.replies) {
            if (reply.getId() == id) {
                return reply;
            }
        }

        return null;
    }

    @Override
    public IForumThreadReply getMostRecentPost() {
        return this.replies.get(this.replies.size() - 1);
    }

    @Override
    public void addReply(IForumThreadReply reply) {
        this.replies.add(reply);
    }

    @Override
    public void dispose() {
        this.replies.clear();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public List<IForumThreadReply> getReplies() {
        return replies;
    }

    @Override
    public void setReplies(List<IForumThreadReply> replies) {
        this.replies = replies;
    }

    @Override
    public int getAuthorId() {
        return authorId;
    }

    @Override
    public int getAuthorTimestamp() {
        return authorTimestamp;
    }

    @Override
    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public boolean isPinned() {
        return isPinned;
    }

    @Override
    public void setIsPinned(boolean isPinned) {
        this.isPinned = isPinned;
    }

}
