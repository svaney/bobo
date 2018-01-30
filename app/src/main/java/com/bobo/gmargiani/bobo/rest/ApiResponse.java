package com.bobo.gmargiani.bobo.rest;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class ApiResponse<ContentType> {
    public final static String SUCCESSFUL_RESPONSE_CODE = "0";
    public static final String INVALID_SESSION_CODE = "13";

    private ContentType result;

    private String code;
    private String error;
    private boolean isNetworkFailure;
    private Throwable networkFailure;

    public boolean isSuccess() {
        return SUCCESSFUL_RESPONSE_CODE.equals(code);
    }

    public boolean isInvalidSession() {
        return  INVALID_SESSION_CODE.equals(code);
    }

    public ContentType getResult() {
        return result;
    }

    public void setResult(ContentType result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isNetworkFailure() {
        return isNetworkFailure;
    }

    public void setNetworkFailure(boolean networkFailure) {
        isNetworkFailure = networkFailure;
    }

    public Throwable getNetworkFailure() {
        return networkFailure;
    }

    public void setNetworkFailure(Throwable networkFailure) {
        this.networkFailure = networkFailure;
    }
}
