package com.cometproject.server.game.rooms.items.types.floor.wired.data;

import com.cometproject.server.game.rooms.items.types.floor.wired.AbstractWiredItem;
import com.cometproject.server.game.rooms.items.types.floor.wired.WiredItemSnapshot;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class WiredItemData {
    private int selectionType = 0;
    private List<Integer> selectedIds;
    private String text;
    private Map<Integer, Integer> params;
    private Map<Integer, WiredItemSnapshot> snapshots;

    public WiredItemData(int selectionType, List<Integer> selectedIds, String text, Map<Integer, Integer> params, Map<Integer, WiredItemSnapshot> snapshots) {
        this.selectionType = selectionType;
        this.selectedIds = selectedIds;
        this.text = text;
        this.params = params;
        this.snapshots = snapshots;
    }

    public WiredItemData() {
        this.selectionType = 0;
        this.selectedIds = Lists.newArrayList();
        this.text = "";
        this.params = Maps.newHashMap();
        this.snapshots = Maps.newHashMap();
    }

    public void putParam(int param, int value) {
        if(this.params.containsKey(param)) {
            if (this.params.get(param) != value) {
                this.params.replace(param, value);
            }
        } else {
            this.params.put(param, value);
        }
    }

    public void removeParam(int param) {
        if(this.params.containsKey(param)) {
            this.params.remove(param);
        }
    }

    public void clearSelectedItems() {
        this.getSelectedIds().clear();
    }

    public void selectItem(int itemId) {
        this.selectedIds.add(itemId);
    }

    public int getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(int selectionType) {
        this.selectionType = selectionType;
    }

    public List<Integer> getSelectedIds() {
        return selectedIds;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<Integer, Integer> getParams() {
        return params;
    }

    public void setParams(Map<Integer, Integer> params) {
        this.params = params;
    }

    public Map<Integer, WiredItemSnapshot> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(Map<Integer, WiredItemSnapshot> snapshots) {
        this.snapshots = snapshots;
    }
}
