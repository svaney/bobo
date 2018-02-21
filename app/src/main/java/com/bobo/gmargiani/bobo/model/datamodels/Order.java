package com.bobo.gmargiani.bobo.model.datamodels;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by gmargiani on 2/5/2018.
 */

public class Order {
    private ArrayList<ProductItem> products = new ArrayList<>();
    private String OrderComment;
    private OrderAddress orderAddress = new OrderAddress();
    private boolean fullAmount = true;
    private boolean asap = true;
    private long receiveDate;
    private String contactNumber;
    private BigDecimal totalAmount;
    private BigDecimal deliveryAmount;
    private String phone;

    public ArrayList<ProductItem> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductItem> products) {
        this.products = products;
    }

    public String getOrderComment() {
        return OrderComment;
    }

    public void setOrderComment(String orderComment) {
        OrderComment = orderComment;
    }

    public OrderAddress getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(OrderAddress orderAddress) {
        this.orderAddress = orderAddress;
    }

    public boolean isFullAmount() {
        return fullAmount;
    }

    public void setFullAmount(boolean fullAmount) {
        this.fullAmount = fullAmount;
    }

    public boolean isAsap() {
        return asap;
    }

    public void setAsap(boolean asap) {
        this.asap = asap;
    }

    public long getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(long receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(BigDecimal deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }
}
