package com.bobo.gmargiani.bobo.rest;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class ApiResponse<ContentType> {
    public final static String SUCCESSFUL_RESPONSE_CODE = "0";
    public final static String SUCCESSFUL_RESPONSE_MESSAGE = "OK";

    private ContentType data;

    private String code;

    private String message;

    public boolean isSuccess() {
        return SUCCESSFUL_RESPONSE_MESSAGE.equals(message) && SUCCESSFUL_RESPONSE_CODE.equals(code);

    }

    public ContentType getResult() {
        return data;
    }

    public void setResult(ContentType result) {
        this.data = result;
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
