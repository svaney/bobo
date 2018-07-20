package com.bobo.gmargiani.bobo.model;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppVersionEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.CategoriesEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.LocationsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.LogInEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.OwnerDetailsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.OwnerSearchEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.OwnerStatementsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.SimilarStatementsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.StatementSearchEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.StatementsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.TokenAuthorizationEvent;
import com.bobo.gmargiani.bobo.rest.ApiManager;
import com.bobo.gmargiani.bobo.rest.ApiResponse;
import com.bobo.gmargiani.bobo.rest.RestCallback;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;
import com.bobo.gmargiani.bobo.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class UserInfo implements NetDataListener {
    private static final int LAZY_LOADER_COUNT = 10;
    private Handler handler = new Handler();
    private ApiManager apiManager;
    private EventBus eventBus;

    private AppVersionEvent appVersionEvent;
    private TokenAuthorizationEvent tokenAuthorizationEvent;
    private StatementsEvent statementsEvent;
    private LocationsEvent locationsEvent;
    private CategoriesEvent categoriesEvent;
    private SimilarStatementsEvent similarStatementsEvent;
    private OwnerStatementsEvent ownerStatementsEvent;
    private ArrayList<OwnerDetailsEvent> ownerDetailsList = new ArrayList<>();
    private StatementSearchEvent statementSearchEvent;
    private OwnerSearchEvent ownerSearchEvent;
    private LogInEvent logInEvent;

    public UserInfo(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setApiManager(ApiManager apiManager) {
        this.apiManager = apiManager;
    }

    public void requestLogInEvent() {
        if (logInEvent == null){
            logInEvent = new LogInEvent();
            logInEvent.setState(RootEvent.STATE_SUCCESS);
        }
        eventBus.post(logInEvent);
    }

    public void setLogInEvent(boolean loggedIn) {
        logInEvent = new LogInEvent();
        logInEvent.setLoggedIn(loggedIn);

        if (loggedIn) {
            apiManager.getUserDetails(new RestCallback<ApiResponse<OwnerDetails>>() {
                @Override
                public void onResponse(ApiResponse<OwnerDetails> response) {
                    super.onResponse(response);
                    logInEvent = new LogInEvent();
                    if (response.isSuccess() && response.getResult() != null) {
                        logInEvent.setUserDetails(response.getResult());
                        logInEvent.setLoggedIn(true);
                    } else {
                        logInEvent.setLoggedIn(false);
                    }

                    eventBus.post(logInEvent);
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    logInEvent = new LogInEvent();
                    logInEvent.setLoggedIn(false);
                    eventBus.post(logInEvent);
                }
            });
        } else {

            eventBus.post(logInEvent);
        }
    }

    public void requestTokenAuthorizationEvent() {
        if (shouldNotRefresh(tokenAuthorizationEvent)) {
            eventBus.post(tokenAuthorizationEvent);
        } else {
            requestTokenAuthorizationEvent(PreferencesApiManager.getInstance().getToken());
        }
    }

    private void requestTokenAuthorizationEvent(final String token) {
        if (TextUtils.isEmpty(token)) {
            tokenAuthorizationEvent = new TokenAuthorizationEvent();
            tokenAuthorizationEvent.setState(RootEvent.STATE_SUCCESS);
            eventBus.post(tokenAuthorizationEvent);
        } else {
            if (shouldNotRefresh(tokenAuthorizationEvent)) {
                eventBus.post(tokenAuthorizationEvent);
            } else {
                tokenAuthorizationEvent = new TokenAuthorizationEvent();
                tokenAuthorizationEvent.setState(RootEvent.STATE_LOADING);
                eventBus.post(tokenAuthorizationEvent);
                apiManager.authorizeByToken(token);
            }
        }
    }

    @Override
    public void onAuthorizeByTokenEvent(ApiResponse<Token> response) {
        tokenAuthorizationEvent = new TokenAuthorizationEvent();
        if (response.isSuccess() && response.getResult() != null && !TextUtils.isEmpty(response.getResult().getToken())) {
            tokenAuthorizationEvent.setState(RootEvent.STATE_SUCCESS);
            PreferencesApiManager.getInstance().saveToken(response.getResult().getToken());
            setLogInEvent(true);
        } else {
            tokenAuthorizationEvent.setState(RootEvent.STATE_ERROR);
        }
        eventBus.post(tokenAuthorizationEvent);

    }

    public void requestSimilarStatements(final String statementId) {
        if (shouldNotRefresh(similarStatementsEvent) && Utils.equals(similarStatementsEvent.getStatementId(), statementId)) {
            eventBus.post(similarStatementsEvent);
        } else {
            similarStatementsEvent = new SimilarStatementsEvent();
            similarStatementsEvent.setStatementId(statementId);
            similarStatementsEvent.setState(RootEvent.STATE_LOADING);
            eventBus.post(similarStatementsEvent);

            apiManager.getSimilarStatements(statementId);

        }
    }

    @Override
    public void onSimilarStatements(String statementId, ApiResponse<ArrayList<StatementItem>> response) {
        if (similarStatementsEvent != null && Utils.equals(similarStatementsEvent.getStatementId(), statementId)) {
            similarStatementsEvent = (SimilarStatementsEvent) similarStatementsEvent.copyData();
            if (response.isSuccess()) {
                similarStatementsEvent.setState(RootEvent.STATE_SUCCESS);
                similarStatementsEvent.setSimilarStatements(response.getResult());
            } else {
                similarStatementsEvent.setState(RootEvent.STATE_ERROR);
            }
            eventBus.post(similarStatementsEvent);
        }
    }

    public void requestAppVersion(boolean update) {

        if (shouldNotRefresh(appVersionEvent, update)) {
            eventBus.post(appVersionEvent);
        } else {

            appVersionEvent = appVersionEvent == null ? new AppVersionEvent() : (AppVersionEvent) appVersionEvent.copyData();

            appVersionEvent.setState(RootEvent.STATE_LOADING);

            eventBus.post(appVersionEvent);

            apiManager.getAppVersion();

        }
    }

    @Override
    public void onAppVersionEvent(ApiResponse<AppVersion> response) {

        if (appVersionEvent != null) {
            appVersionEvent = (AppVersionEvent) appVersionEvent.copyData();
            if (response.isSuccess() && response.getResult() != null) {
            /*    response.getResult().setShowDialog(true);
                response.getResult().setTitle("გთხოვთ განაახლოთ აპლიკაცია");
                response.getResult().setDialogText("აპლიკაციის გამართულად მუშაობისთვის, საჭიროა მისი განახლება");
                response.getResult().setOkButtonLink("https://www.google.com/");
                */
                appVersionEvent.setState(RootEvent.STATE_SUCCESS);
                appVersionEvent.setAppVersion(response.getResult());
            } else {
                appVersionEvent.setState(RootEvent.STATE_ERROR);
            }
            eventBus.post(appVersionEvent);
        }
    }

    public void searchOwners(final String query, final int from) {
        if (TextUtils.isEmpty(query)) {
            return;
        }

        if (shouldNotRefresh(ownerSearchEvent) && ownerSearchEvent.getFrom() >= from && query.equals(ownerSearchEvent.getSearchQuery())) {
            eventBus.post(ownerSearchEvent);
        } else {
            ownerSearchEvent = ownerSearchEvent == null || !query.equals(ownerSearchEvent.getSearchQuery())
                    ? new OwnerSearchEvent(query) : (OwnerSearchEvent) ownerSearchEvent.copyData();

            ownerSearchEvent.setState(RootEvent.STATE_LOADING);
            ownerSearchEvent.setFrom(from);

            eventBus.post(ownerSearchEvent);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    apiManager.searchOwners(from, LAZY_LOADER_COUNT, query);
                }
            }, 2000);
        }
    }

    @Override
    public void onSearchOwners(ApiResponse<ArrayList<OwnerDetails>> response, int from, String query, int count) {
        if (ownerSearchEvent != null && ownerSearchEvent.getFrom() == from && query.equals(ownerSearchEvent.getSearchQuery())) {

            ownerSearchEvent = (OwnerSearchEvent) ownerSearchEvent.copyData();
            if (response.isSuccess()) {
                ownerSearchEvent.setState(RootEvent.STATE_SUCCESS);
                ownerSearchEvent.addOwners(response.getResult());
                if (response.getResult() != null && response.getResult().size() < count) {
                    ownerSearchEvent.setCanLoadMore(false);
                }
            } else {
                ownerSearchEvent.setState(RootEvent.STATE_ERROR);
            }

            eventBus.post(ownerSearchEvent);
        }
    }

    public void searchStatements(final String query, final int from) {
        if (TextUtils.isEmpty(query)) {
            return;
        }

        if (shouldNotRefresh(statementSearchEvent) && statementSearchEvent.getFrom() >= from && query.equals(statementSearchEvent.getSearchQuery())) {
            eventBus.post(statementSearchEvent);
        } else {
            statementSearchEvent = statementSearchEvent == null || !query.equals(statementSearchEvent.getSearchQuery())
                    ? new StatementSearchEvent(query) : (StatementSearchEvent) statementSearchEvent.copyData();

            statementSearchEvent.setState(RootEvent.STATE_LOADING);
            statementSearchEvent.setFrom(from);

            eventBus.post(statementSearchEvent);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    apiManager.searchStatements(from, LAZY_LOADER_COUNT, query);
                }
            }, 2000);
        }
    }

    @Override
    public void onSearchStatements(ApiResponse<ArrayList<StatementItem>> response, int from, String query, int count) {
        if (statementSearchEvent != null && statementSearchEvent.getFrom() == from && query.equals(statementSearchEvent.getSearchQuery())) {

            statementSearchEvent = (StatementSearchEvent) statementSearchEvent.copyData();
            if (response.isSuccess()) {
                statementSearchEvent.setState(RootEvent.STATE_SUCCESS);
                statementSearchEvent.addStatements(response.getResult());
                if (response.getResult() != null && response.getResult().size() < count) {
                    statementSearchEvent.setCanLoadMore(false);
                }
            } else {
                statementSearchEvent.setState(RootEvent.STATE_ERROR);
            }

            eventBus.post(statementSearchEvent);
        }
    }

    public void requestStatements(final int from, boolean update,
                                  final boolean sell, final boolean rent, final String category, final String location,
                                  final BigDecimal priceFrom, final BigDecimal priceTo, final String orderBy) {

        if (shouldNotRefresh(statementsEvent, update)
                && statementsEvent.getFrom() >= from
                && statementsEvent.hasSameParameters(sell, rent, category, location, priceFrom, priceTo, orderBy)) {

            eventBus.post(statementsEvent);
        } else {

            statementsEvent = statementsEvent == null || !statementsEvent.hasSameParameters(sell, rent, category, location, priceFrom, priceTo, orderBy)
                    ? new StatementsEvent(sell, rent, category, location, priceFrom, priceTo, orderBy) : (StatementsEvent) statementsEvent.copyData();

            statementsEvent.setState(RootEvent.STATE_LOADING);
            statementsEvent.setFrom(from);

            eventBus.post(statementsEvent);

            apiManager.getStatements(from, LAZY_LOADER_COUNT, sell, rent, category, location, priceFrom, priceTo, orderBy);
        }
    }

    @Override
    public void onStatementsEvent(ApiResponse<ArrayList<StatementItem>> response,
                                  int from, int count, boolean sell, boolean rent, String category,
                                  String location, BigDecimal priceFrom, BigDecimal priceTo, String orderBy) {

        if (statementsEvent != null && statementsEvent.getFrom() == from
                && statementsEvent.hasSameParameters(sell, rent, category, location, priceFrom, priceTo, orderBy)) {

            statementsEvent = (StatementsEvent) statementsEvent.copyData();
            if (response.isSuccess()) {
                statementsEvent.setState(RootEvent.STATE_SUCCESS);
                statementsEvent.addStatements(response.getResult());
                if (response.getResult() != null && response.getResult().size() < count) {
                    statementsEvent.setCanLoadMore(false);
                }
            } else {
                statementsEvent.setState(RootEvent.STATE_ERROR);
            }

            eventBus.post(statementsEvent);
        }
    }

    public StatementItem getStatementItemById(String id) {
        if (statementsEvent != null && statementsEvent.getStatements() != null) {
            for (StatementItem it : statementsEvent.getStatements()) {
                if (Utils.equals(it.getStatementId(), id)) {
                    return it;
                }
            }
        }

        return null;
    }

    public void requestOwnerDetails(final String ownerId) {
        if (ownerDetailsList == null) {
            ownerDetailsList = new ArrayList<>();
        }

        if (shouldNotRefresh(getOwnerDetailsEvent(ownerId))) {
            eventBus.post(getOwnerDetailsEvent(ownerId));
        } else {
            OwnerDetailsEvent ownerDetailsEvent = getOwnerDetailsEvent(ownerId);

            if (ownerDetailsEvent == null) {
                ownerDetailsEvent = new OwnerDetailsEvent();
                ownerDetailsList.add(ownerDetailsEvent);
            } else {
                ownerDetailsEvent.copyData();
            }

            ownerDetailsEvent.setOwnerId(ownerId);
            ownerDetailsEvent.setState(RootEvent.STATE_LOADING);

            eventBus.post(ownerDetailsEvent);
            apiManager.getOwnerDetails(ownerId);
        }
    }

    @Override
    public void onOwnerInfoDetails(ApiResponse<OwnerDetails> response, String ownerId) {
        if (ownerDetailsList == null) {
            ownerDetailsList = new ArrayList<>();
        }

        OwnerDetailsEvent event = getOwnerDetailsEvent(ownerId);

        if (event != null) {
            ownerDetailsList.remove(event);
            event = (OwnerDetailsEvent) event.copyData();
        } else {
            event = new OwnerDetailsEvent();
            event.setOwnerId(ownerId);
        }

        ownerDetailsList.add(event);

        if (response.isSuccess()) {
            event.setState(RootEvent.STATE_SUCCESS);
            event.setDetails(response.getResult());

        } else {
            event.setState(RootEvent.STATE_ERROR);
        }

        eventBus.post(event);
    }

    public OwnerDetailsEvent getOwnerDetailsEvent(String ownerId) {
        if (ownerDetailsList != null) {
            for (OwnerDetailsEvent event : ownerDetailsList) {
                if (Utils.equals(event.getOwnerId(), ownerId)) {
                    return event;
                }
            }
        }

        return null;
    }

    public void requestOwnerStatements(final String ownerId) {

        if (shouldNotRefresh(ownerStatementsEvent) && Utils.equals(ownerStatementsEvent.getOwnerId(), ownerId)) {
            eventBus.post(ownerStatementsEvent);
        } else {
            ownerStatementsEvent = new OwnerStatementsEvent();
            ownerStatementsEvent.setOwnerId(ownerId);
            ownerStatementsEvent.setState(RootEvent.STATE_LOADING);

            eventBus.post(ownerStatementsEvent);

            apiManager.getStatementsByOwner(ownerId);

        }

    }

    @Override
    public void onOwnerStatements(String ownerId, ApiResponse<ArrayList<StatementItem>> response) {
        if (ownerStatementsEvent != null && Utils.equals(ownerStatementsEvent.getOwnerId(), ownerId)) {
            ownerStatementsEvent = new OwnerStatementsEvent();
            ownerStatementsEvent.setOwnerId(ownerId);
            if (response.isSuccess()) {
                ownerStatementsEvent.setState(RootEvent.STATE_SUCCESS);
                ownerStatementsEvent.setOwnerStatements(response.getResult());
            } else {
                ownerStatementsEvent.setState(RootEvent.STATE_ERROR);
            }

            eventBus.post(ownerStatementsEvent);
        }
    }


    public ArrayList<StatementItem> getOwnerStatements(String ownerId) {
        if (ownerStatementsEvent != null && ownerStatementsEvent.getState() == RootEvent.STATE_SUCCESS && Utils.equals(ownerStatementsEvent.getOwnerId(), ownerId)) {
            return ownerStatementsEvent.getOwnerStatements();
        }

        return null;
    }

    public void requestLocations() {
        if (shouldNotRefresh(locationsEvent)) {
            eventBus.post(locationsEvent);
        } else {
            locationsEvent = new LocationsEvent();
            locationsEvent.setState(RootEvent.STATE_LOADING);
            eventBus.post(locationsEvent);
            apiManager.getLocations();

        }
    }

    @Override
    public void onLocationsResponse(ApiResponse<ArrayList<KeyValue>> response) {
        locationsEvent = new LocationsEvent();
        if (response.isSuccess()) {
            locationsEvent.setState(RootEvent.STATE_SUCCESS);
            locationsEvent.setLocations(response.getResult());
        } else {
            locationsEvent.setState(RootEvent.STATE_ERROR);
        }
        eventBus.post(locationsEvent);
    }

    public void requestCategories() {
        if (shouldNotRefresh(categoriesEvent)) {
            eventBus.post(categoriesEvent);
        } else {
            categoriesEvent = new CategoriesEvent();
            categoriesEvent.setState(RootEvent.STATE_LOADING);
            eventBus.post(categoriesEvent);
            apiManager.getCategories();
        }
    }

    @Override
    public void onCategoriesResponse(ApiResponse<ArrayList<KeyValue>> response) {
        categoriesEvent = new CategoriesEvent();
        if (response.isSuccess()) {
            categoriesEvent.setState(RootEvent.STATE_SUCCESS);
            categoriesEvent.setCategories(response.getResult());
        } else {
            categoriesEvent.setState(RootEvent.STATE_ERROR);
        }
        eventBus.post(categoriesEvent);
    }


    public CategoriesEvent getCategoriesEvent() {
        return categoriesEvent;
    }

    public LocationsEvent getLocationsEvent() {
        return locationsEvent;
    }


    public boolean shouldNotRefresh(RootEvent event) {
        return shouldNotRefresh(event, false);
    }

    public boolean isAuthorized() {
        return logInEvent == null ? false : logInEvent.isLoggedIn();
    }

    public boolean shouldNotRefresh(RootEvent event, boolean update) {
        if (event == null) {
            return false;
        }

        if (event.isError()) {
            return false;
        }

        if (event.isLoading()) {
            return true;
        }

        if (event.isSuccess() && !update) {
            return true;
        }

        return false;
    }


}
