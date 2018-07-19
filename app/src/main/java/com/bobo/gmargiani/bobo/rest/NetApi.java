package com.bobo.gmargiani.bobo.rest;

import com.bobo.gmargiani.bobo.model.AppVersion;
import com.bobo.gmargiani.bobo.model.KeyValue;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.model.StatementItem;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by gmargiani on 1/30/2018.
 */

public abstract class NetApi {

    public abstract void getAppVersion(RestCallback<ApiResponse<AppVersion>> callback);

    public abstract void getLocations(RestCallback<ApiResponse<ArrayList<KeyValue>>> restCallback);

    public abstract void getCategories(RestCallback<ApiResponse<ArrayList<KeyValue>>> restCallback);

    public abstract void getStatements(int from, int count, boolean sell, boolean rent, String category, String location, BigDecimal priceFrom, BigDecimal priceTo, String orderBy, RestCallback<ApiResponse<ArrayList<StatementItem>>> restCallback);

    public abstract void getOwnerDetails(String ownerId, RestCallback<ApiResponse<OwnerDetails>> restCallback);

    public abstract void getSimilarStatements(String statementId, RestCallback<ApiResponse<ArrayList<StatementItem>>> callback);
}
