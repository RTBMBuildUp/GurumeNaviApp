package com.oxymoron.api.search;

import com.oxymoron.api.search.gson.data.RestaurantSearchResult;
import com.oxymoron.api.search.serializable.LocationInformation;
import com.oxymoron.api.search.serializable.Range;
import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.room.RestaurantId;
import com.oxymoron.util.Consumer;

import java.util.List;

import retrofit2.Callback;

public interface RestaurantSearchApiClient {
    void loadRestaurantDetail(RestaurantId restaurantId, Callback<RestaurantSearchResult> callback);

    void loadRestaurantDetails(List<RestaurantId> restaurantIdList, Consumer<List<RestaurantDetail>> function);

    void loadRestaurantList(Range range, LocationInformation locationInformation, Consumer<RestaurantSearchResult> function);

    void loadRestaurantList(Range range, LocationInformation locationInformation, PageState pageState, Consumer<RestaurantSearchResult> function);

}
