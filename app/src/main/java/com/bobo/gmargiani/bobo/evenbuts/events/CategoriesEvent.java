package com.bobo.gmargiani.bobo.evenbuts.events;


import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.KeyValue;

import java.util.ArrayList;

/**
 * Created by gmarg on 4/20/2018.
 */

public class CategoriesEvent extends RootEvent {
    private ArrayList<KeyValue> categories;

    public ArrayList<KeyValue> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<KeyValue> categories) {
        this.categories = categories;
    }

    @Override
    public Object copyData() {
        return null;
    }
}
