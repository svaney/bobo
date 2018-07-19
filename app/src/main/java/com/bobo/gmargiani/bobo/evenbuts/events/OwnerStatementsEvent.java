package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.StatementItem;

import org.parceler.Parcel;

import java.util.ArrayList;

public class OwnerStatementsEvent extends RootEvent {
    private String ownerId;
    private ArrayList<StatementItem> ownerStatements;

    @Override
    public Object copyData() {
        OwnerStatementsEvent ev = new OwnerStatementsEvent();
        ev.setOwnerId(getOwnerId());
        ev.setOwnerStatements(getOwnerStatements());
        return null;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public ArrayList<StatementItem> getOwnerStatements() {
        return ownerStatements;
    }

    public void setOwnerStatements(ArrayList<StatementItem> ownerStatements) {
        this.ownerStatements = ownerStatements;
    }
}
