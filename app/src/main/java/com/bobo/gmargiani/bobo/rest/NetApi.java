package com.bobo.gmargiani.bobo.rest;

import com.bobo.gmargiani.bobo.model.datamodels.AppVersion;

/**
 * Created by gmargiani on 1/30/2018.
 */

public abstract class NetApi {
    public final static String COMMON_GET_APP_CLIENT_STATUS = "COMMON_GET_APP_CLIENT_STATUS";
    public final static String SERVICE_IDENTITY_LOGOUT = "IDENTITY_LOGOUT";

    public abstract void getTestData(String deviceType, String channel, RestCallback<ApiResponse<AppVersion>> callback);
}
