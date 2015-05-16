package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.game.quests.Quest;
import com.cometproject.server.game.quests.QuestManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.quests.QuestStartedMessageComposer;
import com.cometproject.server.network.messages.outgoing.quests.QuestStoppedMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class InitializeRoomMessageEvent implements Event {
    // Temporary hacky shizzle for room loading, will keep using for now, I will remove if any issues
    public static final LoadHeightmapMessageEvent heightmapMessageEvent = new LoadHeightmapMessageEvent();
    public static final AddUserToRoomMessageEvent addUserToRoomMessageEvent = new AddUserToRoomMessageEvent();

    public void handle(Session client, MessageEvent msg) {
        int id = msg.readInt();
        String password = msg.readString();

        client.getPlayer().loadRoom(id, password);

        if (client.getPlayer().getData().getQuestId() != 0) {
            Quest quest = QuestManager.getInstance().getById(client.getPlayer().getData().getQuestId());

            if (quest != null && client.getPlayer().getQuests().hasStartedQuest(quest.getId()) && !client.getPlayer().getQuests().hasCompletedQuest(quest.getId())) {
                client.send(new QuestStartedMessageComposer(quest, client.getPlayer()));
            }
        }
    }
}
