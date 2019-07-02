package com.example.gurumenaviapp.search.result;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.gurumenaviapp.R;
import com.example.gurumenaviapp.search.result.data.ShowedInformation;
import com.example.gurumenaviapp.data.request.Request;
import com.example.gurumenaviapp.data.request.Requests;
import com.example.gurumenaviapp.recyclerview.SearchResultAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.gurumenaviapp.data.request.Requests.keyid;

public class SearchResultActivity extends AppCompatActivity implements SearchResultContract.View {
    private SearchResultContract.Presenter presenter;

    private RecyclerView recyclerView;
    private SearchResultAdapter adapter;
    private List<ShowedInformation> itemList = new ArrayList<>();

    private List<Request> requestList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        findViews();

        System.out.println("stat");

        requestList = initialRequestList();

        presenter = new SearchResultPresenter(this, this);
        adapter = new SearchResultAdapter(itemList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        String token = getIntent().getStringExtra(keyid.toString());

        presenter.searchWithRequest(token, requestList);
    }

    @Override
    public void setPresenter(SearchResultContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void addRecyclerViewItem(ShowedInformation item) {
        presenter.setItem(itemList, item);
        adapter.notifyItemInserted(0);
    }

    @Override
    public void removeRecyclerViewItem(int position) {
        presenter.removeItem(itemList, position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void cleanRecyclerViewItem() {
        presenter.cleanItem(itemList);
    }

    private List<Request> initialRequestList() {
        List<Request> initialList = new ArrayList<>();

        final Intent intent = getIntent();

        for (Requests request : Requests.values()) {
            final String value = intent.getStringExtra(request.toString());

            if (value != null) {
                initialList.add(new Request(request, value));
            }
        }

        return initialList;
    }

    private void findViews() {
        this.recyclerView = findViewById(R.id.search_result_recyclerview);
    }
}
