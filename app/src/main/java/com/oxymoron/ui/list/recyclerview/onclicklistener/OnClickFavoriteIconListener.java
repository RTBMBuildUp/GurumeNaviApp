package com.oxymoron.ui.list.recyclerview.onclicklistener;

import android.widget.ImageView;

import com.oxymoron.data.RestaurantThumbnail;

public interface OnClickFavoriteIconListener {
    public void onClick(ImageView favoriteIcon, RestaurantThumbnail restaurantThumbnail);
}
