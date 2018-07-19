package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.StatementItem;

import java.util.ArrayList;

public class SimilarStatementsEvent extends RootEvent {
    private String statementId;
    private ArrayList<StatementItem> similarStatements;

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
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
        ev.setStatementId(getStatementId());
        ev.setSimilarStatements(getSimilarStatements());
        return ev;
    }
}
