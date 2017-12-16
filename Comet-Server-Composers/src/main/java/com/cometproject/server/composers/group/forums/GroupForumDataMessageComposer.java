package com.cometproject.server.composers.group.forums;

import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.components.forum.IForumSettings;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.api.game.groups.types.components.forum.ForumPermission;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class GroupForumDataMessageComposer extends MessageComposer {
    private final IGroup group;
    private final int playerId;

    public  GroupForumDataMessageComposer(final IGroup group, int playerId) {
        this.group = group;
        this.playerId = playerId;
    }

    @Override
    public short getId() {
        return Composers.GroupForumDataMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        this.group.getForum().composeData(msg, group.getData());

        final IForumSettings forumSettings = this.group.getForum().getForumSettings();

        msg.writeInt(forumSettings.getReadPermission().getPermissionId());
        msg.writeInt(forumSettings.getPostPermission().getPermissionId());
        msg.writeInt(forumSettings.getStartThreadsPermission().getPermissionId());
        msg.writeInt(forumSettings.getModeratePermission().getPermissionId());

        String errorRead = "";
        String errorPost = "";
        String errorStartThread = "";
        String errorModerate = "";

        if(forumSettings.getReadPermission() == ForumPermission.MEMBERS &&
                !this.group.getMembers().getAll().containsKey(playerId)) {
            errorRead = "not_member";
        } else if(forumSettings.getReadPermission() == ForumPermission.ADMINISTRATORS &&
                !this.group.getMembers().getAdministrators().contains(playerId)) {
            errorRead = "not_admin";
        }

        if(forumSettings.getPostPermission() == ForumPermission.MEMBERS &&
                !this.group.getMembers().getAll().containsKey(playerId)) {
            errorPost = "not_member";
        } else if(forumSettings.getPostPermission() == ForumPermission.ADMINISTRATORS &&
                !this.group.getMembers().getAdministrators().contains(playerId)) {
            errorPost = "not_admin";
        } else if(forumSettings.getPostPermission() == ForumPermission.OWNER &&
                this.playerId != this.group.getData().getOwnerId()) {
            errorPost = "not_owner";
        }

        if(forumSettings.getStartThreadsPermission() == ForumPermission.MEMBERS &&
                !this.group.getMembers().getAll().containsKey(playerId)) {
            errorStartThread = "not_member";
        } else if(forumSettings.getStartThreadsPermission() == ForumPermission.ADMINISTRATORS &&
                !this.group.getMembers().getAdministrators().contains(playerId)) {
            errorStartThread = "not_admin";
        } else if(forumSettings.getStartThreadsPermission() == ForumPermission.OWNER &&
                this.playerId != this.group.getData().getOwnerId()) {
            errorStartThread = "not_owner";
        }

        if(forumSettings.getModeratePermission() == ForumPermission.ADMINISTRATORS &&
                !this.group.getMembers().getAdministrators().contains(playerId)) {
            errorModerate = "not_admin";
        } else if(forumSettings.getModeratePermission() == ForumPermission.OWNER &&
                this.playerId != this.group.getData().getOwnerId()) {
            errorModerate = "not_owner";
        }

        msg.writeString(errorRead);//1
        msg.writeString(errorPost);//2
        msg.writeString(errorStartThread);//3
        msg.writeString(errorModerate);//4

        msg.writeString("");//??

        msg.writeBoolean(this.group.getData().getOwnerId() == this.playerId);
        msg.writeBoolean(this.group.getData().getOwnerId() == this.playerId); // TODO: Allow staff to control forums too.
    }
}
