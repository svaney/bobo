package com.bobo.gmargiani.bobo.rest;

import com.bobo.gmargiani.bobo.model.AppVersion;
import com.bobo.gmargiani.bobo.model.NetDataListener;
import com.bobo.gmargiani.bobo.utils.ModelGenerator;

import java.math.BigDecimal;

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

    public void getAppVersion(String deviceType, String channel) {
        netApi.getAppVersion(deviceType, channel, new RestCallback<ApiResponse<AppVersion>>() {
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


    public void getStatements(int from, int count, boolean sell, boolean rent,
                              String category, String location, BigDecimal priceFrom,
                              BigDecimal priceTo, String orderBy) {

        System.out.println("request statements - from: " + from + "; count: " + count + "; sell: " + sell
                + "; rent: " + rent + "; category: " + category + "; location: " + location + "; priceFrom: " + priceFrom
                + "; priceTo: " + priceTo + "; orderBy: " + orderBy);
        dataListener.onStatementsEvent(ModelGenerator.generateStatements(count), from, count,
                sell, rent, category, location, priceFrom, priceTo, orderBy);
    }

    public void getLocations() {
        dataListener.onLocationsResponse(ModelGenerator.getLocations());
    }

    public void getCategories() {
        dataListener.onCategoriesResponse(ModelGenerator.getCategories());
    }
}
