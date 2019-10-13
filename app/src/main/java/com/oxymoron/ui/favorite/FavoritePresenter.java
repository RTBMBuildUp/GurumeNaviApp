package com.oxymoron.ui.favorite;

import android.util.Log;

import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.source.RestaurantDetailsDataSource;
import com.oxymoron.data.source.RestaurantDetailsRepository;
import com.oxymoron.ui.list.data.RestaurantThumbnail;
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
    public void saveFavoriteRestaurants(List<RestaurantThumbnail> restaurantThumbnailList) {
        for (RestaurantThumbnail restaurantThumbnail : restaurantThumbnailList) {
            if (!restaurantThumbnail.isFavorite()) {
                this.restaurantDetailsRepository.getRestaurantDetail(restaurantThumbnail.getId(), new RestaurantDetailsDataSource.GetRestaurantDetailsCallback() {
                    @Override
                    public void onRestaurantDetailLoaded(RestaurantDetail restaurantDetail) {
                        Log.d("log", "onRestaurantDetailLoaded: saveFav");
                        restaurantDetail.removeFromFavorities();
                        restaurantDetailsRepository.saveRestaurantDetail(restaurantDetail);
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
            }
        }
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
