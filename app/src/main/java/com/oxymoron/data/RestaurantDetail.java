package com.oxymoron.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.oxymoron.api.search.gson.data.Access;
import com.oxymoron.api.search.gson.data.Rest;
import com.oxymoron.data.room.ImageUrl;
import com.oxymoron.data.room.PhoneNumber;
import com.oxymoron.data.room.RestaurantId;

import java.util.Objects;

@Entity(tableName = "restaurant_detail")
public class RestaurantDetail {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private final RestaurantId id;

    @Nullable
    @ColumnInfo(name = "phone_number")
    private final PhoneNumber phoneNumber;

    @Nullable
    @ColumnInfo(name = "address")
    private final String address;

    @Nullable
    @ColumnInfo(name = "access")
    private final Access access;

    @Nullable
    @ColumnInfo(name = "open_time")
    private final String openTime;

    @NonNull
    @ColumnInfo(name = "image_url")
    private final ImageUrl imageUrl;

    public RestaurantDetail(@NonNull RestaurantId id,
                            @Nullable PhoneNumber phoneNumber,
                            @Nullable String address,
                            @Nullable Access access,
                            @Nullable String openTime,
                            @NonNull ImageUrl imageUrl
    ) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.access = access;
        this.openTime = openTime;
        this.imageUrl = imageUrl;
    }

    public RestaurantDetail(@NonNull Rest rest) {
        this(new RestaurantId(rest.getId()), new PhoneNumber(rest.getTel()),
                rest.getAddress(), rest.getAccess(), rest.getOpentime(),
                new ImageUrl(rest.getImageUrl().getShopImage().get())
        );
    }

    @NonNull
    public RestaurantId getId() {
        return id;
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
    public Access getAccess() {
        return access;
    }

    @Nullable
    public String getOpenTime() {
        return openTime;
    }

    @NonNull
    public ImageUrl getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        RestaurantDetail restaurantDetail = ((RestaurantDetail) obj);
        return Objects.equals(this.getId(), restaurantDetail.getId());
    }
}
