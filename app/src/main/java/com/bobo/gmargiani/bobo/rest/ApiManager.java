package com.bobo.gmargiani.bobo.rest;

import com.bobo.gmargiani.bobo.model.datamodels.AppVersion;
import com.bobo.gmargiani.bobo.model.NetDataListener;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class ApiManager {
    private NetDataListener dataListener;
    private NetApi netApi;

    public ApiManager(NetApi netApi, NetDataListener dataListener) {
        this.netApi = netApi;
        this.dataListener = dataListener;
    }

    public void getAppVersion(String deviceType, String channel) {
        netApi.getAppVersion(deviceType, channel, new RestCallback<ApiResponse<AppVersion>>() {
            @Override
            public void onResponse(ApiResponse<AppVersion> response) {
                super.onResponse(response);
                dataListener.onAppVersionEvent(response);
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                ApiResponse<AppVersion> response = new ApiResponse<>();
                response.setNetworkFailure(true);
                response.setNetworkFailure(t);
                dataListener.onAppVersionEvent(response);
            }
        });
    }
}
