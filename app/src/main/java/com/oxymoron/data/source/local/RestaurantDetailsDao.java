package com.oxymoron.data.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.source.local.data.RestaurantId;

import java.util.List;

@Dao
public interface RestaurantDetailsDao {
    @Query("SELECT * FROM restaurant_detail")
    List<RestaurantDetail> getRestaurantDetails();

    @Query("SELECT * FROM restaurant_detail WHERE restaurant_id IN (:restaurantIdList)")
    List<RestaurantDetail> getRestaurantDetails(List<RestaurantId> restaurantIdList);

    @Query("SELECT * FROM restaurant_detail WHERE restaurant_id = :restaurantId")
    RestaurantDetail getRestaurantDetail(RestaurantId restaurantId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRestaurantDetail(RestaurantDetail restaurantDetail);

    @Update
    int updateRestaurantDetail(RestaurantDetail restaurantDetail);

    @Query("DELETE FROM restaurant_detail WHERE restaurant_id = :restaurantId")
    void deleteRestaurantDetail(RestaurantId restaurantId);

    @Query("DELETE FROM restaurant_detail")
    void deleteAllRestaurantDetail();

    @Query("SELECT COUNT(*) FROM restaurant_detail")
    int getCount();

}
