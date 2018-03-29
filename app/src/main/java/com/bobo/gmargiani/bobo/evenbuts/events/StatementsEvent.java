package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.StatementItem;

import java.util.ArrayList;

public class StatementsEvent extends RootEvent {
    private boolean canLoadMore = true;
    private int from;

    private ArrayList<StatementItem> statements = new ArrayList<>();

    public ArrayList<StatementItem> getStatements() {
        return statements;
    }

    public void setStatements(ArrayList<StatementItem> statements) {
        this.statements = statements;
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

    public void addStatements(ArrayList<StatementItem> items) {
        if (statements == null) {
            statements = new ArrayList<>();
        }

        if (items != null) {
            statements.addAll(items);
        }
    }

    @Override
    public Object copyData() {
        StatementsEvent ev = new StatementsEvent();
        ev.setStatements(getStatements());
        ev.setCanLoadMore(canLoadMore());
        return ev;
    }
}
