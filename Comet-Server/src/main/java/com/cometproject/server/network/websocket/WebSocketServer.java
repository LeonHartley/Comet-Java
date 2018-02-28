package com.cometproject.server.network.websocket;

import com.cometproject.api.utilities.Initialisable;
import com.cometproject.server.network.websocket.listeners.AuthListener;
import com.cometproject.server.network.websocket.listeners.DisconnectionListener;
import com.cometproject.server.network.websocket.listeners.MessageListener;
import com.cometproject.server.network.websocket.listeners.types.AuthenticationRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.apache.log4j.Logger;

public class WebSocketServer implements Initialisable {
    private static WebSocketServer instance;

    /**
     * The configuration object for the SocketIO server
     */
    private final Configuration configuration;
    /**
     * Logging
     */
    private final Logger log = Logger.getLogger(WebSocketServer.class.getName());
    /**
     * The SocketIO server instance
     */
    private SocketIOServer server;

    /**
     * Creates a WebSocketServer
     */
    public WebSocketServer() {
        this.configuration = new Configuration();
    }

    public static WebSocketServer getInstance() {
        if (instance == null) {
            instance = new WebSocketServer();
        }

        return instance;
    }

    /**
     * Initializes the server
     */
    public void initialize() {
        this.configure();
        this.setupServer();
        this.setupListeners();

        if (this.server != null) {
            this.server.start();
        }
    }

    /**
     * Configures the websocket server
     */
    private void configure() {
        this.configuration.setHostname("0.0.0.0");//configurable soon dw....
        this.configuration.setPort(30002);//configurable soon dw....
    }

    private void setupServer() {
        try {
            this.server = new SocketIOServer(this.configuration);
        } catch (Exception e) {
            this.handleException(e);
        }
    }

    /**
     * Adds all available listeners to the server instance
     */
    private void setupListeners() {
        this.server.addDisconnectListener(new DisconnectionListener());
        this.server.addMessageListener(new MessageListener());
        this.server.addEventListener(AuthListener.EVENT_NAME, AuthenticationRequest.class, new AuthListener());
    }

    /**
     * Handles an exception from the SocketIO Server
     *
     * @param e The exception to be handled
     */
    private void handleException(Exception e) {
        log.error("Error caught from SocketIO server", e);
    }
}
