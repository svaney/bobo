package com.bobo.gmargiani.bobo.model.datamodels;

import org.parceler.Parcel;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by gmargiani on 1/31/2018.
 */

public class ProductItem {
    private String title;
    private int unitType;
    private int unitMeasureType;
    private boolean hasSize;
    private BigDecimal size;
    private String comment;
    private File file;
    private int amount;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUnitType() {
        return unitType;
    }

    public void setUnitType(int unitType) {
        this.unitType = unitType;
    }

    public int getMeasureUnitType() {
        return unitMeasureType;
    }

    public void setUnitMeasureType(int measureUnit) {
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
