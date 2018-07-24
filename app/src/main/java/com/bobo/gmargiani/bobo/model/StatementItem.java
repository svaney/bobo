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
    private String locationId;
    private boolean selling;
    private boolean renting;
    private String categoryId;
    private boolean archive;

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
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
