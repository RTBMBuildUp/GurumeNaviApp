package com.oxymoron.api;

import com.oxymoron.gson.data.GurumeNavi;
import com.oxymoron.util.Consumer;

public interface GurumeNaviApiClient {
    void loadRestaurantDetail(String restaurantId, Consumer<GurumeNavi> function);

    void loadRestaurantList(String latitude, String longitude, String hit_per_page, String offset_page, Consumer<GurumeNavi> function);

    void loadRestaurantList(String latitude, String longitude, Consumer<GurumeNavi> function);
}
