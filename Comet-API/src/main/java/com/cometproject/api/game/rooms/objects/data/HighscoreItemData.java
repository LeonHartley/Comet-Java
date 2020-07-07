package com.cometproject.api.game.rooms.objects.data;

import com.cometproject.api.networking.messages.IComposer;

public class HighscoreItemData extends ItemData {
    private final String state;
    private final int scoreType;
    private final int clearType;
    private final HighscoreEntry[] entries;

    public HighscoreItemData(String state, int scoreType, int clearType, HighscoreEntry[] entries) {
        this.state = state;
        this.scoreType = scoreType;
        this.clearType = clearType;
        this.entries = entries;
    }

    @Override
    public void compose(IComposer composer) {
        composer.writeInt(6);
        composer.writeInt(scoreType);
        composer.writeInt(clearType);
        composer.writeInt(entries.length);

        for (HighscoreEntry entry : entries) {
            composer.writeInt(entry.getScore());
            composer.writeInt(entry.getPlayers().length);
            for (String player : entry.getPlayers()) {
                composer.writeString(player);
            }
        }
    }
}
