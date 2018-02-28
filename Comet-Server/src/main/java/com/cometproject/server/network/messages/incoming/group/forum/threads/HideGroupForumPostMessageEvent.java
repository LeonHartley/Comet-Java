package com.cometproject.server.network.messages.incoming.group.forum.threads;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.components.forum.ForumPermission;
import com.cometproject.api.game.groups.types.components.forum.IForumSettings;
import com.cometproject.api.game.groups.types.components.forum.IForumThread;
import com.cometproject.api.game.groups.types.components.forum.IForumThreadReply;
import com.cometproject.server.composers.group.forums.GroupForumUpdateReplyMessageComposer;
import com.cometproject.server.composers.group.forums.GroupForumUpdateThreadMessageComposer;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.groups.GroupForumThreadDao;

import static com.cometproject.server.protocol.headers.Events.DeleteGroupReplyMessageEvent;

public class HideGroupForumPostMessageEvent implements Event {

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();
        int threadId = msg.readInt();
        int messageId = msg.getId() == DeleteGroupReplyMessageEvent ? msg.readInt() : -1;
        //int messageId = msg.readInt();
        int state = msg.readInt();

        IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);

        if (group == null || !group.getData().hasForum()) {
            return;
        }

        IForumSettings forumSettings = group.getForum().getForumSettings();

        if (forumSettings.getModeratePermission() == ForumPermission.OWNER) {
            if (client.getPlayer().getId() != group.getData().getId()) {
                return;
            }
        } else {
            if (!group.getMembers().getAdministrators().contains(client.getPlayer().getId())) {
                return;
            }
        }

        IForumThread forumThread = group.getForum().getForumThreads().get(threadId);

        if (forumThread == null) {
            return;
        }

        if (!client.getPlayer().getPermissions().getRank().modTool() && state > 10) {
            state = 10;
        }

        if (messageId != -1) {
            IForumThreadReply reply = forumThread.getReplyById(messageId);

            if (reply == null) {
                return;
            }

            reply.setState(state);
            client.send(new GroupForumUpdateReplyMessageComposer(reply, threadId, groupId));
        } else {
            forumThread.setState(state);
            client.send(new GroupForumUpdateThreadMessageComposer(groupId, forumThread));
        }

        GroupForumThreadDao.saveMessageState(messageId != -1 ? messageId : threadId, state, client.getPlayer().getId(),
                client.getPlayer().getData().getUsername());
    }
}
