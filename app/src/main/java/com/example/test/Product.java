package com.example.test;

public class Product {
    private String id;
    private String type;
    private String name;
    private String specs;
    private int price;
    private int stocks;

    public Product() {
    }

    public Product(String id, String type, String name, String specs, int price, int stocks) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.specs = specs;
        this.price = price;
        this.stocks = stocks;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStocks() {
        return stocks;
    }

    public void setStocks(int stocks) {
        this.stocks = stocks;
    }
}
