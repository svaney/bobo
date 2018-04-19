package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.KeyValue;

import java.util.ArrayList;

/**
 * Created by gmarg on 4/20/2018.
 */

public class LocationsEvent extends RootEvent {
    private ArrayList<KeyValue> locations;

    public ArrayList<KeyValue> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<KeyValue> locations) {
        this.locations = locations;
    }

    @Override
    public Object copyData() {
        return null;
    }
}
