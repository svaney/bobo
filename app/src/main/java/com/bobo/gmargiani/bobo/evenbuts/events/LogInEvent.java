package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.OwnerDetails;


public class LogInEvent extends RootEvent {
    private boolean isLoggedIn;
    private OwnerDetails userDetails;

    public OwnerDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(OwnerDetails userDetails) {
        this.userDetails = userDetails;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    @Override
    public Object copyData() {
        return null;
    }
}
