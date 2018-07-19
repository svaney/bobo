package com.bobo.gmargiani.bobo.rest;

import com.bobo.gmargiani.bobo.model.AppVersion;
import com.bobo.gmargiani.bobo.model.KeyValue;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.model.Token;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by gmargiani on 1/30/2018.
 */

public interface RetrofitService {

    @GET("./getAppStatus")
    Call<ApiResponse<AppVersion>> getAppVersion();

    @GET("./locations")
    Call<ApiResponse<ArrayList<KeyValue>>> getLocations();

    @GET("./categories")
    Call<ApiResponse<ArrayList<KeyValue>>> getCategories();

    @POST("/statements/search")
    Call<ApiResponse<ArrayList<StatementItem>>> getStatements(@Body RetrofitApi.StatementJson body);

    @POST("/users/details")
    Call<ApiResponse<OwnerDetails>> getOwnerDetails(@Body RetrofitApi.UserId userId);

    @GET("/statements/similar/5b463ef4497cf6625fc3b8bd")
    Call<ApiResponse<ArrayList<StatementItem>>> getSimilarStatements(@Query("statementId") String statementId);

    @POST("/users/register")
    Call<ApiResponse<Object>> registerUser(@Body RetrofitApi.RegisterUser user);

    @POST("/users/login")
    Call<ApiResponse<Token>> logIn(@Body RetrofitApi.LogIn logIn);
}
