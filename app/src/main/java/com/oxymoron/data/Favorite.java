package com.oxymoron.data;

import androidx.annotation.Nullable;

import com.oxymoron.util.Consumer;

public class Favorite {
    private boolean favorite;
    private Consumer<Boolean> onUpdate;

    public Favorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public void addToFavorites() {
        this.setFavorite(true);
    }

    public void removeFromFavorites() {
        this.setFavorite(false);
    }

    public void switchFavorites() {
        this.setFavorite(!this.favorite);
    }

    public Boolean isFavorite() {
        return this.favorite;
    }

    public void setOnUpdate(Consumer<Boolean> onUpdate) {
        this.onUpdate = onUpdate;
    }

    private void setFavorite(Boolean favorite) {
        this.favorite = favorite;
        if (this.onUpdate != null) this.onUpdate.accept(this.favorite);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Favorite)) return false;
        final Favorite favorite = ((Favorite) obj);

        return favorite.isFavorite() == this.isFavorite();
    }

}
