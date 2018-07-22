package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.StatementItem;

import java.util.ArrayList;

/**
 * Created by gmarg on 7/22/2018.
 */

public class FavoriteStatementsEvent extends RootEvent {
    private ArrayList<StatementItem> statementItems;


    public ArrayList<StatementItem> getStatementItems() {
        return statementItems;
    }

    public void setStatementItems(ArrayList<StatementItem> statementItems) {
        this.statementItems = statementItems;
    }

    @Override
    public Object copyData() {
        return null;
    }

}
