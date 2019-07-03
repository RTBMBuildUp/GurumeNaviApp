package com.example.gurumenaviapp.search.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gurumenaviapp.R;
import com.example.gurumenaviapp.data.request.Requests;
import com.example.gurumenaviapp.search.detail.data.RestaurantDetail;
import com.example.gurumenaviapp.util.Optional;

public class RestaurantDetailActivity extends AppCompatActivity implements RestaurantDetailContract.View {
    private RestaurantDetailContract.Presenter presenter;

    private TextView name;
    private TextView tel;
    private TextView address;
    private TextView access;
    private TextView openTime;

    private ImageView imageView;

    private String restaurantId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_detail);
        findViews();

        presenter = new RestaurantDetailPresenter(this, this);

        Intent intent = getIntent();
        this.restaurantId = intent.getStringExtra(Requests.id.toString());

        presenter.searchDetail(this.restaurantId);
    }

    @Override
    public void setPresenter(RestaurantDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setDetail(RestaurantDetail detail) {
        final String notFound = getResources().getString(R.string.not_found);

        this.name.setText(Optional.of(detail.getName()).getOrElse(notFound));
        this.tel.setText(Optional.of(detail.getTel()).getOrElse(notFound));
        this.address.setText(Optional.of(detail.getAddress()).getOrElse(notFound));
        this.access.setText(Optional.of(detail.getAccess().showUserAround()).getOrElse(notFound));
        this.openTime.setText(Optional.of(detail.getOpenTime()).getOrElse(notFound));

        this.imageView.setImageBitmap(detail.getImage());
    }

    private void findViews() {
        this.name = findViewById(R.id.restaurant_detail_name);
        this.tel = findViewById(R.id.restaurant_detail_telphone);
        this.address = findViewById(R.id.restaurant_detail_address);
        this.access = findViewById(R.id.restaurant_detail_access);
        this.openTime = findViewById(R.id.restaurant_detail_open_time);

        this.imageView = findViewById(R.id.restaurant_item_image);
    }
}
