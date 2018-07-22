package com.bobo.gmargiani.bobo.rest;

import com.bobo.gmargiani.bobo.model.AppVersion;
import com.bobo.gmargiani.bobo.model.KeyValue;
import com.bobo.gmargiani.bobo.model.LogInData;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.model.Token;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;

import java.math.BigDecimal;
import java.util.ArrayList;

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
    public void getAppVersion(RestCallback<ApiResponse<AppVersion>> callback) {
        Call<ApiResponse<AppVersion>> call = retService.getAppVersion();
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getLocations(RestCallback<ApiResponse<ArrayList<KeyValue>>> callback) {
        Call<ApiResponse<ArrayList<KeyValue>>> call = retService.getLocations();
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getCategories(RestCallback<ApiResponse<ArrayList<KeyValue>>> callback) {
        Call<ApiResponse<ArrayList<KeyValue>>> call = retService.getCategories();
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getStatements(int from, int count, boolean selling, boolean renting, String searchQuery, ArrayList<String> categoryIds, ArrayList<String> locationIds, BigDecimal priceFrom,
                              BigDecimal priceTo, String orderBy, RestCallback<ApiResponse<ArrayList<StatementItem>>> callback) {

        StatementJson js = new StatementJson(from, count, selling, renting, categoryIds, locationIds, priceFrom, priceTo, orderBy, searchQuery);
        Call<ApiResponse<ArrayList<StatementItem>>> call = retService.getStatements(js);
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getOwnerDetails(String ownerId, RestCallback<ApiResponse<OwnerDetails>> callback) {
        Call<ApiResponse<OwnerDetails>> call = retService.getOwnerDetails(new UserId(ownerId));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getSimilarStatements(String categoryId, RestCallback<ApiResponse<ArrayList<StatementItem>>> callback) {
        Call<ApiResponse<ArrayList<StatementItem>>> call = retService.getSimilarStatements(new CategoryId(categoryId));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getUserStatements(String ownerId, int from, int count, RestCallback<ApiResponse<ArrayList<StatementItem>>> callback) {
        Call<ApiResponse<ArrayList<StatementItem>>> call = retService.getUserStatements(new OwnerStatements(from, count, ownerId));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void registerUser(boolean isCompany, String firstName, String lastName, String companyName, String password, String email, String phoneNum, RestCallback<ApiResponse<Object>> callback) {
        Call<ApiResponse<Object>> call = retService.registerUser(new RegisterUser(email, password, isCompany, firstName, lastName, phoneNum, companyName));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void logIn(String email, String password, RestCallback<ApiResponse<LogInData>> callback) {
        Call<ApiResponse<LogInData>> call = retService.logIn(new LogIn(email, password));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void logInByToken(String token, RestCallback<ApiResponse<OwnerDetails>> callback) {
        Call<ApiResponse<OwnerDetails>> call = retService.logInByToken("Bearer " + token);
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getUsers(int from, int to, String searchQuery, RestCallback<ApiResponse<ArrayList<OwnerDetails>>> callback) {
        Call<ApiResponse<ArrayList<OwnerDetails>>> call = retService.getUsers(new SearchUser(from, to, searchQuery));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void setFavorite(String statementId, boolean isFavorite, RestCallback<ApiResponse<Object>> callback) {
        Call<ApiResponse<Object>> call = retService.setFavorite("Bearer " + PreferencesApiManager.getInstance().getToken(), new FavoriteStatement(statementId, isFavorite));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void subscribeUser(String userId, boolean isSubscribed, RestCallback<ApiResponse<Object>> callback) {
        Call<ApiResponse<Object>> call = retService.setSubscribed("Bearer " + PreferencesApiManager.getInstance().getToken(), new SubscribeUser(userId, isSubscribed));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    public class OwnerStatements {
        int from;
        int count;
        String ownerId;

        public OwnerStatements(int from, int to, String ownerId) {
            this.from = from;
            this.count = to;
            this.ownerId = ownerId;
        }
    }

    public class SearchUser {
        int from;
        int count;
        String searchQuery;

        public SearchUser(int from, int to, String searchQuery) {
            this.from = from;
            this.count = to;
            this.searchQuery = searchQuery;
        }
    }

    public class FavoriteStatement {
        String statementId;
        boolean isFavourite;

        public FavoriteStatement(String statementId, boolean isFavourite) {
            this.statementId = statementId;
            this.isFavourite = isFavourite;
        }
    }

    public class SubscribeUser {
        String userId;
        boolean isSubscribed;

        public SubscribeUser(String userId, boolean isSubscribed) {
            this.userId = userId;
            this.isSubscribed = isSubscribed;
        }
    }

    public class CategoryId {
        String categoryId;

        public CategoryId(String categoryId) {
            this.categoryId = categoryId;
        }
    }

    public class RegisterUser {
        String email;
        String password;
        boolean isCompany;
        String firstName;
        String lastName;
        String phoneNum;
        String companyName;

        public RegisterUser(String email, String password, boolean isCompany, String firstName, String lastName, String phoneNum, String companyName) {
            this.email = email;
            this.password = password;
            this.isCompany = isCompany;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phoneNum = phoneNum;
            this.companyName = companyName;
        }
    }

    public class LogIn {
        String email;
        String password;

        public LogIn(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }


    public class UserId {
        String userId;

        public UserId(String userId) {
            this.userId = userId;
        }
    }

    public class StatementJson {
        int from;
        int count;
        boolean selling;
        boolean renting;
        ArrayList<String> categoryIds;
        ArrayList<String> locationIds;
        BigDecimal priceFrom;
        BigDecimal priceTo;
        //   String orderBy;
        String searchQuery;

        public StatementJson(int from, int count, boolean selling, boolean renting, ArrayList<String> categoryIds, ArrayList<String> locationIds,
                             BigDecimal priceFrom, BigDecimal priceTo, String orderBy, String searchQuery) {
            this.from = from;
            this.count = count;
            this.selling = selling;
            this.renting = renting;
            this.categoryIds = categoryIds;
            this.locationIds = locationIds;
            this.priceFrom = priceFrom;
            this.priceTo = priceTo;
            //   this.orderBy = orderBy;
            this.searchQuery = searchQuery;
        }
    }
}
