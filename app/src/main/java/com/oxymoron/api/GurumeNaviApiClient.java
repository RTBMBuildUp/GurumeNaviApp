package com.oxymoron.api;

import com.oxymoron.api.gson.data.GurumeNavi;
import com.oxymoron.util.Consumer;

public interface GurumeNaviApiClient {
    void loadRestaurantDetail(String restaurantId, Consumer<GurumeNavi> function);

    void loadRestaurantList(Integer range, String latitude, String longitude, String offset_page, Consumer<GurumeNavi> function);

    void loadRestaurantList(Integer range, String latitude, String longitude, Consumer<GurumeNavi> function);
}
