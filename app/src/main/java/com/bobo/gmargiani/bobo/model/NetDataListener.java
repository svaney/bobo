package com.bobo.gmargiani.bobo.model;

import com.bobo.gmargiani.bobo.rest.ApiResponse;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by gmargiani on 1/30/2018.
 */

public interface NetDataListener {
    void onAppVersionEvent(ApiResponse<AppVersion> response);

    void onAuthorizeByTokenEvent(ApiResponse<OwnerDetails> response, String token);

    void onStatementsEvent(ApiResponse<ArrayList<StatementItem>> response, int from, int count, boolean sell, boolean rent,
                           ArrayList<String> categories, ArrayList<String> locations, BigDecimal priceFrom, BigDecimal priceTo, String orderBy);

    void onLocationsResponse(ApiResponse<ArrayList<KeyValue>> response);

    void onCategoriesResponse(ApiResponse<ArrayList<KeyValue>> response);

    void onOwnerInfoDetails(ApiResponse<ArrayList<OwnerDetails>> response, String ownerId);

    void onSimilarStatements(String statementId, ApiResponse<ArrayList<StatementItem>> response);

    void onOwnerStatements(String ownerId, ApiResponse<ArrayList<StatementItem>> response);

    void onSearchStatements(ApiResponse<ArrayList<StatementItem>> response, int from, String query, int count);

    void onSearchOwners(ApiResponse<ArrayList<OwnerDetails>> response, int from, String query, int count);

    void onFavoriteStatements(ApiResponse<ArrayList<StatementItem>> response);
}
