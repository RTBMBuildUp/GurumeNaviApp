package com.oxymoron.api.search;

import com.oxymoron.api.search.gson.data.RestaurantSearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestaurantSearchApi {
    @GET("RestSearchAPI/v3/")
    Call<RestaurantSearchResult> getRestaurantSearchResult(
            @Query("keyid") String keyid,
            @Query(value = "id", encoded = true) String id);

    @GET("RestSearchAPI/v3/")
    Call<RestaurantSearchResult> getRestaurantSearchResult(
            @Query("keyid") String keyid,
            @Query("range") Integer range,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude);

    @GET("RestSearchAPI/v3/")
    Call<RestaurantSearchResult> getRestaurantSearchResult(
            @Query("keyid") String keyid,
            @Query("range") Integer range,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("offset_page") String offset_page);
}
