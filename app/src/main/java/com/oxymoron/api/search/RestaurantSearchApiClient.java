package com.oxymoron.api.search;

import com.oxymoron.api.search.gson.data.RestaurantSearchResult;
import com.oxymoron.api.search.serializable.LocationInformation;
import com.oxymoron.api.search.serializable.Range;
import com.oxymoron.api.search.serializable.RestaurantId;
import com.oxymoron.util.Consumer;

public interface RestaurantSearchApiClient {
    void loadRestaurantDetail(RestaurantId restaurantId, Consumer<RestaurantSearchResult> function);

    void loadRestaurantList(Range range, LocationInformation locationInformation, Consumer<RestaurantSearchResult> function);

    void loadRestaurantList(Range range, LocationInformation locationInformation, PageState pageState, Consumer<RestaurantSearchResult> function);

}
