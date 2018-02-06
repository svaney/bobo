package com.bobo.gmargiani.bobo.model;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by gmargiani on 1/31/2018.
 */

public class ProductItem {
    String title;
    short unitType;
    short unitMeasureType;
    boolean hasSize;
    BigDecimal size;
    String comment;
    File file;
    int amount;

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

    public short getMeasureUnitType() {
        return unitMeasureType;
    }

    public void setUnitMeasureType(short measureUnit) {
        this.unitMeasureType = measureUnit;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean hasSize() {
        return hasSize;
    }

    public void setHasSize(boolean hasSize) {
        this.hasSize = hasSize;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
