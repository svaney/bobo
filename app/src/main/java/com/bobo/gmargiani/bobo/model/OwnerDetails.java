package com.bobo.gmargiani.bobo.model;

import com.bobo.gmargiani.bobo.evenbuts.events.OwnerStatementsEvent;

import org.parceler.Parcel;

import java.util.ArrayList;


@Parcel
public class OwnerDetails {
    private String _id;
    private String firstName;
    private String lastName;
    private boolean isCompany;
    private String companyName;
    private String phoneNum;
    private String location;
    private String avatar;
    private String email;
    private ArrayList<String> subscribedUsers;
    private ArrayList<String> favourites;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOwnerId() {
        return _id;
    }

    public void setOwnerId(String ownerId) {
        this._id = ownerId;
    }

    public String getOwnerName() {
        return firstName;
    }

    public void setOwnerName(String ownerName) {
        this.firstName = ownerName;
    }

    public String getOwnerSecondName() {
        return lastName;
    }

    public void setOwnerSecondName(String ownerSecondName) {
        this.lastName = ownerSecondName;
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
        return phoneNum;
    }

    public void setPhone(String phone) {
        this.phoneNum = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDisplayName() {
        if (isCompany) {
            return getCompanyName();
        }

        return getOwnerName() + " " + getOwnerSecondName();
    }
}
