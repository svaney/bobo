package com.bobo.gmargiani.bobo.rest;

import com.bobo.gmargiani.bobo.model.AppVersion;
import com.bobo.gmargiani.bobo.model.KeyValue;
import com.bobo.gmargiani.bobo.model.LogInData;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.model.Token;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    @POST("/statements/similar")
    Call<ApiResponse<ArrayList<StatementItem>>> getSimilarStatements(@Body RetrofitApi.CategoryId categoryId);

    @POST("/users/register")
    Call<ApiResponse<Object>> registerUser(@Body RetrofitApi.RegisterUser user);

    @POST("/users/login")
    Call<ApiResponse<LogInData>> logIn(@Body RetrofitApi.LogIn logIn);

    @POST("/users/getInfoByToken")
    Call<ApiResponse<OwnerDetails>> logInByToken(@Header("Authorization") String token);

    @POST("/users/getusers")
    Call<ApiResponse<ArrayList<OwnerDetails>>> getUsers(@Body RetrofitApi.SearchUser token);

    @POST("/statements/search")
    Call<ApiResponse<ArrayList<StatementItem>>> getUserStatements(@Body RetrofitApi.OwnerStatements body);

    @POST("/users/setFavourite")
    Call<ApiResponse<Object>> setFavorite(@Header("Authorization") String token, @Body RetrofitApi.FavoriteStatement favorite);

    @POST("/users/subscribeUser")
    Call<ApiResponse<Object>> setSubscribed(@Header("Authorization") String token, @Body RetrofitApi.SubscribeUser subscribe);
}
