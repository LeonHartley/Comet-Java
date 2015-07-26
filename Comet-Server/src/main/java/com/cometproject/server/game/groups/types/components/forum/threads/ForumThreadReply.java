package com.cometproject.server.game.groups.types.components.forum.threads;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerAvatar;

public class ForumThreadReply {
    private int id;
    private int index;

    private String message;
    private int threadId;
    private int authorId;
    private int authorTimestamp;

    private boolean isHidden;

    public ForumThreadReply(int id, int index, String message, int threadId, int authorId, int authorTimestamp, boolean isHidden) {
        this.id = id;
        this.index = index;
        this.message = message;
        this.threadId = threadId;
        this.authorId = authorId;
        this.authorTimestamp = authorTimestamp;
        this.isHidden = isHidden;
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
        msg.writeByte(1); // state

        msg.writeInt(0); // _adminId
        msg.writeString(""); // _adminName
        msg.writeInt(0); // _adminOperationTimeAsSeccondsAgo
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
