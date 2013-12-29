package com.cometsrv.game.commands.user;

import com.cometsrv.config.Locale;
import com.cometsrv.game.commands.ChatCommand;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.outgoing.misc.MotdNotificationComposer;
import com.cometsrv.network.sessions.Session;

public class SellRoomCommand extends ChatCommand {

    @Override
    public void execute(Session client, String message[]) {
        Room room = client.getPlayer().getAvatar().getRoom();

        if(message[0].equals("info")) {
            client.send(MotdNotificationComposer.compose(Locale.get("command.sellroom.info")));
            return;
        }

        if(room.getData().getOwnerId() != client.getPlayer().getId()) {
            this.sendChat(Locale.get("command.sellroom.owner"), client);
            return;
        }

        int price = Integer.parseInt(message[0]);

        if(price == 0) {
            room.setForSale(false);
            room.setPrice(0);

            this.sendChat(Locale.get("command.sellroom.saleCancelled"), client);
            // TODO: notify room
            return;
        }

        room.setPrice(price);
        room.setForSale(true);

        this.sendChat(Locale.get("command.sellroom.forSale").replace("%", String.valueOf(price)), client);
        // TODO: notify room
    }

    @Override
    public String getPermission() {
        return "sellroom_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.sellroom.description");
    }
}
