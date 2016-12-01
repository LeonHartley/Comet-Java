package com.cometproject.process.processes;

import com.cometproject.process.api.CometAPIClient;
import com.corundumstudio.socketio.SocketIOClient;
import com.google.gson.JsonObject;
import org.apache.http.cookie.CookiePathComparator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import org.zeroturnaround.exec.stream.LogOutputStream;
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeoutException;

public abstract class AbstractProcess extends Thread {
    private final String processName;
    private final Logger log;

    private ProcessStatus processStatus;

    private long lastStatusCheck = 0;
    private long shutdownRequested = 0;

    private final LinkedList<String> console = new LinkedList<String>();


    private final Set<SocketIOClient> listeners = new ConcurrentHashSet<>();

    public AbstractProcess(String processName) {
        super(processName);

        this.processName = processName;
        this.processStatus = ProcessStatus.STARTING;

        this.log = LogManager.getLogger(this.getClass().getName() + "#" + processName);
    }

    public abstract String[] executionCommand();

    public abstract void statusCheck();

    public JsonObject buildStatusObject() {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name", this.getProcessName());
        jsonObject.addProperty("status", this.getProcessStatus().toString());
        jsonObject.addProperty("lastStatusCheck", this.getLastStatusCheck());

        return jsonObject;
    }

    public void performStatusCheck() {
        if (this.requiresStatusCheck()) {
            log.trace("Processing status check for instance");

            if(this.getProcessStatus() == ProcessStatus.STARTING) {
                log.info("Starting instance");

                // start the instance.
                try {
                    this.start();

                } catch(Exception e) {
                    log.warn("Failed to start process", e);
                }
            } else if(this.getProcessStatus() == ProcessStatus.RESTARTING) {
                log.info("Restarting instance");

                try {
                    // todo: First we need to send the shutdown request, if it fails then we'll interrupt.
                    this.interrupt();
                } catch(Exception e) {
                    log.error(e);
                }
            } else if(this.getProcessStatus() == ProcessStatus.STOPPING) {
                log.info("Stopping service");

                try {
                    this.interrupt();
                } catch (Exception e) {
                    log.error(e);
                }

                this.setProcessStatus(ProcessStatus.DOWN);
            }

            this.statusCheck();

            this.lastStatusCheck = System.currentTimeMillis();
        }
    }

    @Override
    public void run() {
        try {
            final ProcessResult processResult = new ProcessExecutor().command(this.executionCommand())
                    .redirectOutput(new LogOutputStream() {
                        @Override
                        protected void processLine(String line) {
                            if(getProcessStatus() == ProcessStatus.STARTING)
                                setProcessStatus(ProcessStatus.UP);

                            if(console.size() >= 100) {
                                console.removeFirst();
                            }

                            console.add(line);

                            if(listeners.isEmpty()) {
                                return;
                            }

                            for(SocketIOClient client : listeners){
                                if(client.isChannelOpen()) {
                                    client.sendEvent("serverLog", line);
                                }
                            }
                            // Here we'll pipe the lines to the user via a websocket or something,
                            // so (if they have permission), they can see the output of the server.
                            log.info(line);
                        }
                    }).execute();

            log.warn("Process exited with code: {}", processResult.getExitValue());
        } catch(Exception e) {
            if(e instanceof InterruptedException) {
                // process was stopped.
            }
        }

        log.info("Process exited");
    }

    public void performShutdown() {
        this.shutdownRequested = System.currentTimeMillis();

        // Here's where we would handle anything we want to run before we shutdown.
    }

    public int statusCheckInterval() {
        return 30000;
    }

    public boolean requiresStatusCheck() {
        return System.currentTimeMillis() >= this.lastStatusCheck + this.statusCheckInterval();
    }

    public String getProcessName() {
        return this.processName;
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(final ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }

    public long getLastStatusCheck() {
        return this.lastStatusCheck;
    }

    public void listen(SocketIOClient client) {
        if(!this.console.isEmpty()) {
            for(String line : this.console) {
                client.sendEvent("serverLog", line);
            }
        }

        this.listeners.add(client);
    }

    public Set<SocketIOClient> getListeners() {
        return this.listeners;
    }
}
