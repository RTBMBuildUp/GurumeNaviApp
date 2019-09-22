package com.oxymoron.data.source.remote.api;

import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.room.RestaurantId;
import com.oxymoron.data.source.remote.api.gson.data.RestaurantSearchResult;
import com.oxymoron.data.source.remote.api.serializable.LocationInformation;
import com.oxymoron.data.source.remote.api.serializable.Range;
import com.oxymoron.util.Consumer;

import java.util.List;

import retrofit2.Callback;

public interface RestaurantSearchApiClient {
    void loadRestaurantDetail(RestaurantId restaurantId, Callback<RestaurantSearchResult> callback);

    void loadRestaurantDetails(List<RestaurantId> restaurantIdList, Consumer<List<RestaurantDetail>> function);

    void loadRestaurantList(Range range, LocationInformation locationInformation, Consumer<RestaurantSearchResult> function);

    void loadRestaurantList(Range range, LocationInformation locationInformation, PageState pageState, Consumer<RestaurantSearchResult> function);

}
