package com.oxymoron.ui.list;

import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gurumenaviapp.R;
import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.source.RestaurantDetailsDataSource;
import com.oxymoron.data.source.RestaurantDetailsRepository;
import com.oxymoron.data.source.local.data.RestaurantId;
import com.oxymoron.data.source.remote.api.PageState;
import com.oxymoron.data.source.remote.api.gson.data.RestaurantSearchResult;
import com.oxymoron.data.source.remote.api.serializable.LocationInformation;
import com.oxymoron.data.source.remote.api.serializable.Range;
import com.oxymoron.ui.list.data.RestaurantThumbnail;

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
    public void saveRestaurantDetails(List<RestaurantThumbnail> restaurantThumbnailList) {
        for (RestaurantThumbnail restaurantThumbnail : restaurantThumbnailList) {
            this.restaurantDetailsRepository.getRestaurantDetail(restaurantThumbnail.getId(), new RestaurantDetailsDataSource.GetRestaurantDetailsCallback() {
                @Override
                public void onRestaurantDetailLoaded(RestaurantDetail restaurantDetail) {
                    if (restaurantThumbnail.isFavorite()) {
                        restaurantDetail.addToFavorities();
                    } else {
                        restaurantDetail.removeFromFavorities();
                    }

                    restaurantDetailsRepository.saveRestaurantDetail(restaurantDetail);
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        }
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
    public void onClickItem(RestaurantThumbnail restaurantThumbnail) {
        final RestaurantId restaurantId = restaurantThumbnail.getId();

        this.view.startRestaurantDetailActivity(restaurantId);
    }

    @Override
    public void onClickFavoriteIcon(ImageView favoriteIcon, RestaurantThumbnail restaurantThumbnail, Animation animation) {
        final int favoriteBorder = R.drawable.ic_favorite_border_gray_24dp;
        final int favorite = R.drawable.ic_favorite_pink_24dp;

        if (restaurantThumbnail.isFavorite()) {
            restaurantThumbnail.removeFromFavorities();

            favoriteIcon.setImageResource(favoriteBorder);
        } else {
            restaurantThumbnail.addToFavorities();

            favoriteIcon.setImageResource(favorite);
            favoriteIcon.startAnimation(animation);
        }
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount, LocationInformation locationInformation) {
        if (locationInformation != null) {
            this.search(new Range(2), locationInformation, new PageState(page));
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
}