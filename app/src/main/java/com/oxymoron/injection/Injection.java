package com.oxymoron.injection;

import android.content.Context;

import com.example.gurumenaviapp.R;
import com.oxymoron.data.source.RestaurantDetailsRepository;
import com.oxymoron.data.source.local.FavoriteRestaurantDataBase;
import com.oxymoron.data.source.local.RestaurantDetailsLocalDataSource;
import com.oxymoron.data.source.remote.RestaurantDetailsRemoteDataSource;
import com.oxymoron.data.source.remote.api.RestaurantSearchApiClientImpl;
import com.oxymoron.util.multi.AppExecutors;

public class Injection {

    public static RestaurantDetailsRepository provideRestaurantDetailsRepository(Context context) {
        FavoriteRestaurantDataBase dataBase = FavoriteRestaurantDataBase.getInstance(context);

        final String token = context.getResources().getString(R.string.api_token);

        return RestaurantDetailsRepository.getInstance(
                RestaurantDetailsLocalDataSource
                        .getInstance(new AppExecutors(), dataBase.restaurantDetailsDao()),

                RestaurantDetailsRemoteDataSource
                        .getInstance(RestaurantSearchApiClientImpl.getInstance(token))
        );
    }

    public static RestaurantSearchApiClientImpl provideRestaurantSearchApiClientImpl(Context context) {
        final String token = context.getResources().getString(R.string.api_token);

        return RestaurantSearchApiClientImpl.getInstance(token);
    }
}
