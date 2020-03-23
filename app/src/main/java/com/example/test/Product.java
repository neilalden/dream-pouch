package com.example.test;

public class Product {
    private String id;
    private String type;
    private String name;
    private String specs;
    private String image;
    private float price;
    private int stocks;

    public Product() {
    }

    public Product(String id, String type, String name, String specs, float price, int stocks, String image) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.specs = specs;
        this.price = price;
        this.stocks = stocks;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStocks() {
        return stocks;
    }

    public void setStocks(int stocks) {
        this.stocks = stocks;
    }
}
