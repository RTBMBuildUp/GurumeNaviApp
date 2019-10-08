package com.oxymoron.data.source;

import androidx.annotation.NonNull;

import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.room.RestaurantId;
import com.oxymoron.data.source.remote.api.PageState;
import com.oxymoron.data.source.remote.api.gson.data.RestaurantSearchResult;
import com.oxymoron.data.source.remote.api.serializable.LocationInformation;
import com.oxymoron.data.source.remote.api.serializable.Range;
import com.oxymoron.util.Optional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RestaurantDetailsRepository implements RestaurantDetailsDataSource {
    private static RestaurantDetailsRepository INSTANCE;

    private final RestaurantDetailsDataSource restaurantDetailsLocalDataSource;
    private final RestaurantDetailsDataSource restaurantDetailsRemoteDataSource;

    private Map<RestaurantId, RestaurantDetail> cachedRestaurantDetailMap;
    private boolean cacheIsDirty = false;

    public static RestaurantDetailsRepository getInstance(RestaurantDetailsDataSource restaurantDetailsLocalDataSource,
                                                          RestaurantDetailsDataSource restaurantDetailsRemoteDataSource) {

        if (INSTANCE == null) {
            INSTANCE = new RestaurantDetailsRepository(
                    restaurantDetailsLocalDataSource,
                    restaurantDetailsRemoteDataSource
            );
        }

        return INSTANCE;
    }

    private RestaurantDetailsRepository(RestaurantDetailsDataSource restaurantDetailsLocalDataSource,
                                        RestaurantDetailsDataSource restaurantDetailsRemoteDataSource) {

        this.restaurantDetailsLocalDataSource = restaurantDetailsLocalDataSource;
        this.restaurantDetailsRemoteDataSource = restaurantDetailsRemoteDataSource;
    }

    @Override
    public void getRestaurantDetails(@NonNull LoadRestaurantDetailsCallback callback) {
        if (this.cachedRestaurantDetailMap != null && !this.cacheIsDirty) {
            callback.onRestaurantDetailsLoaded(new ArrayList<>(this.cachedRestaurantDetailMap.values()));

            return;
        }

        if (this.cacheIsDirty) {
            this.getRestaurantDetailsFromRemote(callback);
        } else {
            this.restaurantDetailsLocalDataSource.getRestaurantDetails(new LoadRestaurantDetailsCallback() {
                @Override
                public void onRestaurantDetailsLoaded(List<RestaurantDetail> restaurantDetailList) {
                    refreshCache(restaurantDetailList);
                    callback.onRestaurantDetailsLoaded(restaurantDetailList);
                }

                @Override
                public void onDataNotAvailable() {
                    getRestaurantDetailsFromRemote(callback);
                }
            });
        }
    }

    @Override
    public void getRestaurantDetails(@NonNull List<RestaurantId> restaurantIdList, @NonNull LoadRestaurantDetailsCallback callback) {
        if (this.cachedRestaurantDetailMap != null && !this.cacheIsDirty) {
            List<RestaurantDetail> restaurantDetailList = new ArrayList<>();

            for (RestaurantId restaurantId : restaurantIdList) {
                RestaurantDetail restaurantDetail = this.cachedRestaurantDetailMap.get(restaurantId);

                if (restaurantDetail != null) {
                    restaurantDetailList.add(restaurantDetail);
                }
            }

            if (restaurantDetailList.size() == restaurantIdList.size()) {
                callback.onRestaurantDetailsLoaded(restaurantDetailList);
                return;
            }
        }

        if (this.cacheIsDirty) {
            this.getRestaurantDetailsFromRemote(restaurantIdList, callback);
        } else {
            this.restaurantDetailsLocalDataSource.getRestaurantDetails(restaurantIdList, new LoadRestaurantDetailsCallback() {
                @Override
                public void onRestaurantDetailsLoaded(List<RestaurantDetail> restaurantDetailList) {
                    refreshCache(restaurantDetailList);
                    callback.onRestaurantDetailsLoaded(restaurantDetailList);
                }

                @Override
                public void onDataNotAvailable() {
                    getRestaurantDetailsFromRemote(restaurantIdList, callback);
                }
            });
        }
    }

    @Override
    public void getRestaurantDetails(@NonNull Range range, @NonNull LocationInformation locationInformation,
                                     @NonNull GetRestaurantSearchResultCallback callback) {

        if (this.cacheIsDirty) {
            this.restaurantDetailsRemoteDataSource.getRestaurantDetails(range, locationInformation, callback);
        } else {
            this.restaurantDetailsLocalDataSource.getRestaurantDetails(range, locationInformation, new GetRestaurantSearchResultCallback() {
                @Override
                public void onRestaurantSearchResultLoaded(RestaurantSearchResult restaurantSearchResult) {
                    callback.onRestaurantSearchResultLoaded(restaurantSearchResult);
                }

                @Override
                public void onDataNotAvailable() {
                    restaurantDetailsRemoteDataSource.getRestaurantDetails(range, locationInformation, callback);
                }
            });
        }
    }

    @Override
    public void getRestaurantDetails(@NonNull Range range, @NonNull LocationInformation locationInformation,
                                     @NonNull PageState pageState, @NonNull GetRestaurantSearchResultCallback callback) {

        if (this.cacheIsDirty) {
            this.restaurantDetailsRemoteDataSource.getRestaurantDetails(range, locationInformation, pageState, callback);
        } else {
            this.restaurantDetailsLocalDataSource.getRestaurantDetails(range, locationInformation, pageState, new GetRestaurantSearchResultCallback() {
                @Override
                public void onRestaurantSearchResultLoaded(RestaurantSearchResult restaurantSearchResult) {
                    callback.onRestaurantSearchResultLoaded(restaurantSearchResult);
                }

                @Override
                public void onDataNotAvailable() {
                    restaurantDetailsRemoteDataSource.getRestaurantDetails(range, locationInformation, pageState, callback);
                }
            });
        }
    }

    @Override
    public void getRestaurantDetail(@NonNull RestaurantId id, @NonNull GetRestaurantDetailsCallback callback) {
        RestaurantDetail restaurantDetailCache = this.cachedRestaurantDetailMap.get(id);
        if (restaurantDetailCache != null) {
            callback.onRestaurantDetailLoaded(restaurantDetailCache);
            return;
        }

        this.getRestaurantDetailById(id).ifPresentOrElse(
                callback::onRestaurantDetailLoaded,
                () -> this.restaurantDetailsLocalDataSource.getRestaurantDetail(id, new GetRestaurantDetailsCallback() {
                    @Override
                    public void onRestaurantDetailLoaded(RestaurantDetail restaurantDetail) {
                        if (cachedRestaurantDetailMap == null) {
                            cachedRestaurantDetailMap = new LinkedHashMap<>();
                        }
                        cachedRestaurantDetailMap.put(restaurantDetail.getId(), restaurantDetail);

                        callback.onRestaurantDetailLoaded(restaurantDetail);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        restaurantDetailsRemoteDataSource.getRestaurantDetail(id, new GetRestaurantDetailsCallback() {
                            @Override
                            public void onRestaurantDetailLoaded(RestaurantDetail restaurantDetail) {
                                if (cachedRestaurantDetailMap == null) {
                                    cachedRestaurantDetailMap = new LinkedHashMap<>();
                                }
                                cachedRestaurantDetailMap.put(restaurantDetail.getId(), restaurantDetail);

                                callback.onRestaurantDetailLoaded(restaurantDetail);
                            }

                            @Override
                            public void onDataNotAvailable() {
                                callback.onDataNotAvailable();
                            }
                        });
                    }
                }));
    }

    @Override
    public void saveRestaurantDetail(@NonNull RestaurantDetail restaurantDetail) {
        restaurantDetail.addToFavorities();

        this.restaurantDetailsLocalDataSource.saveRestaurantDetail(restaurantDetail);
        this.restaurantDetailsRemoteDataSource.saveRestaurantDetail(restaurantDetail);

        if (this.cachedRestaurantDetailMap == null) {
            this.cachedRestaurantDetailMap = new LinkedHashMap<>();
        }
        this.cachedRestaurantDetailMap.put(restaurantDetail.getId(), restaurantDetail);
    }

    @Override
    public void deleteRestaurantDetail(@NonNull RestaurantId id) {
        this.restaurantDetailsLocalDataSource.deleteRestaurantDetail(id);
        this.restaurantDetailsRemoteDataSource.deleteRestaurantDetail(id);

        if (this.cachedRestaurantDetailMap != null) {
            this.cachedRestaurantDetailMap.remove(id);
        }
    }

    @Override
    public void deleteAllRestaurantDetail() {
        this.restaurantDetailsLocalDataSource.deleteAllRestaurantDetail();
        this.restaurantDetailsRemoteDataSource.deleteAllRestaurantDetail();

        if (this.cachedRestaurantDetailMap == null) {
            this.cachedRestaurantDetailMap = new LinkedHashMap<>();
        }
        this.cachedRestaurantDetailMap.clear();
    }

    private void getRestaurantDetailsFromRemote(LoadRestaurantDetailsCallback callback) {
        this.restaurantDetailsRemoteDataSource.getRestaurantDetails(
                new LoadRestaurantDetailsCallback() {
                    @Override
                    public void onRestaurantDetailsLoaded(List<RestaurantDetail> restaurantDetailList) {
                        callback.onRestaurantDetailsLoaded(restaurantDetailList);
                        refreshLocalDataSource(restaurantDetailList);
                        refreshCache(restaurantDetailList);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
    }

    private void getRestaurantDetailsFromRemote(List<RestaurantId> restaurantIdList, LoadRestaurantDetailsCallback callback) {
        this.restaurantDetailsRemoteDataSource.getRestaurantDetails(
                restaurantIdList,
                new LoadRestaurantDetailsCallback() {
                    @Override
                    public void onRestaurantDetailsLoaded(List<RestaurantDetail> restaurantDetailList) {
                        callback.onRestaurantDetailsLoaded(restaurantDetailList);
                        refreshLocalDataSource(restaurantDetailList);
                        refreshCache(restaurantDetailList);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                }
        );
    }

    private void refreshCache(List<RestaurantDetail> restaurantDetailList) {
        if (this.cachedRestaurantDetailMap == null) {
            this.cachedRestaurantDetailMap = new LinkedHashMap<>();
        }
        this.cachedRestaurantDetailMap.clear();

        for (RestaurantDetail restaurantDetail : restaurantDetailList) {
            this.cachedRestaurantDetailMap.put(restaurantDetail.getId(), restaurantDetail);
        }
        this.cacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<RestaurantDetail> restaurantDetailList) {
        this.restaurantDetailsLocalDataSource.deleteAllRestaurantDetail();
        for (RestaurantDetail restaurantDetail : restaurantDetailList) {
            this.restaurantDetailsLocalDataSource.saveRestaurantDetail(restaurantDetail);
        }
    }

    private Optional<RestaurantDetail> getRestaurantDetailById(RestaurantId id) {
        if (this.cachedRestaurantDetailMap == null || this.cachedRestaurantDetailMap.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(this.cachedRestaurantDetailMap.get(id));
        }
    }
}
