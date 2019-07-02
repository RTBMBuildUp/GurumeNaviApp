package com.example.gurumenaviapp.search.result;

import com.example.gurumenaviapp.BasePresenter;
import com.example.gurumenaviapp.BaseView;
import com.example.gurumenaviapp.data.ShowedInformation;
import com.example.gurumenaviapp.data.request.Request;

import java.util.List;

public interface SearchResultContract {
    interface View extends BaseView<Presenter> {
        void addRecyclerViewItem(ShowedInformation item);

        void removeRecyclerViewItem(int position);

        void cleanRecyclerViewItem();
    }

    interface Presenter extends BasePresenter {
        void setItem(List<ShowedInformation> itemList, ShowedInformation item);

        void removeItem(List<ShowedInformation> itemList, int position);

        void cleanItem(List<ShowedInformation> itemList);

        void searchWithRequest(String token, List<Request> requestList);
    }
}
