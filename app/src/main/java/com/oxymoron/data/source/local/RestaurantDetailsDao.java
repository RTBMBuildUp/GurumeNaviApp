package com.oxymoron.data.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.room.RestaurantId;

import java.util.List;

@Dao
public interface RestaurantDetailsDao {
    @Query("SELECT * FROM restaurant_detail")
    public List<RestaurantDetail> getRestaurantDetails();

    @Query("SELECT * FROM restaurant_detail WHERE restaurant_id IN (:restaurantIdList)")
    public List<RestaurantDetail> getRestaurantDetails(List<RestaurantId> restaurantIdList);

    @Query("SELECT * FROM restaurant_detail WHERE restaurant_id = :restaurantId")
    public RestaurantDetail getRestaurantDetail(RestaurantId restaurantId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertRestaurantDetail(RestaurantDetail restaurantDetail);

    @Update
    public int updateRestaurantDetail(RestaurantDetail restaurantDetail);

    @Query("DELETE FROM restaurant_detail WHERE restaurant_id = :restaurantId")
    public void deleteRestaurantDetail(RestaurantId restaurantId);

    @Query("DELETE FROM restaurant_detail")
    public void deleteAllRestaurantDetail();

    @Query("SELECT COUNT(*) FROM restaurant_detail")
    public int getCount();

}
