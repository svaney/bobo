package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.LogInData;
import com.bobo.gmargiani.bobo.model.OwnerDetails;


public class LogInEvent extends RootEvent {
    private boolean isLoggedIn;
    private LogInData logInData;

    public LogInData getLogInData() {
        return logInData;
    }

    public void setLogInData(LogInData logInData) {
        this.logInData = logInData;
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
