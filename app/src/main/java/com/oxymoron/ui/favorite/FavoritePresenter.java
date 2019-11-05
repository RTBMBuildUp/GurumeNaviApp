package com.oxymoron.ui.favorite;

import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.gurumenaviapp.R;
import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.RestaurantThumbnail;
import com.oxymoron.data.source.RestaurantDetailsDataSource;
import com.oxymoron.data.source.RestaurantDetailsRepository;
import com.oxymoron.data.source.local.data.RestaurantId;
import com.oxymoron.util.Consumer;

import java.util.List;

public class FavoritePresenter implements FavoriteContract.Presenter {
    private FavoriteContract.View view;
    private RestaurantDetailsRepository restaurantDetailsRepository;

    FavoritePresenter(FavoriteContract.View view, RestaurantDetailsRepository restaurantDetailsRepository) {
        this.view = view;
        this.restaurantDetailsRepository = restaurantDetailsRepository;
    }

    @Override
    public void setItem(List<RestaurantThumbnail> restaurantThumbnailList, RestaurantThumbnail item) {
        try {
            if (restaurantThumbnailList != null) {
                int index = restaurantThumbnailList.indexOf(item);

                if (index == -1) {
                    restaurantThumbnailList.add(item);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("FavoritePresenter", "setItem: " + e);
        }
    }

    @Override
    public void clearItem(List<RestaurantThumbnail> restaurantThumbnailList) {
        restaurantThumbnailList.clear();
    }

    @Override
    public void clearThumbnail() {
        view.clearRecyclerView();
    }

    @Override
    public void showThumbnails() {
        loadRestaurantThumbnails(view::addRecyclerView);
    }

    @Override
    public void onClickItem(RestaurantThumbnail restaurantThumbnail) {
        final RestaurantId restaurantId = restaurantThumbnail.getId();

        view.startRestaurantDetailActivity(restaurantId);
    }

    @Override
    public void onClickFavoriteIcon(RestaurantThumbnail restaurantThumbnail) {
        Log.d("log", "onClickFavoriteIcon: presenter");
        restaurantThumbnail.switchFavorites();
    }

    @Override
    public void onUpdateFavorites(RestaurantThumbnail restaurantThumbnail, ImageView favoriteIcon, Animation animation) {
        restaurantThumbnail.setOnUpdateFavorites(isFavorite -> {
            if (isFavorite)
                favoriteIcon.startAnimation(animation);

            favoriteIcon.setImageResource(isFavorite ? R.drawable.ic_favorite_pink_24dp : R.drawable.ic_favorite_border_gray_24dp);
        });
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {

    }

    private void loadRestaurantThumbnails(Consumer<RestaurantThumbnail> function) {
        this.restaurantDetailsRepository.getRestaurantDetails(new RestaurantDetailsDataSource.LoadRestaurantDetailsCallback() {
            @Override
            public void onRestaurantDetailsLoaded(List<RestaurantDetail> restaurantDetailList) {
                for (RestaurantDetail restaurantDetail : restaurantDetailList) {
                    RestaurantThumbnail restaurantThumbnail =
                            RestaurantThumbnail.createRestaurantThumbnail(restaurantDetail);

                    if (restaurantDetail.isFavorite()) {
                        function.accept(restaurantThumbnail);
                    }
                }
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }
}
