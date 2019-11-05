package com.oxymoron.ui.list.recyclerview;

import android.widget.ImageView;

import com.oxymoron.data.RestaurantThumbnail;

public interface OnUpdateFavorites {
    void onUpdateFavorites(RestaurantThumbnail restaurantThumbnail, ImageView favoriteIcon);
}
