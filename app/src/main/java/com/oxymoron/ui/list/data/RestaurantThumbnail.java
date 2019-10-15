package com.oxymoron.ui.list.data;

import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.source.local.data.RestaurantId;
import com.oxymoron.data.source.remote.api.gson.data.Rest;
import com.oxymoron.util.Optional;

import java.util.ArrayList;
import java.util.List;

public class RestaurantThumbnail {
    private final String name;
    private final String access;
    private final String imageUrl;

    private boolean favorite;
    private final RestaurantId restaurantId;

    public static RestaurantThumbnail createRestaurantThumbnail(RestaurantDetail restaurantDetail) {
        return new RestaurantThumbnail(
                restaurantDetail.getId(), restaurantDetail.getName(),
                restaurantDetail.getAccess(), restaurantDetail.getImageUrl().getUrl().getOrElse(null),
                restaurantDetail.isFavorite()
        );
    }

    public RestaurantThumbnail(Rest restaurant, boolean favorite) {
        this.name = restaurant.getName();
        this.access = restaurant.getAccess().showUserAround();
        this.imageUrl = restaurant.getImageUrl().getShopImage().getOrElse(null);

        this.restaurantId = new RestaurantId(restaurant.getId());

        this.favorite = favorite;
    }

    private RestaurantThumbnail(RestaurantId restaurantId,
                                String name,
                                String access,
                                String imageUrl,
                                boolean favorite) {

        this.restaurantId = restaurantId;
        this.name = name;
        this.access = access;
        this.imageUrl = imageUrl;
        this.favorite = favorite;
    }

    public RestaurantThumbnail(Rest restaurant) {
        this(restaurant, false);
    }

    public void addToFavorities() {
        this.favorite = true;
    }

    public void removeFromFavorities() {
        this.favorite = false;
    }

    public String getName() {
        return name;
    }

    public Optional<String> getAccess() {
        return Optional.of(access);
    }

    public Optional<String> getImageUrl() {
        return Optional.of(imageUrl);
    }

    public RestaurantId getId() {
        return restaurantId;
    }

    public boolean isFavorite() {
        return this.favorite;
    }

    public static List<RestaurantThumbnail> createRestaurantThumbnailList(List<Rest> restaurantList) {
        final List<RestaurantThumbnail> restaurantThumbnailList = new ArrayList<>();

        for (Rest restaurant : restaurantList) {
            final RestaurantThumbnail restaurantThumbnail = new RestaurantThumbnail(restaurant);
            restaurantThumbnailList.add(restaurantThumbnail);
        }

        return restaurantThumbnailList;
    }
}
