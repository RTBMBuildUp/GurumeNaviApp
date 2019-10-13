package com.oxymoron.ui.list;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.source.RestaurantDetailsDataSource;
import com.oxymoron.data.source.RestaurantDetailsRepository;
import com.oxymoron.data.source.local.data.RestaurantId;
import com.oxymoron.data.source.remote.api.PageState;
import com.oxymoron.data.source.remote.api.gson.data.RestaurantSearchResult;
import com.oxymoron.data.source.remote.api.serializable.LocationInformation;
import com.oxymoron.data.source.remote.api.serializable.Range;
import com.oxymoron.ui.list.data.RestaurantThumbnail;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListPresenter implements RestaurantListContract.Presenter {
    private final RestaurantListContract.View view;
    private final RestaurantDetailsRepository restaurantDetailsRepository;

    private PageState pageState;

    RestaurantListPresenter(RestaurantListContract.View view,
                            RestaurantDetailsRepository restaurantDetailsRepository) {

        this.view = view;

        this.restaurantDetailsRepository = restaurantDetailsRepository;
    }

    @Override
    public void refreshSavedItem(List<RestaurantThumbnail> restaurantThumbnailList) {
        this.saveRestaurantDetailByRestaurantThumbnail(restaurantThumbnailList);
        this.deleteRestaurantDetailByRestaurantThumbnail(restaurantThumbnailList);
    }

    @Override
    public void search(Range range, LocationInformation locationInformation) {
        this.showThumbnail(range, locationInformation);
    }

    @Override
    public void search(Range range, LocationInformation locationInformation, PageState pageState) {
        this.showThumbnail(range, locationInformation, pageState);
    }

    @Override
    public void setItem(List<RestaurantThumbnail> restaurantThumbnailList, RestaurantThumbnail restaurantThumbnail) {
        if (restaurantThumbnailList != null && !restaurantThumbnailList.contains(restaurantThumbnail)) {
            this.restaurantDetailsRepository.getRestaurantDetails(new RestaurantDetailsDataSource.LoadRestaurantDetailsCallback() {
                @Override
                public void onRestaurantDetailsLoaded(List<RestaurantDetail> restaurantDetailList) {
                    for (RestaurantDetail restaurantDetail : restaurantDetailList) {
                        if (restaurantThumbnail.getId().equals(restaurantDetail.getId()) && restaurantDetail.isFavorite()) {
                            restaurantThumbnail.addToFavorities();
                        }
                    }
                }

                @Override
                public void onDataNotAvailable() {

                }
            });

            restaurantThumbnailList.add(restaurantThumbnail);
        }
    }

    @Override
    public void removeItem(List<RestaurantThumbnail> restaurantThumbnailList, int position) {
        try {
            if (restaurantThumbnailList != null) {
                restaurantThumbnailList.remove(position);
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("RestaurantListPresenter", "removeItem: " + e);
        }
    }

    @Override
    public void cleanItem(List<RestaurantThumbnail> restaurantThumbnailList) {
        while (restaurantThumbnailList != null && restaurantThumbnailList.size() != 0)
            view.removeRecyclerViewItem(0);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, Range range, LocationInformation locationInformation, int itemCount) {
        if (this.pageState != null) {
            final int bottom = 1;
            if (!recyclerView.canScrollVertically(bottom)) {
                try {
                    this.search(range, locationInformation, this.pageState.getNextPageState());
                } catch (ArithmeticException e) {
                    Log.d("RestaurantListPresenter", "onScrolled: " + e);
                }
            }
        }
    }

    @Override
    public void start() {

    }

    private void showThumbnail(Range range, LocationInformation locationInformation) {
        this.restaurantDetailsRepository.getRestaurantDetails(
                range, locationInformation,
                new RestaurantDetailsDataSource.GetRestaurantSearchResultCallback() {
                    @Override
                    public void onRestaurantSearchResultLoaded(RestaurantSearchResult restaurantSearchResult) {
                        RestaurantListPresenter.this.pageState = new PageState(restaurantSearchResult.getHitPerPage());

                        restaurantSearchResult.getRest().ifPresent(restList -> {
                            List<RestaurantThumbnail> restaurantThumbnailList = RestaurantThumbnail.createRestaurantThumbnailList(restList);
                            for (RestaurantThumbnail thumbnail : restaurantThumbnailList) {
                                view.addRecyclerViewItem(thumbnail);
                            }
                        });
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
    }

    private void showThumbnail(Range range, LocationInformation locationInformation, PageState pageState) {
        this.restaurantDetailsRepository.getRestaurantDetails(
                range, locationInformation, pageState,
                new RestaurantDetailsDataSource.GetRestaurantSearchResultCallback() {
                    @Override
                    public void onRestaurantSearchResultLoaded(RestaurantSearchResult restaurantSearchResult) {
                        RestaurantListPresenter.this.pageState = pageState;

                        restaurantSearchResult.getRest().ifPresent(restList -> {
                            List<RestaurantThumbnail> restaurantThumbnailList = RestaurantThumbnail.createRestaurantThumbnailList(restList);
                            for (RestaurantThumbnail thumbnail : restaurantThumbnailList) {
                                view.addRecyclerViewItem(thumbnail);
                            }
                        });
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                }
        );
    }

    private void saveRestaurantDetailByRestaurantThumbnail(List<RestaurantThumbnail> restaurantThumbnailList) {
        List<RestaurantId> favoriteRestaurantIdList = new ArrayList<>();
        for (RestaurantThumbnail thumbnail : restaurantThumbnailList) {
            if (thumbnail.isFavorite()) {
                Log.d("log", "saveRestaurantDetailByRestaurantThumbnail: " + thumbnail.getName());
                favoriteRestaurantIdList.add(thumbnail.getId());
            }
        }

        this.restaurantDetailsRepository.getRestaurantDetails(favoriteRestaurantIdList, new RestaurantDetailsDataSource.LoadRestaurantDetailsCallback() {
            @Override
            public void onRestaurantDetailsLoaded(List<RestaurantDetail> favoritedRestaurantList) {
                for (RestaurantDetail favoritedRestaurant : favoritedRestaurantList) {
                    Log.d("log", "onRestaurantDetailsLoaded: " + favoritedRestaurant.getName());
                    favoritedRestaurant.addToFavorities();
                    restaurantDetailsRepository.saveRestaurantDetail(favoritedRestaurant);
                }
            }

            @Override
            public void onDataNotAvailable() {
                Log.d("log", "onDataNotAvailable: ");
            }
        });

        System.out.println("saveResDet: finished");
    }

    private void deleteRestaurantDetailByRestaurantThumbnail(List<RestaurantThumbnail> restaurantThumbnailList) {

    }
}