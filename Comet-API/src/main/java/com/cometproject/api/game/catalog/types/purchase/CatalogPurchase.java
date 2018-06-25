package com.cometproject.api.game.catalog.types.purchase;

/**
 * Catalog purchase object used for batching multiple purchases together
 */
public class CatalogPurchase {
    /**
     * The ID of the player who purchased the item
     */
    private int playerId;

    /**
     * The item definition ID of the item
     */
    private int itemBaseId;

    /**
     * The data generated for items such as trophies etc
     */
    private String data;

    /**
     * Initialize the catalog purchase object
     *
     * @param playerId   The ID of the player who purchased the item
     * @param itemBaseId The item definition ID of the item
     * @param data       The data generated for items such as trophies etc
     */
    public CatalogPurchase(int playerId, int itemBaseId, String data) {
        this.playerId = playerId;
        this.itemBaseId = itemBaseId;
        this.data = data;
    }

    /**
     * Get the player ID of the player who purchased the item
     *
     * @return The ID of the player who purchased the item
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Get the item definition ID
     *
     * @return The item definition ID
     */
    public int getItemBaseId() {
        return itemBaseId;
    }

    /**
     * Get the data generated for items such as trophies etc
     *
     * @return The data generated for items such as trophies etc
     */
    public String getData() {
        return data;
    }
}
