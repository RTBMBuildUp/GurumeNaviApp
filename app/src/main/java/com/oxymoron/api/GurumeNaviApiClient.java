package com.oxymoron.api;

import com.oxymoron.api.gson.data.GurumeNavi;
import com.oxymoron.request.LocationInformation;
import com.oxymoron.request.PageState;
import com.oxymoron.request.Range;
import com.oxymoron.util.Consumer;

public interface GurumeNaviApiClient {
    void loadRestaurantDetail(String restaurantId, Consumer<GurumeNavi> function);

    void loadRestaurantList(Range range, LocationInformation locationInformation, Consumer<GurumeNavi> function);

    void loadRestaurantList(Range range, LocationInformation locationInformation, PageState pageState, Consumer<GurumeNavi> function);

}
