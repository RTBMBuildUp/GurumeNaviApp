package com.oxymoron.data;

import androidx.annotation.Nullable;

import com.oxymoron.util.Consumer;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class Favorite {
    private boolean favorite;
    private final PublishSubject<Boolean> publishSubject;
    private final Observable<Boolean> booleanObservable;

    Favorite(Boolean favorite) {
        this.favorite = favorite;
        this.publishSubject = PublishSubject.create();
        this.booleanObservable = this.publishSubject.observeOn(AndroidSchedulers.mainThread());
    }

    void addToFavorites() {
        this.setFavorite(true);
    }

    void removeFromFavorites() {
        this.setFavorite(false);
    }

    void switchFavorites() {
        this.setFavorite(!this.favorite);
    }

    Boolean isFavorite() {
        return this.favorite;
    }

    void setOnUpdate(Consumer<Boolean> onUpdate) {
        final Disposable subscribe = this.booleanObservable.subscribe(onUpdate::accept);
    }

    private void setFavorite(Boolean favorite) {
        this.favorite = favorite;
        this.publishSubject.onNext(this.favorite);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Favorite)) return false;
        final Favorite favorite = ((Favorite) obj);

        return favorite.isFavorite() == this.isFavorite();
    }

}
