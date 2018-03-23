package com.bobo.gmargiani.bobo.model;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class AppVersion {
    private String serverStatus;
    private String serverStatusDesc;
    private boolean mustUpdateClient;
    private String mustUpdateClientDesc;

    public String getServerStatus() {
        return serverStatus;
    }

    public String getServerStatusDesc() {
        return serverStatusDesc;
    }

    public boolean isMustUpdateClient() {
        return mustUpdateClient;
    }

    public String getMustUpdateClientDesc() {
        return mustUpdateClientDesc;
    }
}
