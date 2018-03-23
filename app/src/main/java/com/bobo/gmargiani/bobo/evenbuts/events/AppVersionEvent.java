package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.AppVersion;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class AppVersionEvent extends RootEvent {
    private AppVersion appVersion;

    @Override
    public Object copyData() {
        AppVersionEvent ts = new AppVersionEvent();

        ts.setState(getState());
        ts.setAppVersion(getAppVersion());

        return ts;
    }

    public void setAppVersion(AppVersion appVersion) {
        this.appVersion = appVersion;
    }

    public AppVersion getAppVersion() {
        return appVersion;
    }
}
