package com.hobby_projects.retrofit_rest_api.API;

import com.hobby_projects.retrofit_rest_api.Constants;
import com.hobby_projects.retrofit_rest_api.Model.AllItems;
import com.hobby_projects.retrofit_rest_api.Model.Items;
import com.hobby_projects.retrofit_rest_api.Model.API_Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface Api {
    String BASE_URL = "http://"+ Constants.IP_ADDRESS+"/rest-api/items/";

    @POST("create")
    Call<API_Response> createItem(@Body Items item);

    @GET("read")
    Call<AllItems> getItems();

    @GET("readSet")
    Call<AllItems> getSetOfItems(@Query("set") String set); // Starts at 0

    @GET("read")
    Call<AllItems> getItem(@Query("id") String id);

    @PUT("update")
    Call<API_Response> updateItem(@Body Items item);

    @DELETE("delete")
    Call<API_Response> deleteItem(@Query("id") String id);
}
