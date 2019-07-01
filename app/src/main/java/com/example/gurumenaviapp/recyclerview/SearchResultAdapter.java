package com.example.gurumenaviapp.recyclerview;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.gurumenaviapp.R;
import com.example.gurumenaviapp.data.ShowedInformation;
import com.example.gurumenaviapp.gson.data.Access;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {
    private List<ShowedInformation> showedInformationList;

    private View view;

    public SearchResultAdapter(List<ShowedInformation> showedInformationList) {
        this.showedInformationList = showedInformationList;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.search_result, viewGroup, false);

        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder searchResultViewHolder, int position) {
        final Resources resources = view.getResources();
        final String notFound = resources.getString(R.string.notFound);

        final ShowedInformation result = showedInformationList.get(position);
        final Access access = result.getAccess();

        searchResultViewHolder.name.setText(
                getOrElse(result.getName(), notFound)
        );

        searchResultViewHolder.address.setText(
                getOrElse(result.getAddress(), notFound)
        );

        searchResultViewHolder.tel.setText(
                getOrElse(result.getTel(), notFound)
        );

        searchResultViewHolder.access.setText(
                getOrElse(access.getLine() + access.getStation() + access.getWalk() + "分", notFound)
        );

        searchResultViewHolder.imageView.setImageBitmap(
                result.getImage()
        );
    }

    @Override
    public int getItemCount() {
        return showedInformationList.size();
    }

    private String getOrElse(String defaultValue, String option) {
        return defaultValue == null ? option : defaultValue;
    }
}
