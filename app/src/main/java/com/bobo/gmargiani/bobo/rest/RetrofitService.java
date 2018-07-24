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

    @POST("/statements/create")
    @Multipart
    Call<ApiResponse<Object>> createStatement(@Header("Authorization") String token,
                                              @Part("title") RequestBody titleBody,
                                              @Part("description") RequestBody descriptionBody,
                                              @Part("price") RequestBody priceBody,
                                              @Part("locationId") RequestBody locationBody,
                                              @Part("categoryId") RequestBody categoryBody,
                                              @Part("lat") RequestBody latBody,
                                              @Part("lon") RequestBody lngBody,
                                              @Part("selling") RequestBody sellingBody,
                                              @Part("renting") RequestBody rentingBody,
                                              @Part MultipartBody.Part photo1,
                                              @Part MultipartBody.Part photo2,
                                              @Part MultipartBody.Part photo3,
                                              @Part MultipartBody.Part photo4,
                                              @Part MultipartBody.Part photo5);

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

    @POST("/statements/getStatementsByIds")
    Call<ApiResponse<ArrayList<StatementItem>>> getStatementsByIds(@Body RetrofitApi.FavoriteStatements favoriteStatements);

    @POST("/statements/delete")
    Call<ApiResponse<Object>> deleteStatement(@Header("Authorization") String token, @Body RetrofitApi.StatementId statementId);

    @POST("/users/update")
    @Multipart
    Call<ApiResponse<OwnerDetails>> updateUser(@Header("Authorization") String token, @Part("password") RequestBody password, @Part MultipartBody.Part avatar,
                                               @Part("firstName") RequestBody firstName, @Part("lastName") RequestBody lastName, @Part("phoneNum") RequestBody phoneNum, @Part("company") RequestBody company);

    @POST("/statements/update")
    @Multipart
    Call<ApiResponse<Object>> updateStatement(@Header("Authorization") String token, @Part("statementId") RequestBody statementId, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("price") RequestBody price,
                                              @Part("locationId") RequestBody locationId, @Part("categoryId") RequestBody categoryId, @Part("lat") RequestBody lat,
                                              @Part("lon") RequestBody lon, @Part("selling") RequestBody selling, @Part("renting") RequestBody renting, @Part("isArchived") RequestBody isArchived,
                                              @Part MultipartBody.Part photos1, @Part MultipartBody.Part photos2, @Part MultipartBody.Part photos3, @Part MultipartBody.Part photos4, @Part MultipartBody.Part photos5);
}
