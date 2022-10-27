package com.hobby_projects.retrofit_rest_api.Model;

public class API_Response {
    String status;
    String message;

    public API_Response(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
