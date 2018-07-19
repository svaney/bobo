package com.bobo.gmargiani.bobo.rest;

import com.bobo.gmargiani.bobo.model.AppVersion;
import com.bobo.gmargiani.bobo.model.KeyValue;
import com.bobo.gmargiani.bobo.model.NetDataListener;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.utils.ModelGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class ApiManager {
    private NetDataListener dataListener;
    private NetApi netApi;

    public ApiManager(NetApi netApi, NetDataListener dataListener) {
        this.netApi = netApi;
        this.dataListener = dataListener;
    }

    public void getAppVersion() {
        netApi.getAppVersion(new RestCallback<ApiResponse<AppVersion>>() {
            @Override
            public void onResponse(ApiResponse<AppVersion> response) {
                super.onResponse(response);
                dataListener.onAppVersionEvent(response);
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                ApiResponse<AppVersion> response = new ApiResponse<>();
                response.setCode("-1");
                dataListener.onAppVersionEvent(response);
            }
        });

    }

    public void authorizeByToken(final String token) {
        dataListener.onAuthorizeByTokenEvent(ModelGenerator.generateTokeResponse(), token);
    }


    public void getStatements(final int from, final int count, final boolean sell, final boolean rent,
                              final String category, final String location, final BigDecimal priceFrom,
                              final BigDecimal priceTo, final String orderBy) {

        System.out.println("request statements - from: " + from + "; count: " + count + "; sell: " + sell
                + "; rent: " + rent + "; category: " + category + "; location: " + location + "; priceFrom: " + priceFrom
                + "; priceTo: " + priceTo + "; orderBy: " + orderBy);

        netApi.getStatements(from, count, sell, rent, category, location, priceFrom, priceTo, orderBy, new RestCallback<ApiResponse<ArrayList<StatementItem>>>() {
            @Override
            public void onResponse(ApiResponse<ArrayList<StatementItem>> response) {
                super.onResponse(response);
                dataListener.onStatementsEvent(response, from, count,
                        sell, rent, category, location, priceFrom, priceTo, orderBy);
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                ApiResponse<ArrayList<StatementItem>> response = new ApiResponse<>();
                response.setCode("-1");
                dataListener.onStatementsEvent(response, from, count,
                        sell, rent, category, location, priceFrom, priceTo, orderBy);
            }
        });
    }

    public void getLocations() {
        netApi.getLocations(new RestCallback<ApiResponse<ArrayList<KeyValue>>>() {
            @Override
            public void onResponse(ApiResponse<ArrayList<KeyValue>> response) {
                super.onResponse(response);
                dataListener.onLocationsResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                ApiResponse<ArrayList<KeyValue>> response = new ApiResponse<>();
                response.setCode("-1");
                dataListener.onLocationsResponse(response);
            }
        });

    }

    public void getCategories() {
        netApi.getCategories(new RestCallback<ApiResponse<ArrayList<KeyValue>>>() {
            @Override
            public void onResponse(ApiResponse<ArrayList<KeyValue>> response) {
                super.onResponse(response);
                dataListener.onCategoriesResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                ApiResponse<ArrayList<KeyValue>> response = new ApiResponse<>();
                response.setCode("-1");
                dataListener.onCategoriesResponse(response);
            }
        });
    }

    public void getOwnerDetails(final String ownerId) {
        netApi.getOwnerDetails(ownerId, new RestCallback<ApiResponse<OwnerDetails>>() {
            @Override
            public void onResponse(ApiResponse<OwnerDetails> response) {
                super.onResponse(response);
                dataListener.onOwnerInfoDetails(response, ownerId);
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                ApiResponse<OwnerDetails> response = new ApiResponse<>();
                response.setCode("-1");
                dataListener.onOwnerInfoDetails(response, ownerId);
            }
        });
      //  dataListener.onOwnerInfoDetails(ModelGenerator.getOwnerDetails(ownerId), ownerId);
    }

    public void getSimilarStatements(final String statementId) {
        netApi.getSimilarStatements(statementId, new RestCallback<ApiResponse<ArrayList<StatementItem>>>() {
            @Override
            public void onResponse(ApiResponse<ArrayList<StatementItem>> response) {
                super.onResponse(response);
                dataListener.onSimilarStatements(statementId, response);
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                ApiResponse<ArrayList<StatementItem>> response = new ApiResponse<>();
                response.setCode("-1");
                dataListener.onSimilarStatements(statementId, response);
            }
        });
    }

    public void getStatementsByOwner(String ownerId) {
      //  dataListener.onOwnerStatements(ownerId, ModelGenerator.generateStatements(10));
    }

    public void searchStatements(int from, int count, String query) {
     //   dataListener.onSearchStatements(ModelGenerator.generateStatements(count), from, query, count);
    }

    public void searchOwners(int from, int count, String query) {
      //  dataListener.onSearchOwners(ModelGenerator.getOwnerDetails(count), from, query, count);
    }

}
