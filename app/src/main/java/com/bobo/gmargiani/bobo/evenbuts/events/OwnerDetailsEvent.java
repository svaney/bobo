package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.OwnerDetails;

public class OwnerDetailsEvent extends RootEvent {
    private long ownerId;


    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public Object copyData() {
        OwnerDetailsEvent event = new OwnerDetailsEvent();
        event.setOwnerId(getOwnerId());
        return event;
    }

}
