package com.cometproject.server.logging.containers;

import com.cometproject.server.logging.AbstractLogEntry;
import com.cometproject.server.logging.database.queries.LogQueries;
import com.cometproject.server.tasks.CometTask;


public class LogEntryContainer implements CometTask {
//    private FastTable<AbstractLogEntry> entriesToSave = new FastTable<>();
//    private FastTable<AbstractLogEntry> entriesPending = new FastTable<>();
//
//    private final Logger logger = Logger.getLogger(LogEntryContainer.class.getName());
//
//    private boolean isWriting = false;

    public LogEntryContainer() {
//        CometThreadManager.getInstance().executePeriodic(this, 500, 500, TimeUnit.MILLISECONDS);
    }

    //
    @Override
    public void run() {
//        if (this.entriesToSave.size() < 1) return;
//
//        this.isWriting = true;
//
//        LogQueries.putEntryBatch(this.entriesToSave);
//
//        logger.debug("Saved " + this.entriesToSave.size() + " log entries to database");
//
//        this.entriesToSave.clear();
//
//        for (AbstractLogEntry pendingEntry : this.entriesPending) {
//            this.entriesToSave.add(pendingEntry);
//        }
//
//        logger.debug("Moved " + this.entriesPending.size() + " pending entries to the save queue");
//
//        this.entriesPending.clear();
//
//        this.isWriting = false;
    }

    public void put(AbstractLogEntry logEntry) {
        LogQueries.putEntry(logEntry);
//
//        if (this.isWriting) {
//            this.entriesPending.add(logEntry);
//        } else {
//            this.entriesToSave.add(logEntry);
//        }
    }
}
