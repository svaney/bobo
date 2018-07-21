package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.StatementItem;

import java.util.ArrayList;

public class SimilarStatementsEvent extends RootEvent {
    private String categoryId;
    private ArrayList<StatementItem> similarStatements;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public ArrayList<StatementItem> getSimilarStatements() {
        return similarStatements;
    }

    public void setSimilarStatements(ArrayList<StatementItem> similarStatements) {
        this.similarStatements = similarStatements;
    }

    @Override
    public Object copyData() {
        SimilarStatementsEvent ev = new SimilarStatementsEvent();
        ev.setCategoryId(getCategoryId());
        ev.setSimilarStatements(getSimilarStatements());
        return ev;
    }
}
