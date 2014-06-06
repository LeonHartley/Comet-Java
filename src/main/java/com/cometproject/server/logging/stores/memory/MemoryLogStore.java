package com.cometproject.server.logging.stores.memory;

import com.cometproject.server.logging.AbstractLogEntry;
import com.cometproject.server.logging.AbstractLoggerStore;
import com.cometproject.server.logging.LogEntryType;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MemoryLogStore extends AbstractLoggerStore {
    private Logger log = Logger.getLogger(MemoryLogStore.class.getName());

    private Map<LogEntryType, Set<AbstractLogEntry>> entries;

    public MemoryLogStore() {
        this.entries = new FastMap<>();
    }

    @Override
    public List<AbstractLogEntry> getByTimeFrame(long min, long max) {
        return null;
    }

    @Override
    public List<AbstractLogEntry> getByRoomId(int roomId) {
        return null;
    }

    @Override
    public List<AbstractLogEntry> getByUserId(int userId) {
        return null;
    }

    @Override
    public void put(AbstractLogEntry entry) {
        if(!entries.containsKey(entry.getType())) {
            entries.put(entry.getType(), new FastSet<>());
        }

        entries.get(entry.getType()).add(entry);
        log.trace("Entry was added to the store, data: " + entry.getString());
    }
}
