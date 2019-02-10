package com.cometproject.server.game.commands.staff;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.vote.RoomVote;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.ws.messages.room.RoomVoteEndedMessage;
import com.cometproject.server.network.ws.messages.room.RoomVoteMessage;
import com.cometproject.server.network.ws.messages.room.data.EventData;
import com.cometproject.server.tasks.CometThreadManager;

import java.util.concurrent.TimeUnit;

public class EventVoteCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 3) {
            return;
        }

        int roomIdA;
        int roomIdB;
        int seconds;

        try {
            roomIdA = Integer.parseInt(params[0]);
            roomIdB = Integer.parseInt(params[1]);
            seconds = Integer.parseInt(params[2]);
        } catch (Exception e) {
            sendWhisper("Must be numeric values", client);
            return;
        }

//        if (RoomManager.getInstance().getRoomVote() != null) {
//            sendWhisper("There's already an ongoing room vote.", client);
//            return;
//        }
//

        final IRoomData roomDataA = GameContext.getCurrent().getRoomService().getRoomData(roomIdA);
        final IRoomData roomDataB = GameContext.getCurrent().getRoomService().getRoomData(roomIdB);

        if (roomDataA == null || roomDataB == null) {
            sendWhisper("Rooms must exist", client);
            return;
        }

        final RoomVote roomVote = new RoomVote(client.getPlayer().getId(), roomIdA, roomIdB);
        RoomManager.getInstance().setRoomVote(roomVote);

        NetworkManager.getInstance().getSessions().broadcastWs(new RoomVoteMessage(
                client.getPlayer().getData().getUsername(),
                client.getPlayer().getData().getFigure(),
                new EventData[]{
                        new EventData(roomIdA, roomDataA.getName(), roomDataA.getDescription()),
                        new EventData(roomIdB, roomDataB.getName(), roomDataB.getDescription())
                },
                seconds));

        CometThreadManager.getInstance().executeSchedule(() -> {
            NetworkManager.getInstance().getSessions().broadcastWs(new RoomVoteEndedMessage());

            final StringBuilder alert = new StringBuilder("Voting has ended!\n\n");
            final int[] votes = roomVote.getVotes();
            alert.append(String.format("%s - Votes: %s\n\n", roomDataA.getName(), votes[0]));
            alert.append(String.format("%s - Votes: %s", roomDataB.getName(), votes[1]));

            NetworkManager.getInstance().getSessions().broadcast(new MotdNotificationMessageComposer(alert.toString()));
        }, seconds, TimeUnit.SECONDS);
    }

    @Override
    public String getPermission() {
        return "eventvote_command";
    }

    @Override
    public String getParameter() {
        return "%roomid% %roomid% %seconds%";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.eventvote.description");
    }
}
