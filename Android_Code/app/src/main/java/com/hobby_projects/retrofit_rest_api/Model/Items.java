package com.hobby_projects.retrofit_rest_api.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Items implements Serializable {

    @SerializedName("id") private int id = 0;
    @SerializedName("name") private String name = "";
    @SerializedName("description") private String description = "";
    @SerializedName("price") private int price = 0;
    @SerializedName("category_id") private int category_id = 0;
    @SerializedName("created") private String created = "";
    @SerializedName("modified") private String modified = "";


    public Items() {
    }

    public Items(int id, String name, String description, int price, int category_id, String created, String modified) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category_id = category_id;
        this.created = created;
        this.modified = modified;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getCategory_id() {
        return category_id;
    }

    public String getCreated() {
        return created;
    }

    public String getModified() {
        return modified;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
