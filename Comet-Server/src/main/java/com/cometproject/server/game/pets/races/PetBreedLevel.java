package com.cometproject.server.game.pets.races;

public enum PetBreedLevel {
    COMMON(3),
    UNCOMMON(2),
    RARE(1),
    EPIC(0);

    private int levelId;

    PetBreedLevel(int levelId) {
        this.levelId = levelId;
    }

    public int getLevelId() {
        return this.levelId;
    }
}
