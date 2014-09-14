package com.cometproject.server.game.rooms.items.types.floor.wired.data;

import com.cometproject.server.game.rooms.items.types.floor.wired.WiredItemSnapshot;

import java.util.ArrayList;
import java.util.List;

public class WiredItemData {
    private int selectionType = 0;
    private List<Integer> selectedIds;
    private String text;
    private int[] params;
    private List<WiredItemSnapshot> snapshots;

    public WiredItemData(int selectionType, List<Integer> selectedIds, String text, int[] params, List<WiredItemSnapshot> snapshots) {
        this.selectionType = selectionType;
        this.selectedIds = selectedIds;
        this.text = text;
        this.params = params;
        this.snapshots = snapshots;
    }

    public WiredItemData() {
        this.selectionType = 0;
        this.selectedIds = new ArrayList<>();
        this.text = "";
        this.params = new int[0];
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

    public void setSelectedIds(List<Integer> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int[] getParams() {
        return params;
    }

    public void setParams(int[] params) {
        this.params = params;
    }

    public List<WiredItemSnapshot> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(List<WiredItemSnapshot> snapshots) {
        this.snapshots = snapshots;
    }
}
