package com.bobo.gmargiani.bobo.model.datamodels;

import com.bobo.gmargiani.bobo.rest.ApiResponse;

/**
 * Created by gmargiani on 1/30/2018.
 */

public interface NetDataListener {
    void onTestData(ApiResponse<AppVersion> response);
}
