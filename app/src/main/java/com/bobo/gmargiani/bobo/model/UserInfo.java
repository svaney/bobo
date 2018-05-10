package com.bobo.gmargiani.bobo.model;

import android.os.Handler;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppVersionEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.CategoriesEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.LocationsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.OwnerDetailsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.OwnerStatementsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.SimilarStatementsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.StatementsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.TokenAuthorizationEvent;
import com.bobo.gmargiani.bobo.rest.ApiManager;
import com.bobo.gmargiani.bobo.rest.ApiResponse;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;
import com.bobo.gmargiani.bobo.utils.Utils;

import org.greenrobot.eventbus.EventBus;

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

    public UserInfo(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setApiManager(ApiManager apiManager) {
        this.apiManager = apiManager;
    }

    public void requestTokenAuthorizationEvent() {
        if (shouldNotRefresh(tokenAuthorizationEvent)) {
            eventBus.post(tokenAuthorizationEvent);
        } else {
            requestTokenAuthorizationEvent(PreferencesApiManager.getInstance().getToken());
        }
    }

    private void requestTokenAuthorizationEvent(final String token) {
        if (shouldNotRefresh(tokenAuthorizationEvent) && Utils.equals(tokenAuthorizationEvent.getToken(), token)) {
            eventBus.post(tokenAuthorizationEvent);
        } else {
            tokenAuthorizationEvent = new TokenAuthorizationEvent();
            tokenAuthorizationEvent.setToken(token);
            tokenAuthorizationEvent.setState(RootEvent.STATE_LOADING);

            eventBus.post(tokenAuthorizationEvent);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    apiManager.authorizeByToken(token);
                }
            }, 2000);

        }
    }

    @Override
    public void onAuthorizeByTokenEvent(ApiResponse<Boolean> response, String token) {
        if (tokenAuthorizationEvent != null && Utils.equals(tokenAuthorizationEvent.getToken(), token)) {
            tokenAuthorizationEvent = (TokenAuthorizationEvent) tokenAuthorizationEvent.copyData();

            if (response.isSuccess()) {
                tokenAuthorizationEvent.setState(RootEvent.STATE_SUCCESS);
                tokenAuthorizationEvent.setAuthorized(response.getResult());
            } else {
                tokenAuthorizationEvent.setState(RootEvent.STATE_ERROR);
            }
            eventBus.post(tokenAuthorizationEvent);
        }
    }

    public void requestSimilarStatements(final long statementId) {
        if (shouldNotRefresh(similarStatementsEvent) && similarStatementsEvent.getStatementId() == statementId) {
            eventBus.post(similarStatementsEvent);
        } else {
            similarStatementsEvent = new SimilarStatementsEvent();
            similarStatementsEvent.setStatementId(statementId);
            similarStatementsEvent.setState(RootEvent.STATE_LOADING);
            eventBus.post(similarStatementsEvent);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    apiManager.getSimilarStatements(statementId);
                }
            }, 1000);

        }
    }

    @Override
    public void onSimilarStatements(long statementId, ApiResponse<ArrayList<StatementItem>> response) {
        if (similarStatementsEvent != null && similarStatementsEvent.getStatementId() == statementId) {
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

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    apiManager.getAppVersion("ANDROID", "MOBILE-EXT");
                }
            }, 1000);

        }
    }

    @Override
    public void onAppVersionEvent(ApiResponse<AppVersion> response) {

        if (appVersionEvent != null) {
            appVersionEvent = (AppVersionEvent) appVersionEvent.copyData();
            if (response.isSuccess()) {
                appVersionEvent.setState(RootEvent.STATE_SUCCESS);
                appVersionEvent.setAppVersion(response.getResult());
            } else {
                appVersionEvent.setState(RootEvent.STATE_ERROR);
            }
            eventBus.post(appVersionEvent);
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

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    apiManager.getStatements(from, LAZY_LOADER_COUNT, sell, rent, category, location, priceFrom, priceTo, orderBy);
                }
            }, 1000);

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


    public StatementItem getStatementItemById(long id) {
        if (statementsEvent != null && statementsEvent.getStatements() != null) {
            for (StatementItem it : statementsEvent.getStatements()) {
                if (it.getStatementId() == id) {
                    return it;
                }
            }
        }

        return null;
    }

    public void requestOwnerDetails(final long ownerId) {
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
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    apiManager.getOwnerDetails(ownerId);
                }
            }, 1000);
        }
    }

    @Override
    public void onOwnerInfoDetails(ApiResponse<OwnerDetails> response, long ownerId) {
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

    public OwnerDetailsEvent getOwnerDetailsEvent(long ownerId) {
        if (ownerDetailsList != null) {
            for (OwnerDetailsEvent event : ownerDetailsList) {
                if (event.getOwnerId() == ownerId) {
                    return event;
                }
            }
        }

        return null;
    }

    public void requestOwnerStatements(final long ownerId) {

        if (shouldNotRefresh(ownerStatementsEvent) && ownerStatementsEvent.getOwnerId() == ownerId) {
            eventBus.post(ownerStatementsEvent);
        } else {
            ownerStatementsEvent = new OwnerStatementsEvent();
            ownerStatementsEvent.setOwnerId(ownerId);
            ownerStatementsEvent.setState(RootEvent.STATE_LOADING);

            eventBus.post(ownerStatementsEvent);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    apiManager.getStatementsByOwner(ownerId);
                }
            }, 500);

        }

    }

    @Override
    public void onOwnerStatements(long ownerId, ApiResponse<ArrayList<StatementItem>> response) {
        if (ownerStatementsEvent != null && ownerStatementsEvent.getOwnerId() == ownerId) {
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


    public ArrayList<StatementItem> getOwnerStatements(long ownerId) {
        if (ownerStatementsEvent != null && ownerStatementsEvent.getState() == RootEvent.STATE_SUCCESS && ownerStatementsEvent.getOwnerId() == ownerId){
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
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    apiManager.getLocations();
                }
            }, 1000);

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
        return tokenAuthorizationEvent == null ? false : tokenAuthorizationEvent.isAuthorized();
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
