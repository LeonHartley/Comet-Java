package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.storage.queries.rooms.RoomFilterDao;

import java.util.Set;

public class FilterComponent {

    private final Room room;
    private final Set<String> filteredWords;

    public FilterComponent(Room room) {
        this.room = room;

        this.filteredWords = RoomFilterDao.getFilterForRoom(room.getId());
    }

    public void add(String word) {
        this.filteredWords.add(word);
        RoomFilterDao.saveWord(word, this.room.getId());
    }

    public void remove(String word) {
        this.filteredWords.remove(word);
        RoomFilterDao.removeWord(word, this.room.getId());
    }

    public String filter(PlayerEntity entity, String message) {
        String msg = message;

        if (!entity.hasRights()) {
            for (String word : this.filteredWords) {
                if (message.contains(word)) {
                    msg = msg.replace(word, Locale.getOrDefault("filter.bobba", "bobba"));
                }
            }
        }

        return msg;
    }

    public Set<String> getFilteredWords() {
        return this.filteredWords;
    }
}
