package com.oxymoron.data;

import com.oxymoron.data.source.local.data.RestaurantId;
import com.oxymoron.data.source.remote.api.gson.data.Rest;
import com.oxymoron.util.Consumer;
import com.oxymoron.util.Optional;

import java.util.ArrayList;
import java.util.List;

public class RestaurantThumbnail {
    private final String name;
    private final String access;
    private final String imageUrl;

    private final Favorite favorite;
    private final RestaurantId restaurantId;

    public static RestaurantThumbnail createRestaurantThumbnail(RestaurantDetail restaurantDetail) {
        return new RestaurantThumbnail(
                restaurantDetail.getId(), restaurantDetail.getName(),
                restaurantDetail.getAccess(), restaurantDetail.getImageUrl().getUrl().getOrElse(null),
                new Favorite(restaurantDetail.isFavorite())
        );
    }

    public RestaurantThumbnail(Rest restaurant, Favorite favorite) {
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
                                Favorite favorite) {

        this.restaurantId = restaurantId;
        this.name = name;
        this.access = access;
        this.imageUrl = imageUrl;
        this.favorite = favorite;
    }

    public RestaurantThumbnail(Rest restaurant) {
        this(restaurant, new Favorite(false));
    }

    public void addToFavorites() {
        this.favorite.addToFavorites();
    }

    public void removeFromFavorites() {
        this.favorite.removeFromFavorites();
    }

    public void switchFavorites() {
        this.favorite.switchFavorites();
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

    public Boolean isFavorite() {
        return favorite.isFavorite();
    }

    public void setOnUpdateFavorites(Consumer<Boolean> onUpdateFavorites) {
        this.favorite.setOnUpdate(onUpdateFavorites);
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
