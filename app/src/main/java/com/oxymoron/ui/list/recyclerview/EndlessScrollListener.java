package com.oxymoron.ui.list.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private final LinearLayoutManager linearLayoutManager;

    private final int visibleThreshold;
    private final int startingPageIndex;

    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = true;

    protected EndlessScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;

        this.visibleThreshold = 5;
        this.startingPageIndex = 0;
    }

    public EndlessScrollListener(int visibleThreshold, LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;

        this.visibleThreshold = visibleThreshold;
        this.startingPageIndex = 0;
    }

    public EndlessScrollListener(int visibleThreshold, int startPage, LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;

        this.startingPageIndex = startPage;
        this.visibleThreshold = visibleThreshold;
        this.currentPage = startPage;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int firstVisibleItem;
        int totalItemCount;
        int visibleItemCount;

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = this.linearLayoutManager.getItemCount();
        firstVisibleItem = this.linearLayoutManager.findFirstVisibleItemPosition();

        if (totalItemCount < this.previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) this.loading = true;
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            this.currentPage += 1;
            this.previousTotalItemCount = totalItemCount;
            this.loading = false;
        }

        if (!loading && totalItemCount - (firstVisibleItem + visibleItemCount) <= this.visibleThreshold) {
            loading = onLoadMore(this.currentPage + 1, totalItemCount);
        }
    }

    public abstract boolean onLoadMore(int page, int totalItemsCount);
}