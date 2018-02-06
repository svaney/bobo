package com.bobo.gmargiani.bobo.rest;

import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class RetrofitCallback<ContentType> implements Callback<ContentType> {
    private RestCallback<ContentType> mCallback;

    public RetrofitCallback(RestCallback<ContentType> callback) {
        this.mCallback = callback;
    }

    @Override
    public void onResponse(Call<ContentType> call, Response<ContentType> response) {
        String requestSessionId = response.raw().request().url().queryParameter(RetrofitClient.PARAMETER_SESSION);
        String serviceId = call.request().url().queryParameter(RetrofitService.SERVICE_ID);


        if (response.body() instanceof ApiResponse) {
            if (Utils.equals(requestSessionId, RetrofitClient.getSessionIdToCheck())) {
                if (response.body() == null) {
                    mCallback.onFailure(new Throwable());
                } else {
                    if (!((ApiResponse) response.body()).isInvalidSession()) {
                        mCallback.onResponse(response.body());
                    }
                }
            }
        } else {
            mCallback.onFailure(new Throwable());
        }
    }

    @Override
    public void onFailure(Call<ContentType> call, Throwable t) {
        mCallback.onFailure(t);
    }
}
