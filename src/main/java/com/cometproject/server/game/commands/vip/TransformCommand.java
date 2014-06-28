package com.cometproject.server.game.commands.vip;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.LeaveRoomMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.sessions.Session;

public class TransformCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        TransformationType type = TransformationType.HUMAN;
        String transformationData = "";

        if(params.length == 1)
            type = TransformationType.valueOf(params[0].toUpperCase());

        if(type == TransformationType.HUMAN) {
            client.getPlayer().getEntity().removeAttribute("transformation");
        } else {
            transformationData = type.getData();
            client.getPlayer().getEntity().setAttribute("transformation", transformationData);
        }

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(LeaveRoomMessageComposer.compose(client.getPlayer().getEntity().getVirtualId()));
        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(AvatarsMessageComposer.compose(client.getPlayer().getEntity()));
    }

    @Override
    public String getPermission() {
        return "transform_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.transform.description");
    }

    public static void composeTransformation(Composer msg, String[] transformationData, PlayerEntity entity) {
        // TODO: create global composer for entity types maybe
        msg.writeInt(entity.getPlayerId());
        msg.writeString(entity.getUsername());
        msg.writeString(entity.getMotto());
        msg.writeString(transformationData[0]);
        msg.writeInt(entity.getVirtualId());

        msg.writeInt(entity.getPosition().getX());
        msg.writeInt(entity.getPosition().getY());
        msg.writeDouble(entity.getPosition().getZ());

        msg.writeInt(0); // 2 = user 4 = bot 0 = pet ??????
        msg.writeInt(2); // 1 = user 2 = pet 3 = bot ??????

        msg.writeInt(Integer.parseInt(transformationData[1]));
        msg.writeInt(entity.getPlayerId());
        msg.writeString("Leon"); // TODO: this :P
        msg.writeInt(1);
        msg.writeBoolean(true); // has saddle
        msg.writeBoolean(false); // has rider?

        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeString("");
    }

    private enum TransformationType {
        HUMAN(""),
        DOG("0 15 FEE4B2 2 2 -1 0 3 -1 0" + "#" + 15),
        CAT("1 0 F5E759 2 2 -1 0 3 -1 0" + "#" + 0),
        HORSE("15 3 FFFFFF 2 2 -1 0 3 -1 0" + "#" + 3),
        LION("6 0 FFFFFF 2 2 -1 0 3 -1 0" + "#" + 0),
        CROCODILE("2 4 96E75A 2 2 -1 0 3 -1 0" + "#" + 4),
        CHICK("10 0 FFFFFF 2 2 -1 0 3 -1 0" + "#" + 0),
        BEAR("4 0 FFFFFF 2 2 -1 0 3 -1 0" + "#" + 0),
        FROG("11 12 FFFFFF 2 2 -1 0 3 -1 0" + "#" + 12),
        TURTLE("9 0 FFFFFF 2 2 -1 0 3 -1 0" + "#" + 0),
        TERRIER("3 0 FFFFFF 2 2 -1 0 3 -1 0" + "#" + 0),
        SPIDER("8 0 FFFFFF 2 2 -1 0 3 -1 0" + "#" + 0),
        RHINO("7 0 CCCCCC 2 2 -1 0 3 -1 0" + "#" + 0),
        PIG("5 0 FFFFFF 2 2 -1 0 3 -1 0" + "#" + 0);

        private String transformationData;

        TransformationType(String data) {
            this.transformationData = data;
        }

        public String getData() {
            return transformationData;
        }
    }
}
