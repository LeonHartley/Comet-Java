package com.cometproject.api.game.furniture.types;

public interface ILimitedEditionItem  {
    long getItemId();

    int getLimitedRare();

    int getLimitedRareTotal();
}
