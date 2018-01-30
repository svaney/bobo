package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;

/**
 * Created by gmarg on 1/30/2018.
 */

public class AuthorizedEvent extends RootEvent {
    private boolean isAuthorized= false;

    @Override
    public Object copyData() {
        return this;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }
}
