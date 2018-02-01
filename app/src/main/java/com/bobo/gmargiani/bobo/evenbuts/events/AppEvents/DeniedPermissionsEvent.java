package com.bobo.gmargiani.bobo.evenbuts.events.AppEvents;

import java.util.ArrayList;

/**
 * Created by gmargiani on 2/1/2018.
 */

public class DeniedPermissionsEvent {
    private ArrayList<String> deniedPermissions;
    private int requestCode;

    public DeniedPermissionsEvent(ArrayList<String> deniedPermissions, int requestCode) {
        this.deniedPermissions = deniedPermissions;
        this.requestCode = requestCode;
    }

    public ArrayList<String> getDeniedPermissions() {
        return deniedPermissions;
    }

    public int getRequestCode() {
        return requestCode;
    }
}
