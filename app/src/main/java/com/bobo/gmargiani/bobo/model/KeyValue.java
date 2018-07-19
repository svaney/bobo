package com.bobo.gmargiani.bobo.model;

/**
 * Created by gmarg on 4/20/2018.
 */

public class KeyValue {
    private String _id;
    private String name;

    public KeyValue() {

    }

    public KeyValue(String key, String value) {
        this._id = key;
        this.name = value;
    }

    public String getKey() {
        return _id;
    }

    public void setKey(String key) {
        this._id = key;
    }

    public String getValue() {
        return name;
    }

    public void setValue(String value) {
        this.name = value;
    }
}
