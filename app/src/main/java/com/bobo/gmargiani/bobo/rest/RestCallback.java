package com.bobo.gmargiani.bobo.rest;

import retrofit2.Call;

/**
 * Created by gmargiani on 1/30/2018.
 */

public abstract class RestCallback<ContentType>{
    private Call mCall;

    public void setCall(Call mCall) {
        this.mCall = mCall;
    }

    public void cancel(){
        mCall.cancel();
    }

    public void onResponse(ContentType response){
    }

    public void onFailure(Throwable t){
    }
}