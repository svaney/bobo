package com.bobo.gmargiani.bobo.rest;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class ApiResponse<ContentType> {
    public final static String SUCCESSFUL_RESPONSE_CODE = "0";

    private ContentType result;

    private String code;

    private String message;

    public boolean isSuccess() {
        return SUCCESSFUL_RESPONSE_CODE.equals(code);
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
