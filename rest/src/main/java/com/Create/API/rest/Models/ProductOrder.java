package com.Create.API.rest.Models;


public class ProductOrder {

        private Product product;
        private long tmpProductId;
        private long orderId;
        private long serviceId;


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getTmpProductId() {
        return tmpProductId;
    }

    public void setTmpProductId(long tmpProductId) {
        this.tmpProductId = tmpProductId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return "ProductOrder{" +
                "product=" + product +
                ", tmpProductId=" + tmpProductId +
                ", orderId=" + orderId +
                ", serviceId=" + serviceId +
                '}';
    }
}
