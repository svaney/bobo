package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.OwnerDetails;

public class OwnerDetailsEvent extends RootEvent {
    private String ownerId;
    private OwnerDetails ownerDetails;


    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public OwnerDetails getOwnerDetails() {
        return ownerDetails;
    }


    @Override
    public Object copyData() {
        OwnerDetailsEvent event = new OwnerDetailsEvent();
        event.setOwnerId(getOwnerId());
        event.setDetails(getOwnerDetails());
        return event;
    }

    public void setDetails(OwnerDetails result) {
        this.ownerDetails = result;
    }

    @Override
    public void setState(int state) {
        super.setState(state);
    }
}
