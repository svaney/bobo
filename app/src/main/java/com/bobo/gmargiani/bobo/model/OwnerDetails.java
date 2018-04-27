package com.bobo.gmargiani.bobo.model;

public class OwnerDetails {
    private long ownerId;
    private String ownerName;
    private String ownerSecondName;
    private boolean isCompany;
    private String companyName;
    private String phone;
    private String location;
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerSecondName() {
        return ownerSecondName;
    }

    public void setOwnerSecondName(String ownerSecondName) {
        this.ownerSecondName = ownerSecondName;
    }

    public boolean isCompany() {
        return isCompany;
    }

    public void setCompany(boolean company) {
        isCompany = company;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
