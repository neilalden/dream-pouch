package com.example.test;

public class CustomerSale {
    public String id;
    public String name;
    public String productid;
    public String productname;
    public String productspecs;
    public String image;
    public int amount;
    public String day;
    public CustomerSale(){

    }

    public CustomerSale(String id, String name, String productid,String productname, String productspecs, String image, int amount, String day) {
        this.id = id;
        this.name = name;
        this.productid = productid;
        this.productname = productname;
        this.productspecs = productspecs;
        this.image = image;
        this.amount = amount;
        this.day = day;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductspecs() {
        return productspecs;
    }

    public void setProductspecs(String productspecs) {
        this.productspecs = productspecs;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
