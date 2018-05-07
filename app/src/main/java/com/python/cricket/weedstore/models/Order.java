package com.python.cricket.weedstore.models;

public class Order {

    private int id;
    private int customerId;
    private boolean status;
    private Product[] productList;
    private Date orderDate;

    public com.python.cricket.weedstore.models.Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(com.python.cricket.weedstore.models.Date orderDate) {
        this.orderDate = orderDate;
    }

    public Product[] getProductList() {
        return productList;
    }

    public void setProductList(Product[] productList) {
        this.productList = productList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
