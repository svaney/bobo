package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.OwnerDetails;

public class OwnerDetailsEvent extends RootEvent {
    private long ownerId;
    private OwnerDetails ownerDetails;


    public OwnerDetails getOwnerDetails() {
        return ownerDetails;
    }

    public void setOwnerDetails(OwnerDetails ownerDetails) {
        this.ownerDetails = ownerDetails;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public Object copyData() {
        OwnerDetailsEvent event = new OwnerDetailsEvent();
        event.setOwnerDetails(getOwnerDetails());
        event.setOwnerId(getOwnerId());
        return event;
    }

}
