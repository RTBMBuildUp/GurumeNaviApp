package com.oxymoron.api;

import com.oxymoron.api.gson.data.GurumeNavi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GurumeNaviApi {
    @GET("RestSearchAPI/v3/")
    Call<GurumeNavi> getGurumeNavi(@Query("keyid") String keyid, @Query("id") String id);

    @GET("RestSearchAPI/v3/")
    Call<GurumeNavi> getGurumeNavi(
            @Query("keyid") String keyid,
            @Query("range") Integer range,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude);

    @GET("RestSearchAPI/v3/")
    Call<GurumeNavi> getGurumeNavi(
            @Query("keyid") String keyid,
            @Query("range") Integer range,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("offset_page") String offset_page);

}
