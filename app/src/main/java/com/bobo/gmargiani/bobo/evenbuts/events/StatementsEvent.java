package com.bobo.gmargiani.bobo.evenbuts.events;

import android.text.TextUtils;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

public class StatementsEvent extends RootEvent {
    private boolean canLoadMore = true;
    private int from;

    private boolean selling;
    private boolean rent;
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private String orderBy = "";

    private ArrayList<StatementItem> statements = new ArrayList<>();

    public StatementsEvent() {

    }

    public StatementsEvent(boolean sell, boolean rent, ArrayList<String> categories, ArrayList<String> locations,
                           BigDecimal priceFrom, BigDecimal priceTo, String orderBy) {
        setSelling(sell);
        setRent(rent);
        setCategories(categories);
        setLocations(locations);
        setPriceFrom(priceFrom);
        setPriceTo(priceTo);
        setOrderBy(orderBy);
    }

    public boolean hasSameParameters(boolean sell, boolean rent, ArrayList<String> categories, ArrayList<String> locations,
                                     BigDecimal priceFrom, BigDecimal priceTo, String orderBy) {

        if (isRent() != rent || isSelling() != sell
                || !Utils.equals(getPriceFrom(), priceFrom)
                || !Utils.equals(getPriceTo(), priceTo)
                || !Utils.equals(getOrderBy(), orderBy)) {
            return false;
        }


        HashSet<String> localCategories = new HashSet<>();
        HashSet<String> passedCategories = new HashSet<>();

        if (getCategories() != null) {
            for (String cat : getCategories()) {
                if (!TextUtils.isEmpty(cat))
                    localCategories.add(cat);
            }
        }

        if (categories != null) {
            for (String cat : categories) {
                if (!TextUtils.isEmpty(cat))
                    passedCategories.add(cat);
            }
        }

        HashSet<String> localLocations = new HashSet<>();
        HashSet<String> passedLocations = new HashSet<>();

        if (getLocations() != null) {
            for (String loc : getLocations()) {
                if (!TextUtils.isEmpty(loc))
                    localLocations.add(loc);
            }
        }

        if (locations != null) {
            for (String loc : locations) {
                if (!TextUtils.isEmpty(loc))
                    passedLocations.add(loc);
            }
        }


        if (localLocations.size() != passedLocations.size()
                || localCategories.size() != passedCategories.size()) {
            return false;
        }

        for (String loc : localLocations) {
            if (!passedLocations.contains(loc)) {
                return false;
            }
        }

        for (String cat : localCategories) {
            if (!passedCategories.contains(cat)) {
                return false;
            }
        }


        return true;
    }

    @Override
    public Object copyData() {
        StatementsEvent ev = new StatementsEvent();
        ev.setStatements(getStatements());
        ev.setCanLoadMore(canLoadMore());
        ev.setFrom(getFrom());
        ev.setSelling(isSelling());
        ev.setRent(isRent());
        ev.setCategories(getCategories());
        ev.setLocations(getLocations());
        ev.setPriceFrom(getPriceFrom());
        ev.setPriceTo(getPriceTo());
        ev.setOrderBy(getOrderBy());

        return ev;
    }

    public void addStatements(ArrayList<StatementItem> items) {
        if (statements == null) {
            statements = new ArrayList<>();
        }

        if (items != null) {
            statements.addAll(items);
        }
    }

    public ArrayList<StatementItem> getStatements() {
        return statements;
    }

    public void setStatements(ArrayList<StatementItem> statements) {
        this.statements = statements;
    }

    public boolean canLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    public boolean isSelling() {
        return selling;
    }

    public void setSelling(boolean selling) {
        this.selling = selling;
    }

    public boolean isRent() {
        return rent;
    }

    public void setRent(boolean rent) {
        this.rent = rent;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public ArrayList<String> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<String> locations) {
        this.locations = locations;
    }

    public BigDecimal getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(BigDecimal priceFrom) {
        this.priceFrom = priceFrom;
    }

    public BigDecimal getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(BigDecimal priceTo) {
        this.priceTo = priceTo;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
