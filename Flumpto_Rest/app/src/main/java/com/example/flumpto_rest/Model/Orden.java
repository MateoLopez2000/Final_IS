package com.example.flumpto_rest.Model;

public class Orden {
    private  long OrderId;
    private float OrderPrice;
    private String OrderDetail,OrderAddress,UserPhone,NIT;

    public Orden() {
    }

    public long getOrderId() {
        return OrderId;
    }

    public void setOrderId(long orderId) {
        OrderId = orderId;
    }

    public float getOrderPrice() {
        return OrderPrice;
    }

    public void setOrderPrice(float orderPrice) {
        OrderPrice = orderPrice;
    }

    public String getOrderDetail() {
        return OrderDetail;
    }

    public void setOrderDetail(String orderDetail) {
        OrderDetail = orderDetail;
    }

    public String getOrderAddress() {
        return OrderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        OrderAddress = orderAddress;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getNIT() {
        return NIT;
    }

    public void setNIT(String NIT) {
        this.NIT = NIT;
    }
}
