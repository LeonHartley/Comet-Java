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
import com.corundumstudio.socketio.parser.JsonObject;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AUTH;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class WebSocketServer {
    private final String AUTHENTICATION_KEY = "cometServer69";
    private final SocketIOServer server;

    private final Map<String, BiConsumer<SocketIOClient, String[]>> commands = new HashMap<>();

    private final CometProcessManager processManager;

    public WebSocketServer(CometProcessManager processManager) {
        this.processManager = processManager;

        final Configuration configuration = new Configuration();

        configuration.setPort(1232);

        configuration.setBossThreads(4);
        configuration.setWorkerThreads(4);

        this.commands.put("help", (client, data) -> {
            final StringBuilder status = new StringBuilder();

            status.append("Commands: <br />");
            status.append("<div style='padding-left: 30px;'>service status [alias | all]</div>");
            status.append("<div style='padding-left: 30px;'>service start [alias]</div>");
            status.append("<div style='padding-left: 30px;'>service start [alias] [version] [api url] [api token] [arguments]</div>");
            status.append("<div style='padding-left: 30px;'>service status [alias]</div>");
            status.append("<div style='padding-left: 30px;'>service stop [alias]</div>");
            status.append("<div style='padding-left: 30px;'>service listen [alias]</div>");
            status.append("<div style='padding-left: 30px;'>service update [alias] [version]</div>");
            status.append("<div style='padding-left: 30px;'>service delete [alias]</div>");

            client.sendEvent("log", status.toString());
            return;
        });

        this.commands.put("service", (client, data) -> {
            if (data.length < 3) {
                return;
            }

            if(!client.has("authenticated")) {
                client.sendEvent("log", "You must be authenticated to use this command");
                return;
            }

            try {
                switch (data[1]) {

                    case "status": {
                        final String instance = data[2];

                        if (instance.equals("all")) {
                            final StringBuilder status = new StringBuilder();

                            status.append("Status of all processes: <br />");

                            for (AbstractProcess process : this.processManager.getProcesses().values()) {
                                final CometServerProcess comet = ((CometServerProcess) process);

                                if(comet.getStatusObject() == null) {
                                    continue;
                                }

                                final com.google.gson.JsonObject statusObject = comet.getStatusObject().get("status").getAsJsonObject();

                                status.append("<div style='padding-left: 30px;'><b>" +
                                        comet.getProcessName() + " (Version: " + comet.getServerVersion() + ")</b><br />" +
                                        "Uptime: " + statusObject.get("uptime").getAsString() + "<br />" +
                                        "Players online: " + statusObject.get("players").getAsString() + "<br />" +
                                        "Active rooms: " + statusObject.get("rooms").getAsString() + "<br />" +
                                        "Allocated memory: " + statusObject.get("allocatedMemory").getAsString() + "<br />" +
                                        "Used memory: " + statusObject.get("usedMemory").getAsString() +
                                        "</div>");
                            }

                            client.sendEvent("log", status.toString());
                            return;
                        }

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

                        if (this.processManager.getProcesses().containsKey(instance)) {
                            final CometServerProcess process = (CometServerProcess) this.processManager.getProcesses().get(instance);

                            final CometServerProcess newProcess = new CometServerProcess(process.getProcessName(),
                                    process.getApplicationArguments(),
                                    process.getServerVersion(),
                                    process.getApiUrl(),
                                    process.getApiToken());

                            newProcess.getListeners().addAll(process.getListeners());

                            this.processManager.getProcesses().replace(instance, newProcess);
                            client.sendEvent("log", "Starting already-existing instance " + instance);
                            return;
                        }

                        if (data.length < 5) {
                            client.sendEvent("log", "Expected format: service start [instanceName] [serverVersion] [apiUrl] [apiToken] [arguments...]");
                            return;
                        }

                        final String serverVersion = data[3];
                        final String apiUrl = data[4];
                        final String apiToken = data[5];
                        final String arguments = StringUtils.join(data, " ", 5, data.length - 1);

                        this.processManager.getProcesses().put(instance, new CometServerProcess(instance, arguments, serverVersion, apiUrl, apiToken));
                        client.sendEvent("log", "Creating instance " + instance + " with arguments [" + arguments + "]");
                        return;
                    }

                    case "stop": {
                        final String instance = data[2];

                        final AbstractProcess process = this.processManager.getProcesses().get(instance);

                        if (process == null) {
                            client.sendEvent("log", "There is no process with this name");
                            return;
                        } else {
                            process.setProcessStatus(ProcessStatus.STOPPING);
                        }

                        client.sendEvent("log", "This process is now stopping");
                        return;
                    }

                    case "listen": {
                        final String instance = data[2];
                        final AbstractProcess process = this.processManager.getProcesses().get(instance);

                        if (process == null) {
                            client.sendEvent("log", "There is no process with this name");
                            return;
                        }

                        process.listen(client);
                        return;
                    }

                    case "update": {
                        final String instance = data[2];
                        final AbstractProcess process = this.processManager.getProcesses().get(instance);

                        if (process == null) {
                            client.sendEvent("log", "There is no process with this name");
                            return;
                        }

                        if(data.length < 4) {
                            client.sendEvent("log", "There is no process with this name");
                            return;
                        }

                        final String version = data[3];

                        client.sendEvent("log", "Service was updated to version: <i>" + version + "</i>, previous version: " +
                                "<i>" + ((CometServerProcess) process).getServerVersion() + "</i> <br />You must restart the process for this change to take effect.");
                        ((CometServerProcess) process).setServerVersion(version);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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
