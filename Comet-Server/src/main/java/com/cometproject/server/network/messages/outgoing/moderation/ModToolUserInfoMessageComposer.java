package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class ModToolUserInfoMessageComposer extends MessageComposer {
    private final PlayerData playerData;
    private final PlayerStatistics playerStatistics;

    public ModToolUserInfoMessageComposer(final PlayerData playerData, final PlayerStatistics playerStatistics) {
        this.playerData = playerData;
        this.playerStatistics = playerStatistics;
    }

    @Override
    public short getId() {
        return Composers.ModeratorUserInfoMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(playerData.getId());
        msg.writeString(playerData.getUsername());
        msg.writeString(playerData.getFigure());

        msg.writeInt((int) (Comet.getTime() - playerData.getRegTimestamp()) / 60);
        msg.writeInt((int) (Comet.getTime() - playerData.getLastVisit()) / 60);

        msg.writeBoolean(PlayerManager.getInstance().isOnline(playerData.getId()));

        msg.writeInt(playerStatistics.getHelpTickets());
        msg.writeInt(playerStatistics.getAbusiveHelpTickets());
        msg.writeInt(playerStatistics.getCautions());
        msg.writeInt(playerStatistics.getBans());
        msg.writeInt(0); // TODO: FInd out what this is
        msg.writeString("N/A"); //trading_lock_expiry_txt

        msg.writeString("N/A"); // TODO: purchase logging
        msg.writeInt(1); // ???
        msg.writeInt(0); // Banned accounts

        // TODO: Find banned accounts using this IP address or linked to this e-mail address (for hotels that use the Habbo ID system)

        msg.writeString("Email: " + playerData.getEmail());
        msg.writeString(playerData.getIpAddress());
    }
}
