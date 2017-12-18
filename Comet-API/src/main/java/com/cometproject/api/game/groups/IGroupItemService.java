package com.cometproject.api.game.groups;

import com.cometproject.api.game.groups.items.IGroupBadgeItem;

import java.util.List;
import java.util.Map;

public interface IGroupItemService {

    List<IGroupBadgeItem> getBases();

    List<IGroupBadgeItem> getSymbols();

    List<IGroupBadgeItem> getBaseColours();

    Map<Integer, IGroupBadgeItem> getSymbolColours();

    Map<Integer, IGroupBadgeItem> getBackgroundColours();

    void load();
}
