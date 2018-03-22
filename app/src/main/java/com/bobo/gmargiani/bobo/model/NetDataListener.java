package com.bobo.gmargiani.bobo.model;

import com.bobo.gmargiani.bobo.model.datamodels.AppVersion;
import com.bobo.gmargiani.bobo.rest.ApiResponse;

/**
 * Created by gmargiani on 1/30/2018.
 */

public interface NetDataListener {
    void onAppVersionEvent(ApiResponse<AppVersion> response);
}
