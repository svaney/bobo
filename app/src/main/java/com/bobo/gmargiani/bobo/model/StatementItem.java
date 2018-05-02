package com.bobo.gmargiani.bobo.model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class StatementItem {
    private long statementId;
    private long ownerId;
    private ArrayList<String> images;
    private String title;
    private String description;
    private Long createDate;
    private BigDecimal price;
    private boolean isFavorite;
    private int totalViews;
    private int totalFavorites;
    private OwnerDetails ownerDetails;
    private String location;
    private boolean isSelling;
    private boolean isRenting;
    private String category;

    public String getMainImage() {
        if (images != null && images.size() > 0) {
            return images.get(0);
        }
        return null;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public long getStatementId() {
        return statementId;
    }

    public void setStatementId(long statementId) {
        this.statementId = statementId;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
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

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
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

    public void setOwnerDetails(OwnerDetails ownerDetails) {
        this.ownerDetails = ownerDetails;
    }

    public OwnerDetails getOwnerDetails() {
        return ownerDetails;
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
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
