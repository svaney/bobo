package com.bobo.gmargiani.bobo.rest;

import com.bobo.gmargiani.bobo.model.AppVersion;
import com.bobo.gmargiani.bobo.model.NetDataListener;
import com.bobo.gmargiani.bobo.model.StatementItem;

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
        ApiResponse<Boolean> resp = new ApiResponse<>();
        resp.setCode("0");
        resp.setResult(false);
        dataListener.onAuthorizeByTokenEvent(resp, token);
    }

    private int itemCount;

    public void getStatements(int from, int count) {
        ArrayList<StatementItem> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            StatementItem item = new StatementItem();
            item.setDescription("aq aris agcerili ranairia da ravaria simons es saqoneli");
            item.setTitle("Nivti: " + itemCount++);
            item.setPrice(new BigDecimal(2.5));
            items.add(item);
        }

        ApiResponse<ArrayList<StatementItem>> response = new ApiResponse<>();
        response.setCode("0");
        response.setResult(items);
        dataListener.onStatementsEvent(response, count);
    }
}
