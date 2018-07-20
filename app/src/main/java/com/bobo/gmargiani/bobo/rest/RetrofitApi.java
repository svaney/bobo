package com.bobo.gmargiani.bobo.rest;

import com.bobo.gmargiani.bobo.model.AppVersion;
import com.bobo.gmargiani.bobo.model.KeyValue;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.model.Token;

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
    public void getStatements(int from, int count, boolean selling, boolean renting, String categoryId, String locationId, BigDecimal priceFrom,
                              BigDecimal priceTo, String orderBy, RestCallback<ApiResponse<ArrayList<StatementItem>>> callback) {

        StatementJson js = new StatementJson(from, count, selling, renting, categoryId, locationId, priceFrom, priceTo, orderBy, "");
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
    public void getUserDetails(RestCallback<ApiResponse<OwnerDetails>> callback) {

    }

    @Override
    public void getSimilarStatements(String statementId, RestCallback<ApiResponse<ArrayList<StatementItem>>> callback) {
        Call<ApiResponse<ArrayList<StatementItem>>> call = retService.getSimilarStatements(statementId);
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void registerUser(boolean isCompany, String firstName, String lastName, String companyName, String password, String email, String phoneNum, RestCallback<ApiResponse<Object>> callback) {
        Call<ApiResponse<Object>> call = retService.registerUser(new RegisterUser(email, password, isCompany, firstName, lastName, phoneNum));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void logIn(String email, String password, RestCallback<ApiResponse<Token>> callback) {
        Call<ApiResponse<Token>> call = retService.logIn(new LogIn(email, password));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    public class RegisterUser {
        String email;
        String password;
        boolean isCompany;
        String firstName;
        String lastName;
        String phoneNum;

        public RegisterUser(String email, String password, boolean isCompany, String firstName, String lastName, String phoneNum) {
            this.email = email;
            this.password = password;
            this.isCompany = isCompany;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phoneNum = phoneNum;
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
        //   boolean renting;
        //    String categoryId;
        //    String locationId;
        //     BigDecimal priceFrom;
        BigDecimal priceTo;
        //      String orderBy;
        String searchQuery;

        public StatementJson(int from, int count, boolean selling, boolean renting, String categoryId, String locationId,
                             BigDecimal priceFrom, BigDecimal priceTo, String orderBy, String searchQuery) {
            this.from = from;
            this.count = count;
            this.selling = selling;
            //      this.renting = renting;
            //      this.categoryId = categoryId;
            //      this.locationId = locationId;
            //      this.priceFrom = priceFrom;
            this.priceTo = priceTo;
            //      this.orderBy = orderBy;
            this.searchQuery = searchQuery;
        }
    }
}
