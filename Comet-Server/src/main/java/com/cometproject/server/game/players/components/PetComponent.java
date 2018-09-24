package com.cometproject.server.game.players.components;

import com.cometproject.api.game.pets.IPetData;
import com.cometproject.api.game.players.IPlayer;
import com.cometproject.api.game.players.data.components.PlayerPets;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.players.types.PlayerComponent;
import com.cometproject.server.storage.queries.pets.PetDao;

import java.util.Map;


public class PetComponent extends PlayerComponent implements PlayerPets {
    private Player player;
    private Map<Integer, IPetData> pets;

    public PetComponent(IPlayer player) {
        super(player);

        this.pets = PetDao.getPetsByPlayerId(player.getId());
    }

    @Override
    public IPetData getPet(int id) {
        if (this.getPets().containsKey(id)) {
            return this.getPets().get(id);
        }

        return null;
    }

    public void clearPets() {
        this.pets.clear();

        this.getPlayer().flush();
    }

    public void addPet(IPetData petData) {
        this.pets.put(petData.getId(), petData);

        this.getPlayer().flush();
    }

    public void removePet(int id) {
        this.pets.remove(id);

        this.getPlayer().flush();
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

    @Override
    public Map<Integer, IPetData> getPets() {
        return this.pets;
    }
}
