package com.cometproject.server.game.players.components;

import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.players.types.PlayerComponent;
import com.cometproject.server.storage.queries.pets.PetDao;

import java.util.Map;


public class PetComponent implements PlayerComponent {
    private Player player;
    private Map<Integer, PetData> pets;

    public PetComponent(Player player) {
        this.player = player;

        this.pets = PetDao.getPetsByPlayerId(player.getId());
    }

    public PetData getPet(int id) {
        if (this.getPets().containsKey(id)) {
            return this.getPets().get(id);
        }

        return null;
    }

    public void clearPets() {
        this.pets.clear();
    }

    public void addPet(PetData petData) {
        this.pets.put(petData.getId(), petData);
    }

    public void removePet(int id) {
        this.pets.remove(id);
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    public void dispose() {
        this.pets.clear();
        this.pets = null;
        this.player = null;
    }

    public Map<Integer, PetData> getPets() {
        return this.pets;
    }
}
