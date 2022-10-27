package com.hobby_projects.retrofit_rest_api.Model;

import java.io.Serializable;
import java.util.List;

public class AllItems implements Serializable {
    String status;
    List<Items> items;

    public AllItems(String status, List<Items> items) {
        this.status = status;
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public List<Items> getItems() {
        return items;
    }
}
