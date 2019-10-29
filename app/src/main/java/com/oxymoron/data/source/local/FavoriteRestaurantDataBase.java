package com.oxymoron.data.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.typeconverter.ImageUrlConverter;
import com.oxymoron.data.typeconverter.PhoneNumberConverter;
import com.oxymoron.data.typeconverter.RestaurantIdConverter;

@Database(entities = {RestaurantDetail.class}, version = 1)
@TypeConverters({RestaurantIdConverter.class, PhoneNumberConverter.class, ImageUrlConverter.class})
public abstract class FavoriteRestaurantDataBase extends RoomDatabase {
    private static FavoriteRestaurantDataBase ourInstance;

    public abstract RestaurantDetailsDao restaurantDetailsDao();

    private static final Object lock = new Object();

    public static FavoriteRestaurantDataBase getInstance(Context context) {
        synchronized (lock) {
            if (ourInstance == null) {
                return Room.databaseBuilder(
                        context.getApplicationContext(),
                        FavoriteRestaurantDataBase.class,
                        "FavoriteRestaurant.db").build();
            }
            return ourInstance;
        }
    }
}
