//package com.cometproject.server.logging.old;

//
//import com.cometproject.server.boot.Comet;
//import com.cometproject.server.logging.old.objects.TestJSON;
//import com.cometproject.server.tasks.CometTask;
//import com.google.gson.Gson;
//import javolution.util.FastTable;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.utils.HttpClientUtils;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.apache.log4j.Logger;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicBoolean;


//
//public class LoggingQueue implements CometTask {
//    private final static Logger log = Logger.getLogger(LoggingQueue.class);
//
//    private final String BASE_URL = Comet.getServer().getConfig().get("comet.game.logging.baseurl");
//
//    private FastTable<AbstractLogEntry> loggingEntries = new FastTable<>();
//    private FastTable<AbstractLogEntry> pendingLogEntries = new FastTable<>();
//
//    private AtomicBoolean isWriting = new AtomicBoolean(false);
//    private CloseableHttpClient httpClient = HttpClients.custom()
//            .disableAutomaticRetries()
//            .disableCookieManagement()
//            .disableRedirectHandling()
//            .build();
//
//    private AtomicBoolean isLoggingActive = new AtomicBoolean(true);
//    private String token;
//
//    // the amount of rounds to retry if service is unavailable
//    private int INACTIVE_RETRY_ROUNDS = 60;
//
//    private int retryCount = 0;
//
//    public LoggingQueue(String token) {
//        this.token = token;
//    }
//
//    public void addEntry(AbstractLogEntry entry) {
//        // Checks for if the logging server is responding
//        if (!this.isLoggingActive.get()) { return; }
//
//        if (this.isWriting.get()) {
//            this.pendingLogEntries.add(entry);
//        } else {
//            this.loggingEntries.add(entry);
//        }
//    }
//
//    @Override
//    public void run() {
//        // inactive retry
//        if (!this.isLoggingActive.get()) {
//            if (retryCount < INACTIVE_RETRY_ROUNDS) {
//                retryCount++;
//                return;
//            } else {
//                retryCount = 0;
//            }
//        }
//
//        // check if the logging service is responding
//        if (!this.isLoggingOnline()) {
//            this.isLoggingActive.set(false);
//            log.trace("Log server is offline, will retry soon");
//            return;
//        } else {
//            if (!this.isLoggingActive.get()) {
//                this.isLoggingActive.set(true);
//                log.trace("Log server is back online");
//            }
//        }
//
//        this.isWriting.set(true);
//
//        for (AbstractLogEntry entry : this.loggingEntries) {
//            if (entry.getType() != null) {
//                if (entry.getType() == LogType.ROOM_CHATLOGS) {
//                    this.saveChatlog(entry);
//                }
//            }
//        }
//
//        this.loggingEntries.clear();
//
//        this.isWriting.set(false);
//
//        // shift the pending entries to the active queue for next round
//        for (AbstractLogEntry entry : this.pendingLogEntries) {
//            this.loggingEntries.add(entry);
//        }
//
//        this.pendingLogEntries.clear();
//    }
//
//    private boolean isLoggingOnline() {
//        log.debug("Testing if logging server is online");
//
//        HttpGet getRequest = new HttpGet(BASE_URL + "/v1/test");
//
//        try (CloseableHttpResponse res = this.httpClient.execute(getRequest)) {
//            String s = EntityUtils.toString(res.getEntity());
//            TestJSON testJSON = new Gson().fromJson(s, TestJSON.class);
//            return testJSON.status;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    private void saveChatlog(AbstractLogEntry entry) {
//        log.debug("Sending chat log entries to logging server");
//
//        try {
//            HttpPost postRequest = new HttpPost(BASE_URL + "/v1/chatlogs/room/add");
//            int roomId = entry.getRoomId();
//            int userId = entry.getPlayerId();
//            String message = entry.getString();
//
//            List<NameValuePair> postParameters = new ArrayList<>();
//            postParameters.add(new BasicNameValuePair("room_id", String.valueOf(roomId)));
//            postParameters.add(new BasicNameValuePair("user_id", String.valueOf(userId)));
//            postParameters.add(new BasicNameValuePair("message", message));
//
//            postRequest.setEntity(new UrlEncodedFormEntity(postParameters));
//
//            try (CloseableHttpResponse res = this.httpClient.execute(postRequest)) {
//                HttpClientUtils.closeQuietly(res);
//            }
//        } catch (Exception e) { e.printStackTrace(); }
//    }
//}
