package com.bobo.gmargiani.bobo.rest;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.model.AppVersion;
import com.bobo.gmargiani.bobo.model.KeyValue;
import com.bobo.gmargiani.bobo.model.LogInData;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.model.Token;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;
import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class RetrofitApi extends NetApi {
    private static RetrofitService retService;
    private RetrofitClient retrofitClient;

    public RetrofitApi(RetrofitClient retrofitClient) {
        if (retService == null) {
            this.retrofitClient = retrofitClient;
            retService = retrofitClient.getService();
        }
    }

    @Override
    public void getAppVersion(RestCallback<ApiResponse<AppVersion>> callback) {
        Call<ApiResponse<AppVersion>> call = retService.getAppVersion();
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getLocations(RestCallback<ApiResponse<ArrayList<KeyValue>>> callback) {
        Call<ApiResponse<ArrayList<KeyValue>>> call = retService.getLocations();
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getCategories(RestCallback<ApiResponse<ArrayList<KeyValue>>> callback) {
        Call<ApiResponse<ArrayList<KeyValue>>> call = retService.getCategories();
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getStatements(int from, int count, boolean selling, boolean renting, String searchQuery, ArrayList<String> categoryIds, ArrayList<String> locationIds, BigDecimal priceFrom,
                              BigDecimal priceTo, String orderBy, RestCallback<ApiResponse<ArrayList<StatementItem>>> callback) {

        StatementJson js = new StatementJson(from, count, selling, renting, categoryIds, locationIds, priceFrom, priceTo, orderBy, searchQuery);
        Call<ApiResponse<ArrayList<StatementItem>>> call = retService.getStatements(js);
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getOwnerDetails(String ownerId, RestCallback<ApiResponse<ArrayList<OwnerDetails>>> callback) {
        ArrayList<String> userIds = new ArrayList<>();
        userIds.add(ownerId);
        Call<ApiResponse<ArrayList<OwnerDetails>>> call = retService.getOwnerDetails(new UserId(userIds));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getSubscribedUserDetails(ArrayList<String> userIds, RestCallback<ApiResponse<ArrayList<OwnerDetails>>> callback) {
        Call<ApiResponse<ArrayList<OwnerDetails>>> call = retService.getOwnerDetails(new UserId(userIds));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getSimilarStatements(String categoryId, RestCallback<ApiResponse<ArrayList<StatementItem>>> callback) {
        Call<ApiResponse<ArrayList<StatementItem>>> call = retService.getSimilarStatements(new CategoryId(categoryId));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getUserStatements(String ownerId, int from, int count, RestCallback<ApiResponse<ArrayList<StatementItem>>> callback) {
        Call<ApiResponse<ArrayList<StatementItem>>> call = retService.getUserStatements(new OwnerStatements(from, count, ownerId));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void requestFavoriteStatements(ArrayList<String> favourites, RestCallback<ApiResponse<ArrayList<StatementItem>>> callback) {
        Call<ApiResponse<ArrayList<StatementItem>>> call = retService.getStatementsByIds(new FavoriteStatements(favourites));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void createStatement(String title, String description, String price, String locationId, String categoryId, double lat, double lng, boolean selling,
                                boolean renting, ArrayList<File> userImageFiles, RestCallback<ApiResponse<Object>> callback) {
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), price);
        RequestBody locationBody = null;
        if (!TextUtils.isEmpty(locationId))
            locationBody = RequestBody.create(MediaType.parse("text/plain"), locationId);
        RequestBody categoryBody = RequestBody.create(MediaType.parse("text/plain"), categoryId);
        RequestBody latBody = null;
        if (lat != -1000)
            latBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(lat));
        RequestBody lngBody = null;
        if (lng != -1000)
            lngBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(lng));
        RequestBody sellingBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selling));
        RequestBody rentingBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(renting));

        MultipartBody.Part photo1 = null;
        MultipartBody.Part photo2 = null;
        MultipartBody.Part photo3 = null;
        MultipartBody.Part photo4 = null;
        MultipartBody.Part photo5 = null;

        if (userImageFiles != null && userImageFiles.size() > 0) {
            RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), userImageFiles.get(0));
            photo1 = MultipartBody.Part.createFormData("photos", userImageFiles.get(0).getName(), requestFile1);
            if (userImageFiles.size() > 1) {
                RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), userImageFiles.get(1));
                photo2 = MultipartBody.Part.createFormData("photos", userImageFiles.get(1).getName(), requestFile2);
                if (userImageFiles.size() > 2) {
                    RequestBody requestFile3 = RequestBody.create(MediaType.parse("multipart/form-data"), userImageFiles.get(2));
                    photo3 = MultipartBody.Part.createFormData("photos", userImageFiles.get(2).getName(), requestFile3);
                    if (userImageFiles.size() > 3) {
                        RequestBody requestFile4 = RequestBody.create(MediaType.parse("multipart/form-data"), userImageFiles.get(3));
                        photo4 = MultipartBody.Part.createFormData("photos", userImageFiles.get(3).getName(), requestFile4);
                        if (userImageFiles.size() > 4) {
                            RequestBody requestFile5 = RequestBody.create(MediaType.parse("multipart/form-data"), userImageFiles.get(4));
                            photo5 = MultipartBody.Part.createFormData("photos", userImageFiles.get(4).getName(), requestFile5);
                        }
                    }
                }
            }
        }

        Call<ApiResponse<Object>> call = retService.createStatement("Bearer " + PreferencesApiManager.getInstance().getToken(), titleBody, descriptionBody, priceBody, locationBody,
                categoryBody, latBody, lngBody, sellingBody, rentingBody, photo1, photo2, photo3, photo4, photo5);

        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));

    }

    @Override
    public void registerUser(boolean isCompany, String firstName, String lastName, String companyName, String password, String email, String phoneNum, File imageFile, RestCallback<ApiResponse<Object>> callback) {

        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody companyNameBody = RequestBody.create(MediaType.parse("text/plain"), companyName);
        RequestBody isCompanyBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(isCompany));
        RequestBody firstNameBody = RequestBody.create(MediaType.parse("text/plain"), firstName);
        RequestBody lastNameBody = RequestBody.create(MediaType.parse("text/plain"), lastName);
        RequestBody phoneNumBody = RequestBody.create(MediaType.parse("text/plain"), phoneNum);

        MultipartBody.Part avatarBody = null;
        try {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            avatarBody = MultipartBody.Part.createFormData("avatar", imageFile.getName(), requestFile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<ApiResponse<Object>> call = retService.registerUser(avatarBody, emailBody, passwordBody, isCompanyBody, firstNameBody,
                lastNameBody, phoneNumBody, companyNameBody);

        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));

    }

    @Override
    public void logIn(String email, String password, RestCallback<ApiResponse<LogInData>> callback) {
        Call<ApiResponse<LogInData>> call = retService.logIn(new LogIn(email, password));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void logInByToken(String token, RestCallback<ApiResponse<OwnerDetails>> callback) {
        Call<ApiResponse<OwnerDetails>> call = retService.logInByToken("Bearer " + token);
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void getUsers(int from, int to, String searchQuery, RestCallback<ApiResponse<ArrayList<OwnerDetails>>> callback) {
        Call<ApiResponse<ArrayList<OwnerDetails>>> call = retService.getUsers(new SearchUser(from, to, searchQuery));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void setFavorite(String statementId, boolean isFavorite, RestCallback<ApiResponse<Object>> callback) {
        Call<ApiResponse<Object>> call = retService.setFavorite("Bearer " + PreferencesApiManager.getInstance().getToken(), new FavoriteStatement(statementId, isFavorite));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void subscribeUser(String userId, boolean isSubscribed, RestCallback<ApiResponse<Object>> callback) {
        Call<ApiResponse<Object>> call = retService.setSubscribed("Bearer " + PreferencesApiManager.getInstance().getToken(), new SubscribeUser(userId, isSubscribed));
        callback.setCall(call);
        call.enqueue(new RetrofitCallback<>(callback));
    }

    public class OwnerStatements {
        int from;
        int count;
        String ownerId;

        public OwnerStatements(int from, int to, String ownerId) {
            this.from = from;
            this.count = to;
            this.ownerId = ownerId;
        }
    }

    public class SearchUser {
        int from;
        int count;
        String searchQuery;

        public SearchUser(int from, int to, String searchQuery) {
            this.from = from;
            this.count = to;
            this.searchQuery = searchQuery;
        }
    }

    public class FavoriteStatement {
        String statementId;
        boolean isFavourite;

        public FavoriteStatement(String statementId, boolean isFavourite) {
            this.statementId = statementId;
            this.isFavourite = isFavourite;
        }
    }

    public class SubscribeUser {
        String userId;
        boolean isSubscribed;

        public SubscribeUser(String userId, boolean isSubscribed) {
            this.userId = userId;
            this.isSubscribed = isSubscribed;
        }
    }

    public class CategoryId {
        String categoryId;

        public CategoryId(String categoryId) {
            this.categoryId = categoryId;
        }
    }

    public class RegisterUser {
        @SerializedName("email")
        String email;
        @SerializedName("password")
        String password;
        @SerializedName("isCompany")
        boolean isCompany;
        @SerializedName("firstName")
        String firstName;
        @SerializedName("lastName")
        String lastName;
        @SerializedName("phoneNum")
        String phoneNum;
        @SerializedName("companyName")
        String companyName;
        @SerializedName("avatar")
        MultipartBody.Part avatar;

        public RegisterUser(String email, String password, boolean isCompany, String firstName, String lastName, String phoneNum, String companyName, MultipartBody.Part avatar) {
            this.email = email;
            this.password = password;
            this.isCompany = isCompany;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phoneNum = phoneNum;
            this.companyName = companyName;
            this.avatar = avatar;
        }
    }

    public class LogIn {
        String email;
        String password;

        public LogIn(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }


    public class FavoriteStatements {
        ArrayList<String> statementsIds;

        public FavoriteStatements(ArrayList<String> statementsIds) {
            this.statementsIds = statementsIds;
        }
    }


    public class UserId {
        ArrayList<String> userIds;

        public UserId(ArrayList<String> userIds) {
            this.userIds = userIds;
        }
    }

    public class StatementJson {
        int from;
        int count;
        boolean selling;
        boolean renting;
        ArrayList<String> categoryIds;
        ArrayList<String> locationIds;
        BigDecimal priceFrom;
        BigDecimal priceTo;
        //   String orderBy;
        String searchQuery;

        public StatementJson(int from, int count, boolean selling, boolean renting, ArrayList<String> categoryIds, ArrayList<String> locationIds,
                             BigDecimal priceFrom, BigDecimal priceTo, String orderBy, String searchQuery) {
            this.from = from;
            this.count = count;
            this.selling = selling;
            this.renting = renting;
            this.categoryIds = categoryIds;
            this.locationIds = locationIds;
            this.priceFrom = priceFrom;
            this.priceTo = priceTo;
            //   this.orderBy = orderBy;
            this.searchQuery = searchQuery;
        }
    }
}
