package com.mentornity.ecosytemfeed;

public class RegionListItem {
    private String title;
    private int id;
    private int orderIndex;
    private int groupId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public RegionListItem(String title, int id, int orderIndex, int groupId) {
        this.title = title;
        this.id = id;
        this.orderIndex = orderIndex;
        this.groupId = groupId;
    }

    public int getId() {
        return id;
    }

    public int getGroupId() {
        return groupId;
    }
}
