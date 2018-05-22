package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.StatementItem;

import java.util.ArrayList;

public class StatementSearchEvent extends RootEvent {
    private String searchQuery;

    private boolean canLoadMore = true;
    private int from;

    private ArrayList<StatementItem> statements = new ArrayList<>();

    public StatementSearchEvent() {

    }

    public StatementSearchEvent(String searchQuery) {
        setSearchQuery(searchQuery);
    }

    @Override
    public Object copyData() {
        StatementSearchEvent ev = new StatementSearchEvent();
        ev.setCanLoadMore(canLoadMore());
        ev.setSearchQuery(getSearchQuery());
        ev.setFrom(getFrom());
        ev.setStatements(getStatements());
        return ev;
    }

    public void addStatements(ArrayList<StatementItem> items) {
        if (statements == null) {
            statements = new ArrayList<>();
        }

        if (items != null) {
            statements.addAll(items);
        }
    }

    public ArrayList<StatementItem> getStatements() {
        return statements;
    }

    public void setStatements(ArrayList<StatementItem> statements) {
        this.statements = statements;
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
