package com.bobo.gmargiani.bobo.model;

import java.math.BigDecimal;

/**
 * Created by gmargiani on 1/31/2018.
 */

public class ProductItem {
    public String title;
    public short unitType;
    public short unitMeasureType;
    public BigDecimal amount;
    public String comment;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public short getUnitType() {
        return unitType;
    }

    public void setUnitType(short unitType) {
        this.unitType = unitType;
    }

    public short getMeasureUnit() {
        return unitMeasureType;
    }

    public void setUnitMeasureType(short measureUnit) {
        this.unitMeasureType = measureUnit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
