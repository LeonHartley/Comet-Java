package com.cometproject.gamecenter.fastfood.net.composers;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.gamecenter.fastfood.FastFoodGame;
import com.cometproject.gamecenter.fastfood.net.FastFoodGameSession;
import com.cometproject.gamecenter.fastfood.net.FastFoodNetSession;
import com.cometproject.server.protocol.messages.MessageComposer;

public class PlayerJoinGameMessageComposer extends MessageComposer  {

    private final FastFoodGame fastFoodGame;
    private final FastFoodGameSession gameSession;

    public PlayerJoinGameMessageComposer(FastFoodGame fastFoodGame, FastFoodGameSession gameSession) {
        this.fastFoodGame = fastFoodGame;
        this.gameSession = gameSession;
    }

    @Override
    public short getId() {
        return 3;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.gameSession.getPlayerId());

        msg.writeBoolean(true);
        msg.writeBoolean(true);
        msg.writeBoolean(true);
        msg.writeInt(0);

        msg.writeInt(6); // count of what?

        msg.writeInt(0); //obj0
        msg.writeString("0.4");
        msg.writeString("-2.0");
        msg.writeString("0.1");
        msg.writeString("0.2");
        msg.writeInt(100);

        msg.writeInt(1);
        msg.writeString("0.5");
        msg.writeString("-2.0");
        msg.writeString("0.1");
        msg.writeString("0.15");
        msg.writeInt(150);

        msg.writeInt(2);
        msg.writeString("0.7");
        msg.writeString("-1.2");
        msg.writeString("0.2");
        msg.writeString("0.2");
        msg.writeInt(100);

        msg.writeInt(3);
        msg.writeString("0.9");
        msg.writeString("-1.5");
        msg.writeString("0.2");
        msg.writeString("0.2");
        msg.writeInt(200);

        msg.writeInt(4);
        msg.writeString("1.1");
        msg.writeString("-1.5");
        msg.writeString("0.15");
        msg.writeString("0.15");
        msg.writeInt(300);

        msg.writeInt(5);
        msg.writeString("1.5");
        msg.writeString("-2.0");
        msg.writeString("0.15");
        msg.writeString("0.2");
        msg.writeInt(200);

        msg.writeInt(3);

        msg.writeInt(0);
        msg.writeInt(this.gameSession.getParachutes());

        msg.writeInt(1);
        msg.writeInt(this.gameSession.getMissiles());

        msg.writeInt(2);
        msg.writeInt(this.gameSession.getShields());

        msg.writeInt(this.fastFoodGame.getPlayers().size());

        for(FastFoodNetSession netSession : this.fastFoodGame.getPlayers()) {
            msg.writeInt(netSession.getGameSession().getPlayerId());
            msg.writeString(netSession.getGameSession().getUsername());
            msg.writeString(".gif");//figure
            msg.writeString(netSession.getGameSession().getGender().toUpperCase());
            msg.writeString("");//hotel
            msg.writeInt(0);//achv count
        }
    }
}
