package com.bobo.gmargiani.bobo.model;

import android.content.Context;

import com.bobo.gmargiani.bobo.utils.Utils;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@Parcel
public class StatementItem {
    private String _id;
    private String userId;
    private ArrayList<String> imageLinks;
    private String title;
    private String description;
    private String createDate;
    private BigDecimal price;
    private boolean isFavorite;
    private int totalViews;
    private int totalFavorites;
    private String locationId;
    private boolean selling;
    private boolean renting;
    private String categoryId;
    private boolean isArchived;
    private Double lat;
    private Double lon;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public boolean isArchive() {
        return isArchived;
    }

    public void setArchive(boolean archive) {
        this.isArchived = archive;
    }

    public String getMainImage() {
        if (imageLinks != null && imageLinks.size() > 0) {
            return imageLinks.get(0);
        }
        return null;
    }

    public String getOwnerId() {
        return userId;
    }

    public void setOwnerId(String ownerId) {
        this.userId = ownerId;
    }

    public String getStatementId() {
        return _id;
    }

    public void setStatementId(String statementId) {
        this._id = statementId;
    }

    public ArrayList<String> getImages() {
        return imageLinks;
    }

    public void setImages(ArrayList<String> images) {
        this.imageLinks = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateDate() {
        try {
            return createDate.substring(0, 10);
        } catch (Exception e) {

        }
        return "";
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(int totalViews) {
        this.totalViews = totalViews;
    }

    public int getTotalFavorites() {
        return totalFavorites;
    }

    public void setTotalFavorites(int totalFavorites) {
        this.totalFavorites = totalFavorites;
    }

    public String getLocation() {
        return locationId;
    }

    public void setLocation(String location) {
        this.locationId = location;
    }

    public boolean isSelling() {
        return selling;
    }

    public void setSelling(boolean selling) {
        this.selling = selling;
    }

    public boolean isRenting() {
        return renting;
    }

    public void setRenting(boolean renting) {
        this.renting = renting;
    }

    public String getCategory() {
        return categoryId;
    }

    public void setCategory(String category) {
        this.categoryId = category;
    }
}
