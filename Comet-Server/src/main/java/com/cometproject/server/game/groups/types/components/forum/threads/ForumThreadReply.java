package com.cometproject.server.game.groups.types.components.forum.threads;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerAvatar;
import com.cometproject.server.storage.queries.groups.GroupForumThreadDao;

public class ForumThreadReply {
    private int id;
    private int index;

    private String message;
    private int threadId;
    private int authorId;
    private int authorTimestamp;

    private int state;

    private int adminId;
    private String adminUsername;

    public ForumThreadReply(int id, int index, String message, int threadId, int authorId, int authorTimestamp, int state, int adminId, String adminUsername) {
        this.id = id;
        this.index = index;
        this.message = message;
        this.threadId = threadId;
        this.authorId = authorId;
        this.authorTimestamp = authorTimestamp;
        this.state = state;
        this.adminId = adminId;
        this.adminUsername = adminUsername;
    }
    
    public void compose(IComposer msg) {
        final PlayerAvatar playerAvatar = PlayerManager.getInstance().getAvatarByPlayerId(this.getAuthorId(),
                PlayerAvatar.USERNAME_FIGURE);

        msg.writeInt(this.getId());
        msg.writeInt(this.index);

        msg.writeInt(this.getAuthorId());
        msg.writeString(playerAvatar.getUsername());
        msg.writeString(playerAvatar.getFigure());

        msg.writeInt((int) Comet.getTime() - this.getAuthorTimestamp());
        msg.writeString(this.getMessage());
        msg.writeByte(this.getState()); // state

        msg.writeInt(this.adminId); // _adminId
        msg.writeString(this.adminUsername); // _adminName
        msg.writeInt(0); // _adminOperationTimeAsSeccondsAgo
        msg.writeInt(GroupForumThreadDao.getPlayerMessageCount(playerAvatar.getId())); // messages by author todo: optimise if needed
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

    public int getThreadId() {
        return threadId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }
}
