package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;

/**
 * Created by gmargiani on 3/27/2018.
 */

public class TokenAuthorizationEvent extends RootEvent {
    private String token;
    private boolean isAuthorized;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }

    @Override
    public Object copyData() {
        TokenAuthorizationEvent temp = new TokenAuthorizationEvent();
        temp.setToken(getToken());
        temp.setAuthorized(false);
        return temp;
    }
}
