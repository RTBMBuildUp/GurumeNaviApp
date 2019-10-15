package com.oxymoron.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.gurumenaviapp.R;
import com.oxymoron.data.RestaurantDetail;
import com.oxymoron.data.source.local.data.RestaurantId;
import com.oxymoron.injection.Injection;

public class RestaurantDetailActivity extends AppCompatActivity implements RestaurantDetailContract.View {
    private final static String KEY_RESTAURANT_ID = "KEY_RESTAURANT_ID";

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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        findViews();

        presenter = new RestaurantDetailPresenter(this, Injection.provideRestaurantDetailsRepository(this));

        final Intent intent = getIntent();
        final RestaurantId restaurantId = ((RestaurantId) intent.getSerializableExtra(KEY_RESTAURANT_ID));

        presenter.searchDetail(restaurantId);
    }

    public static Intent createIntent(Context packageContext, RestaurantId restaurantId) {
        final Intent intent = new Intent(packageContext, RestaurantDetailActivity.class);

        return intent.putExtra(KEY_RESTAURANT_ID, restaurantId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(RestaurantDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showDetail(RestaurantDetail restaurantDetail) {
        final String notFound = getResources().getString(R.string.not_found);
        final Bitmap notFoundImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_not_found);

        this.name.setText(restaurantDetail.getName());

        if (restaurantDetail.getPhoneNumber() != null) {
            this.tel.setText(restaurantDetail.getPhoneNumber().getPhoneNumber().getOrElse(notFound));
        }

        this.address.setText(restaurantDetail.getAddress());

        if (restaurantDetail.getAccess() != null) {
            this.access.setText(restaurantDetail.getAccess());
        }

        this.openTime.setText(restaurantDetail.getOpenTime());

        restaurantDetail.getImageUrl().getUrl().ifPresentOrElse(
                this::setImageView,
                () -> this.imageView.setImageBitmap(notFoundImage)
        );
    }

    private void findViews() {
        this.name = findViewById(R.id.restaurant_detail_name);
        this.tel = findViewById(R.id.restaurant_detail_telephone);
        this.address = findViewById(R.id.restaurant_detail_address);
        this.access = findViewById(R.id.restaurant_detail_access);
        this.openTime = findViewById(R.id.restaurant_detail_open_time);

        this.imageView = findViewById(R.id.restaurant_item_thumbnail);
    }

    private void setImageView(String imageUrl) {
        Glide.with(this).load(imageUrl).into(imageView);
    }
}
