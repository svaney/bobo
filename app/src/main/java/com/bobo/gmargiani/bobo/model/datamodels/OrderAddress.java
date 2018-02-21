package com.bobo.gmargiani.bobo.model.datamodels;

import org.parceler.Parcel;

/**
 * Created by gmarg on 2/21/2018.
 */

public class OrderAddress {
    private String address;
    private String addressAdditionalInfo;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressAdditionalInfo() {
        return addressAdditionalInfo;
    }

    public void setAddressAdditionalInfo(String addressAdditionalInfo) {
        this.addressAdditionalInfo = addressAdditionalInfo;
    }
}
