package com.oxymoron.search.candidate.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import com.oxymoron.request.RequestIds;
import com.oxymoron.search.candidate.data.RestaurantThumbnail;
import com.oxymoron.search.detail.RestaurantDetailActivity;

import java.util.List;

public class RecyclerViewPresenter implements RecyclerViewContract.Presenter {
    private Context context;
    private RecyclerViewContract.View view;

    public RecyclerViewPresenter(Context context, RecyclerViewContract.View view) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void onClick(RecyclerView.ViewHolder viewHolder, List<RestaurantThumbnail> thumbnailList) {
        int position = viewHolder.getAdapterPosition();
        RestaurantThumbnail restaurantThumbnail = thumbnailList.get(position);

        Intent intent = new Intent(context, RestaurantDetailActivity.class);
        intent.putExtra(RequestIds.restaurant_id.toString(), restaurantThumbnail.getRestaurantId());
        view.startActivity(intent);
    }

    @Override
    public void start() {

    }
}
