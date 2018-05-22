package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.OwnerDetails;

import java.util.ArrayList;

public class OwnerSearchEvent extends RootEvent {
    private String searchQuery;

    private boolean canLoadMore = true;
    private int from;

    private ArrayList<OwnerDetails> owners = new ArrayList<>();

    public OwnerSearchEvent() {

    }

    public OwnerSearchEvent(String searchQuery) {
        setSearchQuery(searchQuery);
    }

    @Override
    public Object copyData() {
        OwnerSearchEvent ev = new OwnerSearchEvent();
        ev.setCanLoadMore(canLoadMore());
        ev.setSearchQuery(getSearchQuery());
        ev.setFrom(getFrom());
        ev.setOwners(getOwners());
        return ev;
    }

    public void addOwners(ArrayList<OwnerDetails> items) {
        if (owners == null) {
            owners = new ArrayList<>();
        }

        if (items != null) {
            owners.addAll(items);
        }
    }

    public ArrayList<OwnerDetails> getOwners() {
        return owners;
    }

    public void setOwners(ArrayList<OwnerDetails> owners) {
        this.owners = owners;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public boolean canLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }
}
