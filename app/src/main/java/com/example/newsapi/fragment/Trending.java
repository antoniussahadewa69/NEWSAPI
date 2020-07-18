package com.example.newsapi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapi.R;
import com.example.newsapi.activity.TechnologyNewsActivity;
import com.example.newsapi.activity.WebviewNewsActivity;
import com.example.newsapi.adapter.NewsAdapter;
import com.example.newsapi.core.CoreApp;
import com.example.newsapi.core.PaginationScrollListener;
import com.example.newsapi.core.RecyclerItemListener;
import com.example.newsapi.model.News;
import com.example.newsapi.model.NewsResources;
import com.example.newsapi.rest.ApiClient;
import com.example.newsapi.rest.ApiResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.newsapi.model.Contants.API_KEY;
import static com.example.newsapi.model.Contants.CATEGORY_TECHNOLOGY;
import static com.example.newsapi.model.Contants.COUNTRY;

/**
 * Created by antoniuskrisnasahadewa on 17/06/2020.
 */

public class Trending extends Fragment {
    private Context mContext;
    @BindView(R.id.rvTren)
    RecyclerView rvTren;
    @BindView(R.id.progressTren)
    ProgressBar progressTren;
    NewsAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    List<News> results;
    private ApiResponse apiResponse;

    public Trending() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trending, container, false);
        ButterKnife.bind(this, view);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mContext = getContext();
        adapter = new NewsAdapter(mContext);
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvTren.setLayoutManager(linearLayoutManager);
        rvTren.setItemAnimator(new DefaultItemAnimator());
        rvTren.setAdapter(adapter);
        rvTren.addOnItemTouchListener(new RecyclerItemListener(mContext, rvTren, new RecyclerItemListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                News news = adapter.getItem(position);
                Intent title_Intent = new Intent(mContext, WebviewNewsActivity.class);
                title_Intent.putExtra("url", news.getUrl());
                title_Intent.putExtra("sourceId",news.getSource().getId());
                title_Intent.putExtra("sourceName",news.getSource().getName());
                title_Intent.putExtra("author",news.getAuthor());
                title_Intent.putExtra("title",news.getTitle());
                title_Intent.putExtra("description",news.getDescription());
                title_Intent.putExtra("urltoimage",news.getUrlToImage());
                title_Intent.putExtra("content",news.getContent());
                title_Intent.putExtra("publishedAt",news.getPublishedAt());
                startActivity(title_Intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        rvTren.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        loadFirstPage();
        return view;
    }

    public int round(double d){
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if(result<=0.1){
            return d<0 ? -i : i;
        }else{
            return d<0 ? -(i+1) : i+1;
        }
    }

    private void loadFirstPage() {
        if (CoreApp.checkConnection(mContext)) {
            ApiResponse request = ApiClient.getApiService();

            Call<NewsResources> call = request.getNewsTrending(COUNTRY, API_KEY,10,currentPage);
            call.enqueue(new Callback<NewsResources>() {

                @Override
                public void onResponse(Call<NewsResources> call, Response<NewsResources> response) {

                    if (response.isSuccessful() && response.body().getArticles() != null) {
                        results = fetchResults(response);
                        progressTren.setVisibility(View.GONE);
                        adapter.addAll(results);
                        adapter.addLoadingFooter();

                        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                }

                @Override
                public void onFailure(Call<NewsResources> call, Throwable t) {
                    progressTren.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(mContext, "Internet connection not Available", Toast.LENGTH_SHORT).show();
            progressTren.setVisibility(View.GONE);
        }
    }

    private List<News> fetchResults(Response<NewsResources> response) {
        NewsResources technologiNews = response.body();
        return technologiNews.getArticles();
    }

    private void loadNextPage() {
        if (CoreApp.checkConnection(mContext)) {
            ApiResponse request = ApiClient.getApiService();

            Call<NewsResources> call = request.getNewsTrending(COUNTRY, API_KEY,10,currentPage);
            call.enqueue(new Callback<NewsResources>() {

                @Override
                public void onResponse(Call<NewsResources> call, Response<NewsResources> response) {

                    if (response.isSuccessful() && response.body().getArticles() != null) {
                        adapter.removeLoadingFooter();
                        isLoading = false;
                        results = fetchResults(response);
                        adapter.addAll(results);

                        if (currentPage <= TOTAL_PAGES){
                            adapter.addLoadingFooter();
                        } else {
                            progressTren.setVisibility(View.GONE);
                            isLastPage = true;
                        }
                    }
                }

                @Override
                public void onFailure(Call<NewsResources> call, Throwable t) {
                    progressTren.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(mContext, "Internet connection not Available", Toast.LENGTH_SHORT).show();
            progressTren.setVisibility(View.GONE);
        }
    }
}
