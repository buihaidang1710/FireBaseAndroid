package com.example.firebase_giandon.model;

public class Work {
    private String key,name,date,desc,image;

    public Work() {
    }

    public Work(String key, String name, String date, String desc, String image) {
        this.key = key;
        this.name = name;
        this.date = date;
        this.desc = desc;
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
