package com.cometproject.server.logging;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.logging.objects.TestJSON;
import com.cometproject.server.tasks.CometTask;
import com.google.gson.Gson;
import javolution.util.FastTable;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.concurrent.atomic.AtomicBoolean;

public class LoggingQueue implements CometTask {
    private final String BASE_URL = Comet.getServer().getConfig().get("comet.game.logging.baseurl");

    private FastTable<AbstractLogEntry> loggingEntries = new FastTable<>();
    private FastTable<AbstractLogEntry> pendingLogEntries = new FastTable<>();

    private AtomicBoolean isWriting = new AtomicBoolean(false);
    private CloseableHttpClient httpClient = HttpClients.createDefault();

    private AtomicBoolean isLoggingActive = new AtomicBoolean(true);
    private String token;

    // the amount of rounds to retry if service is unavailable
    private int INACTIVE_RETRY_ROUNDS = 60;

    private int retryCount = 0;

    public LoggingQueue(String token) {
        this.token = token;
    }

    public void addEntry(AbstractLogEntry entry) {
        // Checks for if the logging server is responding
        if (!this.isLoggingActive.get()) { return; }

        if (this.isWriting.get()) {
            this.pendingLogEntries.add(entry);
        } else {
            this.loggingEntries.add(entry);
        }
    }

    @Override
    public void run() {
        // inactive retry
        if (!this.isLoggingActive.get()) {
            if (retryCount < INACTIVE_RETRY_ROUNDS) {
                retryCount++;
                return;
            } else {
                retryCount = 0;
            }
        }

        // check if the logging service is responding
        if (!this.isLoggingOnline()) {
            this.isLoggingActive.set(false);
            return;
        }

        this.isWriting.set(true);

        for (AbstractLogEntry entry : this.loggingEntries) {
            if (entry.getType() != null) {
                if (entry.getType() == LogType.CHATLOGS) {
                    this.saveChatlog(entry);
                }
            }
        }

        this.loggingEntries.clear();

        this.isWriting.set(false);

        // shift the pending entries to the active queue for next round
        for (AbstractLogEntry entry : this.pendingLogEntries) {
            this.loggingEntries.add(entry);
        }

        this.pendingLogEntries.clear();
    }

    private boolean isLoggingOnline() {
        HttpGet getRequest = new HttpGet(BASE_URL + "/v1/test");

        try (CloseableHttpResponse res = this.httpClient.execute(getRequest)) {
            String s = EntityUtils.toString(res.getEntity());
            TestJSON testJSON = new Gson().fromJson(s, TestJSON.class);
            return testJSON.status;
        } catch (Exception e) {
            return false;
        }
    }

    private void saveChatlog(AbstractLogEntry entry) {
        HttpPost postRequest = new HttpPost(BASE_URL + "/v1/log/chat");
    }
}
