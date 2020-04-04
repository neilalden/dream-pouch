package com.example.test;

public class Sales {
    public String id;
    public String name;
    public int sold;
    public Sales() {
    }

    public Sales(String id, String name, int sold) {
        this.id = id;
        this.name = name;
        this.sold = sold;
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

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }
}
