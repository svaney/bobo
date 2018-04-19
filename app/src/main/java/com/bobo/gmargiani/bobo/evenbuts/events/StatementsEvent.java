package com.bobo.gmargiani.bobo.evenbuts.events;

import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class StatementsEvent extends RootEvent {
    private boolean canLoadMore = true;
    private int from;

    private boolean selling;
    private boolean rent;
    private String category = "";
    private String location = "";
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private String orderBy = "";

    private ArrayList<StatementItem> statements = new ArrayList<>();

    public StatementsEvent(){

    }

    public StatementsEvent(boolean sell, boolean rent, String category, String location,
                           BigDecimal priceFrom, BigDecimal priceTo, String orderBy){
        setSelling(sell);
        setRent(rent);
        setCategory(category);
        setLocation(location);
        setPriceTo(priceFrom);
        setPriceTo(priceTo);
        setOrderBy(orderBy);
    }

    public boolean hasSameParameters(boolean sell, boolean rent, String category, String location,
                                     BigDecimal priceFrom, BigDecimal priceTo, String orderBy) {

        if (isRent() != rent || isSelling() != sell
                || !Utils.equals(getCategory(), category)
                || !Utils.equals(getLocation(), location)
                || !Utils.equals(getPriceFrom(), priceFrom)
                || !Utils.equals(getPriceTo(), priceTo)
                || !Utils.equals(getOrderBy(), orderBy)) {
            return false;
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
        ev.setCategory(getCategory());
        ev.setLocation(getLocation());
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
