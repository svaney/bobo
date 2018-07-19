package com.bobo.gmargiani.bobo.rest;

import com.bobo.gmargiani.bobo.model.AppVersion;
import com.bobo.gmargiani.bobo.model.KeyValue;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.model.StatementItem;

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
    public void getOwnerDetails(String ownerId,RestCallback<ApiResponse<OwnerDetails>> callback) {
        Call<ApiResponse<OwnerDetails>> call = retService.getOwnerDetails(new UserId(ownerId));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getSimilarStatements(String statementId, RestCallback<ApiResponse<ArrayList<StatementItem>>> callback) {
        Call<ApiResponse<ArrayList<StatementItem>>> call = retService.getSimilarStatements(statementId);
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    public class UserId{
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
