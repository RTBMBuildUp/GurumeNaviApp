package com.oxymoron.ui.list.recyclerview.onclicklistener;

import com.oxymoron.data.RestaurantThumbnail;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public abstract class OnClickSafelyListener {
    private final PublishSubject<RestaurantThumbnail> publishSubject = PublishSubject.create();

    public OnClickSafelyListener() {
        final Disposable subscribe = publishSubject
                .throttleFirst(1500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onClicked);
    }

    public void onClick(RestaurantThumbnail restaurantThumbnail) {
        publishSubject.onNext(restaurantThumbnail);
    }

    public abstract void onClicked(RestaurantThumbnail favorite);
}
