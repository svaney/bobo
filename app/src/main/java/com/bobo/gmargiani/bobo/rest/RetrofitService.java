package com.bobo.gmargiani.bobo.rest;

import com.bobo.gmargiani.bobo.model.AppVersion;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gmargiani on 1/30/2018.
 */

public interface RetrofitService {
    String SERVICE_ID = "serviceId";

    @GET(".")
    Call<ApiResponse<AppVersion>> getTestData(
            @Query(SERVICE_ID) String serviceId,
            @Query("deviceType") String deviceType,
            @Query("channel") String channel
    );
}
