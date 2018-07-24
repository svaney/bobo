package com.bobo.gmargiani.bobo.rest;

import android.graphics.Bitmap;

import com.bobo.gmargiani.bobo.model.AppVersion;
import com.bobo.gmargiani.bobo.model.KeyValue;
import com.bobo.gmargiani.bobo.model.LogInData;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.model.Token;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by gmargiani on 1/30/2018.
 */

public abstract class NetApi {

    public abstract void getAppVersion(RestCallback<ApiResponse<AppVersion>> callback);

    public abstract void getLocations(RestCallback<ApiResponse<ArrayList<KeyValue>>> restCallback);

    public abstract void getCategories(RestCallback<ApiResponse<ArrayList<KeyValue>>> restCallback);

    public abstract void getStatements(int from, int count, boolean sell, boolean rent, String searchQuery, ArrayList<String> categories, ArrayList<String> locations, BigDecimal priceFrom, BigDecimal priceTo, String orderBy, RestCallback<ApiResponse<ArrayList<StatementItem>>> restCallback);

    public abstract void getOwnerDetails(String ownerId, RestCallback<ApiResponse<ArrayList<OwnerDetails>>> restCallback);

    public abstract void getSimilarStatements(String setCategoryId, RestCallback<ApiResponse<ArrayList<StatementItem>>> callback);

    public abstract void registerUser(boolean isCompany, String firstName, String lastName, String companyName, String password, String email, String phoneNum, File bitmap, RestCallback<ApiResponse<Object>> callback);

    public abstract void logIn(String email, String password, RestCallback<ApiResponse<LogInData>> callback);

    public abstract void logInByToken(String token, RestCallback<ApiResponse<OwnerDetails>> callback);

    public abstract void getUsers(int from, int to, String searchQuery, RestCallback<ApiResponse<ArrayList<OwnerDetails>>> callback);

    public abstract void getUserStatements(String ownerId, int from, int count, RestCallback<ApiResponse<ArrayList<StatementItem>>> restCallback);

    public abstract void setFavorite(String statementId, boolean isFavorite, RestCallback<ApiResponse<Object>> callback);

    public abstract void subscribeUser(String userId, boolean isSubscribed, RestCallback<ApiResponse<Object>> callback);

    public abstract void requestFavoriteStatements(ArrayList<String> favourites, RestCallback<ApiResponse<ArrayList<StatementItem>>> callback);

    public abstract void getSubscribedUserDetails(ArrayList<String> userIds, RestCallback<ApiResponse<ArrayList<OwnerDetails>>> callback);

    public abstract void createStatement(String title, String description, String price, String locationId, String categoryId,
                                         double lat, double lng, boolean selling, boolean renting, ArrayList<File> userImageFiles, RestCallback<ApiResponse<Object>> objectApiResponse);
}
