package com.cometproject.server.logging.containers;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.logging.AbstractLogEntry;
import com.cometproject.server.logging.database.queries.LogQueries;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManagement;
import com.google.common.collect.Table;
import javolution.util.FastTable;

import java.util.concurrent.TimeUnit;

public class LogEntryContainer implements CometTask {
    private FastTable<AbstractLogEntry> entriesToSave = new FastTable<>();
    private FastTable<AbstractLogEntry> entriesPending = new FastTable<>();

    private boolean isWriting = false;

    public LogEntryContainer() {
        Comet.getServer().getThreadManagement().executePeriodic(this, 500, 500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        if(this.entriesToSave.size() < 1) return;

        this.isWriting = true;

        LogQueries.putEntryBatch(this.entriesToSave);

        this.entriesToSave.clear();

        for(AbstractLogEntry pendingEntry : this.entriesPending) {
            this.entriesToSave.add(pendingEntry);
        }

        this.entriesPending.clear();

        this.isWriting = false;
    }

    public void put(AbstractLogEntry logEntry) {
        if (this.isWriting) {
            this.entriesPending.add(logEntry);
        } else {
            this.entriesToSave.add(logEntry);
        }
    }

}
