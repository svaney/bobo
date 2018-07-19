package com.bobo.gmargiani.bobo.model;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.ArrayList;

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
    private String location;
    private boolean isSelling;
    private boolean isRenting;
    private String categoryId;

    public String getMainImage() {
        if (imageLinks != null && imageLinks.size() > 0) {
            return imageLinks.get(0);
        }
        return null;
    }

    public String getOwnerId() {
        return userId;
    }

    public void setOwnerId(String  ownerId) {
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
        return createDate;
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
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isSelling() {
        return isSelling;
    }

    public void setSelling(boolean selling) {
        isSelling = selling;
    }

    public boolean isRenting() {
        return isRenting;
    }

    public void setRenting(boolean renting) {
        isRenting = renting;
    }

    public String getCategory() {
        return categoryId;
    }

    public void setCategory(String category) {
        this.categoryId = category;
    }
}
