package com.bobo.gmargiani.bobo.model;

/**
 * Created by gmarg on 7/21/2018.
 */

public class LogInData {
    private String token;
    private OwnerDetails user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public OwnerDetails getUserDetails() {
        return user;
    }

    public void setUserDetails(OwnerDetails userDetails) {
        this.user = userDetails;
    }
}
