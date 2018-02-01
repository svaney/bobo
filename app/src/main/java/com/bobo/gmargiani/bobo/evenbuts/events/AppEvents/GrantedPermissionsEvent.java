package com.bobo.gmargiani.bobo.evenbuts.events.AppEvents;

import java.util.ArrayList;

/**
 * Created by gmargiani on 2/1/2018.
 */

public class GrantedPermissionsEvent {
    private ArrayList<String> grantedPermissions;
    private int requestCode;

    public GrantedPermissionsEvent(ArrayList<String> grantedPermissions, int requestCode) {
        this.grantedPermissions = grantedPermissions;
        this.requestCode = requestCode;
    }

    public ArrayList<String> getGrantedPermissions() {
        return grantedPermissions;
    }

    public int getRequestCode() {
        return requestCode;
    }
}
