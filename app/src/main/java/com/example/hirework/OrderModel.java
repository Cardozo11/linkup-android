package com.example.hirework;

import android.content.Context;

public class OrderModel {

    Context context;

    int orderId;
    int orderAmount;
    int gigId;
    String freelancerUsername;
    String customerUsername;
    String description;
    String orderStatus;
    String gigOrderTitle;
    String completionDays;
    String fileForamt;
    String additionalInfo;
    String orderPlacedOn;
    String paymentStatus;
    String orderTrackingStatus;
    String ActivityPurpose;


    // INTENT KEY
    public final String INTENT_ORDER_ID="order_id";
    public final String INTENT_ORDER_PRICE="order_price";
    public final String INTENT_ORDER_PAYMENT_STATUS="order_payment_status";
    public final String INTENT_ORDER_STATUS="order_status";
    public final String INTENT_ORDER_FREELANCER="freelancer";

    public final String INTENT_PURPOSE="purpose";

//    public OrderModel(String description, String orderStatus, String cUsername, String fUsername, int gId, int price, String orderTrackingStatus, String orderTitle, String completionDays, String fileFormat, String additionalInfo, String orderPlaced, int id) {
//        this.orderId = orderId;
//        this.orderAmount = orderAmount;
//        this.gigId = gigId;
//        this.freelancerUsername = freelancerUsername;
//        this.customerUsername = customerUsername;
//        this.description = description;
//        this.orderStatus = orderStatus;
//        this.gigOrderTitle = gigOrderTitle;
//        this.completionDays = completionDays;
//        this.fileForamt = fileForamt;
//        this.additionalInfo = additionalInfo;
//        this.orderPlacedOn = orderPlacedOn;
//        this.orderTrackingStatus=orderTrackingStatus;
//    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }



    // used in Dashboard.java
    public OrderModel(String description,
                      String orderStatus,
                      String customerUsername,
                      String freelancerUsername,
                      int gigId,
                      int orderAmount,
                      String orderTrackingStatus,
                      String gigOrderTitle,
                      String completionDays,
                      String fileForamt,
                      String additionalInfo,
                      String orderPlacedOn,
                      int orderId) {
        this.orderAmount = orderAmount;
        this.gigId = gigId;
        this.freelancerUsername = freelancerUsername;
        this.customerUsername = customerUsername;
        this.description = description;
        this.orderStatus = orderStatus;
        this.gigOrderTitle = gigOrderTitle;
        this.completionDays = completionDays;
        this.fileForamt = fileForamt;
        this.additionalInfo = additionalInfo;
        this.orderPlacedOn = orderPlacedOn;
        this.orderTrackingStatus=orderTrackingStatus;
        this.orderId = orderId;
    }

    // end of Dashboard.java //


    public OrderModel(int orderId,
                      int orderAmount,
                      int gigId,
                      String freelancerUsername,
                      String customerUsername,
                      String description,
                      String orderStatus,
                      String gigOrderTitle,
                      String orderTrackingStatus) {
        this.orderId = orderId;
        this.orderAmount = orderAmount;
        this.gigId = gigId;
        this.freelancerUsername = freelancerUsername;
        this.customerUsername = customerUsername;
        this.description = description;
        this.orderStatus = orderStatus;
        this.gigOrderTitle = gigOrderTitle;
        this.orderTrackingStatus = orderTrackingStatus;
    }

    public String getOrderPlacedOn() {
        return orderPlacedOn;
    }

    public void setOrderPlacedOn(String orderPlacedOn) {
        this.orderPlacedOn = orderPlacedOn;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public int getGigId() {
        return gigId;
    }

    public void setGigId(int gigId) {
        this.gigId = gigId;
    }

    public String getFreelancerUsername() {
        return freelancerUsername;
    }

    public void setFreelancerUsername(String freelancerUsername) {
        this.freelancerUsername = freelancerUsername;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getCompletionDays() {
        return completionDays;
    }

    public void setCompletionDays(String completionDays) {
        this.completionDays = completionDays;
    }

    public String getFileForamt() {
        return fileForamt;
    }

    public void setFileForamt(String fileForamt) {
        this.fileForamt = fileForamt;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }





    public String getGigOrderTitle() {
        return gigOrderTitle;
    }

    public void setGigOrderTitle(String gigOrderTitle) {
        this.gigOrderTitle = gigOrderTitle;
    }


    public String getOrderTrackingStatus() {
        return orderTrackingStatus;
    }

    public void setOrderTrackingStatus(String orderTrackingStatus) {
        this.orderTrackingStatus = orderTrackingStatus;
    }


    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }



    // in use CustomerOrderStats.jav

    public OrderModel(int orderId,
                      String description,
                      String orderStatus,
                      String customerUsername,
                      String freelancerUsername,
                      int gigId,
                      int orderAmount,
                      String orderTrackingStatus,
                      String gigOrderTitle,
                      String completionDays,
                      String fileForamt,
                      String additionalInfo) {
        this.orderId = orderId;
        this.description = description;
        this.orderStatus=orderStatus;
        this.customerUsername = customerUsername;
        this.freelancerUsername = freelancerUsername;
        this.gigId = gigId;
        this.orderAmount = orderAmount;
        this.orderTrackingStatus=orderTrackingStatus;
        this.gigOrderTitle = gigOrderTitle;
        this.completionDays=completionDays;
        this.fileForamt=fileForamt;
        this.additionalInfo=additionalInfo;


    }

    // end

    // in use

//    public OrderModel(int orderId,String description,String orderStatus, String customerUsername,String freelancerUsername, int gigId,int orderAmount) {
//        this.orderId = orderId;
//        this.description = description;
//        this.orderStatus=orderStatus;
//        this.customerUsername = customerUsername;
//        this.freelancerUsername = freelancerUsername;
//        this.gigId = gigId;
//        this.orderAmount = orderAmount;
//
//    }

    //used in CustomerOrder.java

    public OrderModel(int orderId,String description,String orderStatus, String customerUsername,String freelancerUsername,int gigId,int orderAmount,String paymentStatus,String orderTrackingStatus, String gigOrderTitle,String completionDays,String fileForamt,String additionalInfo) {
        this.orderId = orderId;
        this.description = description;
        this.orderStatus = orderStatus;
        this.customerUsername = customerUsername;
        this.freelancerUsername = freelancerUsername;
        this.gigId = gigId;
        this.orderAmount = orderAmount;
        this.paymentStatus=paymentStatus;
        this.orderTrackingStatus=orderTrackingStatus;
        this.gigOrderTitle = gigOrderTitle;
        this.completionDays = completionDays;
        this.fileForamt = fileForamt;
        this.additionalInfo = additionalInfo;


    }
    // end of CustomerOrders.java



    public OrderModel(Context context) {
        this.context=context;
    }
}
