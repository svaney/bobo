package com.bobo.gmargiani.bobo.rest;

import com.bobo.gmargiani.bobo.model.datamodels.AppVersion;

import retrofit2.Call;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class RetrofitApi extends NetApi {
    private static RetrofitService retService;
    private RetrofitClient retrofitClient;

    public RetrofitApi(RetrofitClient retrofitClient) {
        if (retService == null) {
            this.retrofitClient = retrofitClient;
            retService = retrofitClient.getService();
        }
    }

    @Override
    public void getTestData(String deviceType, String channel, RestCallback<ApiResponse<AppVersion>> callback) {
        Call<ApiResponse<AppVersion>> call = retService.getTestData(COMMON_GET_APP_CLIENT_STATUS, deviceType, channel);
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }
}
