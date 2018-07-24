package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.OwnerDetails;

import java.util.ArrayList;

public class SubscribedUsersEvent extends RootEvent {

    private ArrayList<OwnerDetails> users;

    public ArrayList<OwnerDetails> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<OwnerDetails> users) {
        this.users = users;
    }

    @Override
    public Object copyData() {
        return null;
    }
}
