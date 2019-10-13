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
    public void refreshSavedItem(List<RestaurantThumbnail> itemList) {
        this.saveRestaurantDetailByRestaurantThumbnail(itemList);
        this.deleteRestaurantDetailByRestaurantThumbnail(itemList);
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
    public void setItem(List<RestaurantThumbnail> itemList, RestaurantThumbnail item) {
        if (itemList != null && !itemList.contains(item)) {
            this.restaurantDetailsRepository.getRestaurantDetails(new RestaurantDetailsDataSource.LoadRestaurantDetailsCallback() {
                @Override
                public void onRestaurantDetailsLoaded(List<RestaurantDetail> restaurantDetailList) {
                    for (RestaurantDetail restaurantDetail : restaurantDetailList) {
                        if (item.getId().equals(restaurantDetail.getId()) && restaurantDetail.isFavorite()) {
                            item.addToFavorities();
                        }
                    }
                }

                @Override
                public void onDataNotAvailable() {

                }
            });

            itemList.add(item);
        }
    }

    @Override
    public void removeItem(List<RestaurantThumbnail> itemList, int position) {
        try {
            if (itemList != null) {
                itemList.remove(position);
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("RestaurantListPresenter", "removeItem: " + e);
        }
    }

    @Override
    public void cleanItem(List<RestaurantThumbnail> itemList) {
        while (itemList != null && itemList.size() != 0)
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