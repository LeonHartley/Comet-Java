package com.cometproject.process.io;

import com.cometproject.process.CometProcessManager;
import com.cometproject.process.processes.AbstractProcess;
import com.cometproject.process.processes.ProcessStatus;
import com.cometproject.process.processes.server.CometServerProcess;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AUTH;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class WebSocketServer {
    private final String AUTHENTICATION_KEY = "hellomarkones";
    private final SocketIOServer server;

    private final Map<String, BiConsumer<SocketIOClient, String[]>> commands = new HashMap<>();

    private final CometProcessManager processManager;

    public WebSocketServer(CometProcessManager processManager) {
        this.processManager = processManager;

        final Configuration configuration = new Configuration();

        configuration.setPort(3000);

        configuration.setBossThreads(4);
        configuration.setWorkerThreads(4);

        this.commands.put("service", (client, data) -> {
            if (data.length < 3) {
                return;
            }

            if(!client.has("authenticated")) {
                client.sendEvent("log", "You must be authenticated to use this command");
                return;
            }

            switch (data[1]) {
                case "status": {
                    final String instance = data[2];

                    if (!this.processManager.getProcesses().containsKey(instance)) {
                        client.sendEvent("log", "There is no process with this name");
                    } else {
                        final AbstractProcess process = this.processManager.getProcesses().get(instance);

                        client.sendEvent("log", "This process is currently " + process.getProcessStatus());
                    }

                    return;
                }

                case "start": {
                    final String instance = data[2];

                    if(this.processManager.getProcesses().containsKey(instance)) {
                        this.processManager.getProcesses().get(instance).setProcessStatus(ProcessStatus.STARTING);
                        client.sendEvent("log", "Starting already-existing instance " + instance);
                        return;
                    }

                    if(data.length < 5) {
                        client.sendEvent("log", "Expected format: service start [instanceName] [serverVersion] [apiUrl] [apiToken] [arguments...]");
                        return;
                    }

                    final String serverVersion = data[3];
                    final String apiUrl = data[4];
                    final String apiToken = data[5];
                    final String arguments = StringUtils.join(data, " ", 5, data.length-1);

                    this.processManager.getProcesses().put(instance, new CometServerProcess(instance, arguments, serverVersion, apiUrl, apiToken));
                    client.sendEvent("log", "Creating instance " + instance + " with arguments [" + arguments + "]");
                    return;
                }

                case "stop": {
                    final String instance = data[2];

                    final AbstractProcess process = this.processManager.getProcesses().get(instance);

                    if(process == null) {
                        client.sendEvent("log", "There is no process with this name");
                        return;
                    } else {
                        process.setProcessStatus(ProcessStatus.STOPPING);
                    }

                    client.sendEvent("log", "This process is now stopping");
                    return;
                }
            }
        });

        this.commands.put("authenticate", (client, data) -> {
            if (data.length == 1) {
                client.sendEvent("log", "Failed to authenticate");
                return;
            }

            final String authKey = data[1];

            if (authKey.equals(AUTHENTICATION_KEY)) {
                client.sendEvent("log", "Successfully authenticated");
                client.set("authenticated", "yes");
            } else {
                client.sendEvent("log", "Failed to authenticate");
            }
        });

        this.server = new SocketIOServer(configuration);
        this.server.addEventListener("Command", String.class, (socketIOClient, s, ackRequest) -> handleCommand(socketIOClient, s));
        this.server.start();
    }

    private void handleCommand(SocketIOClient client, String data) {
        final String[] commandData = data.split(" ");
        final BiConsumer<SocketIOClient, String[]> commandHandler = this.commands.get(commandData[0]);

        if (commandHandler != null) {
            commandHandler.accept(client, commandData);
        }
    }

    private final DataListener<String> dataListener = (socketIOClient, data, request) -> {
        System.out.println(data);
    };
}
