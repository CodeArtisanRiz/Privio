package com.winkbr.browser.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.winkbr.browser.R;
import com.winkbr.browser.activity.WebViewActivity;
import com.winkbr.browser.adapter.NewsAdapter;
import com.winkbr.browser.adapter.RecyclerTouchListener;
import com.winkbr.browser.Model.News;
import com.winkbr.browser.Model.NewsResource;
import com.winkbr.browser.util.ApiClient;
import com.winkbr.browser.util.ApiResponse;
import com.winkbr.browser.util.Internet;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.winkbr.browser.Model.Contants.API_KEY;
import static com.winkbr.browser.Model.Contants.CATEGORY_TECHNOLOGY;
import static com.winkbr.browser.Model.Contants.COUNTRY;

/**
 * A simple {@link Fragment} subclass.
 */
public class TechnologyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<News> newsArrayList = new ArrayList<>();
    private NewsAdapter mAdapter;
    private RecyclerView recyclerView;

    public TechnologyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        initViews();

        return view;
    }

    private void initViews() {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorGreen,
                R.color.colorBlue,
                R.color.colorOrange);
        loadJSON();

        recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                News news = newsArrayList.get(position);
                Intent title_Intent = new Intent(getActivity(), WebViewActivity.class);
                title_Intent.putExtra("url", news.getUrl());
                startActivity(title_Intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void loadJSON() {
        swipeRefreshLayout.setRefreshing(true);

        if (Internet.checkConnection(getContext())) {
            ApiResponse request = ApiClient.getApiService();

            Call<NewsResource> call = request.getCategoryOfHeadlines(COUNTRY, CATEGORY_TECHNOLOGY, API_KEY);
            call.enqueue(new Callback<NewsResource>() {

                @Override
                public void onResponse(Call<NewsResource> call, Response<NewsResource> response) {

                    if (response.isSuccessful() && response.body().getArticles() != null) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (!newsArrayList.isEmpty()) {
                            newsArrayList.clear();
                        }

                        newsArrayList = response.body().getArticles();

                        mAdapter = new NewsAdapter(newsArrayList);
                        recyclerView.setAdapter(mAdapter);
                    }
                }

                @Override
                public void onFailure(Call<NewsResource> call, Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Internet connection not Available", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadJSON();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}