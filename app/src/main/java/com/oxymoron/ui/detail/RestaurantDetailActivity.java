package com.oxymoron.ui.detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gurumenaviapp.R;
import com.oxymoron.api.GurumeNaviApiClientImpl;
import com.oxymoron.api.gson.data.Access;
import com.oxymoron.request.RequestIds;
import com.oxymoron.ui.detail.data.RestaurantDetail;
import com.oxymoron.util.Function;

public class RestaurantDetailActivity extends AppCompatActivity implements RestaurantDetailContract.View {
    private RestaurantDetailContract.Presenter presenter;

    private TextView name;
    private TextView tel;
    private TextView address;
    private TextView access;
    private TextView openTime;

    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_detail);
        findViews();

        presenter = new RestaurantDetailPresenter(this, GurumeNaviApiClientImpl.getInstance());

        Intent intent = getIntent();
        String restaurantId = intent.getStringExtra(RequestIds.restaurant_id.toString());

        presenter.searchDetail(restaurantId);
    }

    @Override
    public void setPresenter(RestaurantDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showDetail(RestaurantDetail detail) {
        final String notFound = getResources().getString(R.string.not_found);
        final Bitmap notFoundImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_not_found);

        this.name.setText(detail.getName());
        this.tel.setText(detail.getTel().getOrElse(notFound));
        this.address.setText(detail.getAddress());

        this.access.setText(detail.getAccess().map(new Function<Access, String>() {
            @Override
            public String apply(Access value) {
                return value.showUserAround();
            }
        }).getOrElse(notFound));

        this.openTime.setText(detail.getOpenTime().getOrElse(notFound));

        if (detail.getImageUrl().isPresent()) {
            this.setImageView(detail.getImageUrl().get());
        } else {
            this.imageView.setImageBitmap(notFoundImage);
        }
    }

    private void findViews() {
        this.name = findViewById(R.id.restaurant_detail_name);
        this.tel = findViewById(R.id.restaurant_detail_telephone);
        this.address = findViewById(R.id.restaurant_detail_address);
        this.access = findViewById(R.id.restaurant_detail_access);
        this.openTime = findViewById(R.id.restaurant_detail_open_time);

        this.imageView = findViewById(R.id.restaurant_item_image);
    }

    private void setImageView(String imageUrl) {
        Glide.with(this).load(imageUrl).into(imageView);
    }
}
