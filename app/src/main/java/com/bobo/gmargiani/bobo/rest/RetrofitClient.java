package com.bobo.gmargiani.bobo.rest;

import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.bobo.gmargiani.bobo.utils.LocaleHelper;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;
import com.bobo.gmargiani.bobo.utils.Utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class RetrofitClient {
    public static final String BASE_URL_IP = "http://10.45.48.95";
    public static final String BASE_URL = "login.bog.ge";
    public static final String BASE_SEGMENT = "rb-middleware-api-connector";

    public static final String PARAMETER_SESSION = "sessionId";

    public static final long READ_TIMEOUT = 40;
    public static final int MAX_REQUEST = 40;

    private static RetrofitClient instance;

    private RetrofitService retrofitService;

    private String sessionId;
    private int appVersion;

    public RetrofitClient(){
        instance = this;

        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL_IP)
                .client(createClient(READ_TIMEOUT, true))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitService = restAdapter.create(RetrofitService.class);
    }

    private OkHttpClient createClient(long readTimeOutSeconds, boolean automaticRetry) {

        Dispatcher dispatcher = new Dispatcher();

        dispatcher.setMaxRequestsPerHost(MAX_REQUEST);
        dispatcher.setMaxRequests(MAX_REQUEST);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.retryOnConnectionFailure(automaticRetry);
        httpClient.readTimeout(readTimeOutSeconds, TimeUnit.SECONDS);
        httpClient.addInterceptor(createInterceptor());

        httpClient.addInterceptor(createLogging());

        return httpClient.dispatcher(dispatcher).build();
    }

    private HttpLoggingInterceptor createLogging() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return logging;
    }

    private Interceptor createInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl.Builder builder = originalHttpUrl.newBuilder()
                        .host(BASE_URL)
                        .scheme("https")
                        .addPathSegment(BASE_SEGMENT)
                        .addQueryParameter(PARAMETER_SESSION, sessionId)
                        .addQueryParameter("os", "ANDROID")
                        .addQueryParameter("osVersion", String.valueOf(AppUtils.getAndroidVersion()))
                        .addQueryParameter("appVersion", String.valueOf(appVersion))
                        .addQueryParameter("langCode", LocaleHelper.getLanguage(App.getInstance()));

                HttpUrl url = builder.build();


                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();

                return chain.proceed(request);
            }
        };
    }

    public void setSessionId(String sessionId){
        this.sessionId = sessionId;
    }

    public String getSessionId(){
        return sessionId;
    }

    public void setAppVersion(int appVersion){
        this.appVersion = appVersion;
    }

    public RetrofitService getService() {
        return retrofitService;
    }

    public static String getSessionIdToCheck() {
        if (instance != null) {
            return instance.getSessionId();
        }
        return null;
    }
}
