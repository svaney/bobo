package com.bobo.gmargiani.bobo.model;

import com.bobo.gmargiani.bobo.rest.ApiResponse;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by gmargiani on 1/30/2018.
 */

public interface NetDataListener {
    void onAppVersionEvent(ApiResponse<AppVersion> response);

    void onAuthorizeByTokenEvent(ApiResponse<Boolean> response, String token);

    void onStatementsEvent(ApiResponse<ArrayList<StatementItem>> response, int from, int count, boolean sell, boolean rent,
                           String category, String location, BigDecimal priceFrom, BigDecimal priceTo, String orderBy);

    void onLocationsResponse(ApiResponse<ArrayList<KeyValue>> response);

    void onCategoriesResponse(ApiResponse<ArrayList<KeyValue>> response);

    void onOwnerInfoDetails(ApiResponse<OwnerDetails> response, long ownerId);
}
