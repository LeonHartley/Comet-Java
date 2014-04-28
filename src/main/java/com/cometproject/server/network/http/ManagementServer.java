package com.cometproject.server.network.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.net.InetSocketAddress;

public class ManagementServer {
    private static Logger logger = Logger.getLogger(ManagementServer.class.getName());

    public ManagementServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/", new ManagementCommandHandler());
            server.setExecutor(null);
            server.start();

        } catch (Exception e) {
            logger.error("Error while starting remote management server", e);
        }
    }

    public void sendResponse(String response, HttpExchange e) {
        try {
            e.sendResponseHeaders(200, response.length());

            OutputStream os = e.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception ex) {
            logger.error("Error while writing response", ex);
        }
    }

    public void sendResponse(ManagementCommandHandler.RequestError response, HttpExchange e) {
        sendResponse(response.toString(), e);
    }
}
