package com.oxymoron.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.oxymoron.data.source.local.data.ImageUrl;
import com.oxymoron.data.source.local.data.PhoneNumber;
import com.oxymoron.data.source.local.data.RestaurantId;
import com.oxymoron.data.source.remote.api.gson.data.Rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "restaurant_detail")
public class RestaurantDetail {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "restaurant_id")
    private final RestaurantId id;

    @NonNull
    @ColumnInfo(name = "name")
    private final String name;

    @Nullable
    @ColumnInfo(name = "phone_number")
    private final PhoneNumber phoneNumber;

    @Nullable
    @ColumnInfo(name = "address")
    private final String address;

    @Nullable
    @ColumnInfo(name = "access")
    private final String access;

    @Nullable
    @ColumnInfo(name = "open_time")
    private final String openTime;

    @Nullable
    @ColumnInfo(name = "image_url")
    private final ImageUrl imageUrl;

    @ColumnInfo(name = "favorite")
    private boolean favorite;

    public RestaurantDetail(@NonNull RestaurantId id,
                            @NonNull String name,
                            @Nullable PhoneNumber phoneNumber,
                            @Nullable String address,
                            @Nullable String access,
                            @Nullable String openTime,
                            @NonNull ImageUrl imageUrl,
                            boolean favorite
    ) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.access = access;
        this.openTime = openTime;
        this.imageUrl = imageUrl;
        this.favorite = favorite;
    }

    public RestaurantDetail(@NonNull Rest rest) {
        this(new RestaurantId(rest.getId()), rest.getName(), new PhoneNumber(rest.getTel().getOrElse("")),
                rest.getAddress(), rest.getAccess().showUserAround(), rest.getOpentime(),
                new ImageUrl(rest.getImageUrl().getShopImage().getOrElse(null)), false
        );
    }

    public void addToFavorities() {
        this.favorite = true;
    }

    public void removeFromFavorities() {
        this.favorite = false;
    }

    @NonNull
    public RestaurantId getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    @Nullable
    public String getAccess() {
        return access;
    }

    @Nullable
    public String getOpenTime() {
        return openTime;
    }

    @Nullable
    public ImageUrl getImageUrl() {
        return imageUrl;
    }

    public boolean isFavorite() {
        return favorite;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof RestaurantDetail)) return false;
        RestaurantDetail restaurantDetail = ((RestaurantDetail) obj);
        return Objects.equals(this.getId(), restaurantDetail.getId());
    }

    public static List<RestaurantDetail> createRestaurantDetailList(List<Rest> restList) {
        final List<RestaurantDetail> restaurantDetailList = new ArrayList<>();

        for (Rest rest : restList) {
            final RestaurantDetail restaurantDetail = new RestaurantDetail(rest);

            restaurantDetailList.add(restaurantDetail);
        }

        return restaurantDetailList;
    }
}
