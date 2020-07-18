package com.example.newsapi.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.newsapi.R;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.example.newsapi.model.Contants.API_KEY;
import static com.example.newsapi.model.Contants.CATEGORY_TECHNOLOGY;
import static com.example.newsapi.model.Contants.COUNTRY;

public class TechnologyNewsActivity extends AppCompatActivity {

    private Context mContext;
    @BindView(R.id.rvTech)
    RecyclerView rvTech;
    @BindView(R.id.progressTech)
    ProgressBar progressTech;
    NewsAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 8;
    private int currentPage = PAGE_START;
    List<News> results;
    private ApiResponse apiResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technology);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("TECHNOLOGY NEWS");
        adapter = new NewsAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTech.setLayoutManager(linearLayoutManager);
        rvTech.setItemAnimator(new DefaultItemAnimator());
        rvTech.setAdapter(adapter);
        mContext = this;

        rvTech.addOnItemTouchListener(new RecyclerItemListener(TechnologyNewsActivity.this, rvTech, new RecyclerItemListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                News news = adapter.getItem(position);
                Intent title_Intent = new Intent(TechnologyNewsActivity.this, WebviewNewsActivity.class);
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

        rvTech.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadFirstPage() {
        if (CoreApp.checkConnection((TechnologyNewsActivity.this))) {
            ApiResponse request = ApiClient.getApiService();

            Call<NewsResources> call = request.getNewsByCategory(COUNTRY, CATEGORY_TECHNOLOGY, API_KEY,10,currentPage);
            call.enqueue(new Callback<NewsResources>() {

                @Override
                public void onResponse(Call<NewsResources> call, Response<NewsResources> response) {

                    if (response.isSuccessful() && response.body().getArticles() != null) {
                        results = fetchResults(response);
                        progressTech.setVisibility(View.GONE);
                        adapter.addAll(results);
                        adapter.addLoadingFooter();

                        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                }

                @Override
                public void onFailure(Call<NewsResources> call, Throwable t) {
                    progressTech.setVisibility(View.GONE);
                    Toast.makeText(TechnologyNewsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(TechnologyNewsActivity.this, "Internet connection not Available", Toast.LENGTH_SHORT).show();
            progressTech.setVisibility(View.GONE);
        }
    }

    private List<News> fetchResults(Response<NewsResources> response) {
        NewsResources technologiNews = response.body();
        return technologiNews.getArticles();
    }

    private void loadNextPage() {
        if (CoreApp.checkConnection((TechnologyNewsActivity.this))) {
            ApiResponse request = ApiClient.getApiService();

            Call<NewsResources> call = request.getNewsByCategory(COUNTRY, CATEGORY_TECHNOLOGY, API_KEY,10,currentPage);
            call.enqueue(new Callback<NewsResources>() {

                @Override
                public void onResponse(Call<NewsResources> call, Response<NewsResources> response) {

                    if (response.isSuccessful() && response.body().getArticles() != null) {
                        adapter.removeLoadingFooter();
                        isLoading = false;
                        results = fetchResults(response);
                        adapter.addAll(results);

                        if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                }

                @Override
                public void onFailure(Call<NewsResources> call, Throwable t) {
                    progressTech.setVisibility(View.GONE);
                    Toast.makeText(TechnologyNewsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(TechnologyNewsActivity.this, "Internet connection not Available", Toast.LENGTH_SHORT).show();
            progressTech.setVisibility(View.GONE);
        }
    }
}
