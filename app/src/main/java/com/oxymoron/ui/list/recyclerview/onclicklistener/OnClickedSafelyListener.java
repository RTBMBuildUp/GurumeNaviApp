package com.oxymoron.ui.list.recyclerview.onclicklistener;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public abstract class OnClickedSafelyListener<T> implements OnClickedListener<T> {
    private final PublishSubject<T> publishSubject = PublishSubject.create();

    protected OnClickedSafelyListener() {
        final Disposable subscribe = publishSubject
                .throttleFirst(1500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onClicked);
    }

    public void onClick(T value) {
        publishSubject.onNext(value);

    }

    @Override
    public abstract void onClicked(T value);
}
