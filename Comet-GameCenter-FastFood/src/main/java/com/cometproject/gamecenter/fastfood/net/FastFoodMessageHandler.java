package com.cometproject.gamecenter.fastfood.net;

import com.cometproject.api.game.players.IPlayerService;
import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.networking.messages.IMessageEvent;
import com.cometproject.gamecenter.fastfood.FastFoodGame;
import com.cometproject.gamecenter.fastfood.net.composers.AuthenticationOKMessageComposer;
import com.cometproject.gamecenter.fastfood.net.composers.MyPowerUpsMessageComposer;
import com.cometproject.gamecenter.fastfood.net.composers.SetClientLocalisationMessageComposer;
import com.cometproject.gamecenter.fastfood.storage.MySQLFastFoodRepository;
import com.cometproject.networking.api.messages.IMessageHandler;
import com.cometproject.networking.api.sessions.INetSession;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiConsumer;

public class FastFoodMessageHandler implements IMessageHandler {
    private static final Logger log = Logger.getLogger(FastFoodMessageHandler.class);

    private final MySQLFastFoodRepository fastFoodRepository;
    private final IPlayerService playerService;
    private final ScheduledExecutorService executorService;

    private final Map<Short, BiConsumer<IMessageEvent, FastFoodNetSession>> messageHandlers;
    private final Set<FastFoodGame> games = Sets.newConcurrentHashSet();

    public FastFoodMessageHandler(ScheduledExecutorService executorService, IPlayerService playerService, MySQLFastFoodRepository fastFoodRepository) {
        this.messageHandlers = Maps.newConcurrentMap();

        this.playerService = playerService;
        this.fastFoodRepository = fastFoodRepository;
        this.executorService = executorService;

        this.messageHandlers.put((short) 1, this::authenticate);
        this.messageHandlers.put((short) 7, this::getLocalisations);
        this.messageHandlers.put((short) 5, this::disconnect);
        this.messageHandlers.put((short) 9, this::getPowerUps);
        this.messageHandlers.put((short) 8, this::getPowerUpPrices);
        this.messageHandlers.put((short) 11, this::getGamesCount);
        this.messageHandlers.put((short) 14, this::getUserCredits);
        this.messageHandlers.put((short) 10, this::purchasePowerUp);
        this.messageHandlers.put((short) 6, this::joinLobby);
        this.messageHandlers.put((short) 3, this::launchMissile);
    }

    private void authenticate(IMessageEvent messageEvent, INetSession session) {
        final String ticket = messageEvent.readString();

        final Integer playerId = this.playerService.getPlayerIdByAuthToken(ticket);

        if (playerId == null) {
            return;
        }

        final FastFoodGameSession gameSession = (FastFoodGameSession) session.getGameSession();
        gameSession.setPlayerId(playerId);

        session.getChannel().writeAndFlush(new AuthenticationOKMessageComposer());
    }

    public void getLocalisations(IMessageEvent messageEvent, INetSession session) {
        session.getChannel().writeAndFlush(new SetClientLocalisationMessageComposer());
    }

    public void disconnect(IMessageEvent messageEvent, INetSession session) {
        final FastFoodGameSession gameSession = (FastFoodGameSession) session.getGameSession();

        if(gameSession.getCurrentGame() != null) {
            // Leave the game!
        }
    }

    public void getPowerUpPrices(IMessageEvent messageEvent, INetSession session) {

    }

    public void getPowerUps(IMessageEvent messageEvent, INetSession session) {
        final FastFoodGameSession gameSession = (FastFoodGameSession) session.getGameSession();

        this.fastFoodRepository.loadPlayerData(gameSession);

        final PlayerAvatar playerAvatar = this.playerService.getAvatarByPlayerId(gameSession.getPlayerId(), PlayerAvatar.USERNAME_FIGURE);

        if(playerAvatar != null) {
            gameSession.setUsername(playerAvatar.getUsername());
            gameSession.setFigure(playerAvatar.getFigure());
            gameSession.setGender(playerAvatar.getGender());
        } else {
            session.getChannel().disconnect();
        }

        session.getChannel().writeAndFlush(new MyPowerUpsMessageComposer(gameSession));
    }

    public void getGamesCount(IMessageEvent messageEvent, INetSession session) {

    }

    public void getUserCredits(IMessageEvent messageEvent, INetSession session) {

    }


    public void purchasePowerUp(IMessageEvent messageEvent, INetSession session) {

    }

    public void joinLobby(IMessageEvent messageEvent, FastFoodNetSession session) {
        final FastFoodGame fastFoodGame = this.findGame();

        fastFoodGame.getPlayers().add(session);
        session.getGameSession().setCurrentGame(fastFoodGame);

        if (fastFoodGame.canStart()) {
            // Start the game!
            fastFoodGame.startGame(this.executorService);
        }
    }

    public void launchMissile(IMessageEvent messageEvent, FastFoodNetSession session) {
        if(session.getGameSession().getCurrentGame() == null || !session.getGameSession().getCurrentGame().hasStarted()) {
            return;
        }

        final int type = messageEvent.readInt();

        session.getGameSession().getCurrentGame().launch(type, session.getGameSession());
    }

    private FastFoodGame findGame() {
        FastFoodGame fastFoodGame = null;

        for (FastFoodGame game : this.games) {
            if (!game.canStart() && !game.canStart()) {
                fastFoodGame = game;
            }
        }

        if (fastFoodGame == null) {
            fastFoodGame = new FastFoodGame();

            this.games.add(fastFoodGame);
        }

        return fastFoodGame;
    }

    @Override
    public void handleMessage(IMessageEvent messageEvent, INetSession session) {
        final short messageId = messageEvent.getId();

        if (!this.messageHandlers.containsKey(messageId)) {
            log.debug("Unhandled message event: " + messageId);
        } else {
            this.messageHandlers.get(messageId).accept(messageEvent, (FastFoodNetSession) session);
            log.debug("Handled message event: " + messageId);
        }
    }
}
