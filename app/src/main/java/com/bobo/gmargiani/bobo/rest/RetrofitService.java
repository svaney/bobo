package com.bobo.gmargiani.bobo.rest;

import com.bobo.gmargiani.bobo.model.AppVersion;
import com.bobo.gmargiani.bobo.model.KeyValue;
import com.bobo.gmargiani.bobo.model.LogInData;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.model.Token;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Call<ApiResponse<ArrayList<OwnerDetails>>> getOwnerDetails(@Body RetrofitApi.UserId userIds);

    @POST("/statements/similar")
    Call<ApiResponse<ArrayList<StatementItem>>> getSimilarStatements(@Body RetrofitApi.CategoryId categoryId);

    @POST("/users/register")
    @Multipart
    Call<ApiResponse<Object>> registerUser(@Part MultipartBody.Part avatar,
                                           @Part("email") RequestBody email,
                                           @Part("password") RequestBody password,
                                           @Part("isCompany") RequestBody isCompany,
                                           @Part("firstName") RequestBody firstName,
                                           @Part("lastName") RequestBody lastName,
                                           @Part("phoneNum") RequestBody phoneNum,
                                           @Part("companyName") RequestBody companyName);

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
