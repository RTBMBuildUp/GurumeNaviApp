package com.example.gurumenaviapp.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gurumenaviapp.R;

public class SearchResultViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView tel;
    public TextView address;
    public TextView access;

    public ImageView imageView;

    SearchResultViewHolder(View view) {
        super(view);

        name = view.findViewById(R.id.RestNameTextView);
        tel = view.findViewById(R.id.telTextView);
        address = view.findViewById(R.id.addressTextView);
        access = view.findViewById(R.id.accessTextView);

        imageView = view.findViewById(R.id.imageView);
    }
}
