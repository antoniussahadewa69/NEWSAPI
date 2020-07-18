package com.example.newsapi.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapi.R;
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
import io.realm.RealmChangeListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.newsapi.model.Contants.API_KEY;
import static com.example.newsapi.model.Contants.COUNTRY;
import static com.example.newsapi.model.Contants.SORT_PUBLISHEDAT;

/**
 * Created by antoniuskrisnasahadewa on 17/06/2020.
 */

public class Home extends Fragment {
    private Context mContext;
    @BindView(R.id.rvHome)
    RecyclerView rvhome;
    @BindView(R.id.progressHome)
    ProgressBar progresshome;
    @BindView(R.id.fabSort)
    FloatingActionButton fabSort;
    @BindView(R.id.btnSearchNews)
    Button btnSearchNews;
    @BindView(R.id.etSearchNews)
    EditText etSearchNews;
    CheckBox cbpublishedAt;
    CheckBox cbrelevancy;
    CheckBox cbpopularity;
    Button btnSortCancel;
    Button btnSortOk;
    NewsAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 8;
    private int currentPage = PAGE_START;
    List<News> results;
    String etSearch = "Indonesia";
    String sortString = "publishedAt";
    Dialog dialogSort;
    private ApiResponse apiResponse;

    public Home() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mContext = getContext();
        setDialog();
        adapter = new NewsAdapter(mContext);
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvhome.setLayoutManager(linearLayoutManager);
        rvhome.setItemAnimator(new DefaultItemAnimator());
        rvhome.setAdapter(adapter);
        rvhome.addOnItemTouchListener(new RecyclerItemListener(mContext, rvhome, new RecyclerItemListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                News news = results.get(position);
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

        fabSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortString.equals("publishedAt")){
                    cbpublishedAt.setChecked(true);
                    cbrelevancy.setChecked(false);
                    cbpopularity.setChecked(false);
                } else if (sortString.equals("relevancy")){
                    cbpublishedAt.setChecked(false);
                    cbrelevancy.setChecked(true);
                    cbpopularity.setChecked(false);
                } else if (sortString.equals("popularity")){
                    cbpublishedAt.setChecked(false);
                    cbrelevancy.setChecked(false);
                    cbpopularity.setChecked(true);
                }
                dialogSort.show();
            }
        });

        etSearchNews.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    etSearch = charSequence.toString();
                } else {
                    etSearch = "Indonesia";
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnSearchNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                loadFirstPage();
            }
        });

        rvhome.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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

    private void setDialog() {
        dialogSort = new Dialog(mContext);
        dialogSort.setContentView(R.layout.sort_picker);
        dialogSort.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSort.setTitle("Sort By:");

        cbpublishedAt = (CheckBox) dialogSort.findViewById(R.id.cbpublishedAt);
        cbrelevancy = (CheckBox) dialogSort.findViewById(R.id.cbrelevancy);
        cbpopularity = (CheckBox) dialogSort.findViewById(R.id.cbpopularity);
        btnSortCancel = (Button) dialogSort.findViewById(R.id.btnSortCancel);
        btnSortOk = (Button) dialogSort.findViewById(R.id.btnSortOK);

        if (sortString.equals("publishedAt")){
            cbpublishedAt.setChecked(true);
            cbrelevancy.setChecked(false);
            cbpopularity.setChecked(false);
        } else if (sortString.equals("relevancy")){
            cbpublishedAt.setChecked(false);
            cbrelevancy.setChecked(true);
            cbpopularity.setChecked(false);
        } else if (sortString.equals("popularity")){
            cbpublishedAt.setChecked(false);
            cbrelevancy.setChecked(false);
            cbpopularity.setChecked(true);
        }

        final String[] sortTemp = {"publishedAt"};

        cbpublishedAt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbrelevancy.setChecked(false);
                    cbpopularity.setChecked(false);
                    sortTemp[0] = "publishedAt";
                }
            }
        });
        cbrelevancy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbpublishedAt.setChecked(false);
                    cbpopularity.setChecked(false);
                    sortTemp[0] = "relevancy";
                }
            }
        });
        cbpopularity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbpublishedAt.setChecked(false);
                    cbrelevancy.setChecked(false);
                    sortTemp[0] = "popularity";
                }
            }
        });
        btnSortCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSort.dismiss();
            }
        });
        btnSortOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortString = sortTemp[0];
                adapter.clear();
                loadFirstPage();
                dialogSort.dismiss();
            }
        });
    }

    private void loadFirstPage() {
        if (CoreApp.checkConnection(mContext)) {
            ApiResponse request = ApiClient.getApiService();

            Call<NewsResources> call = request.getNewsBeranda(etSearch,sortString, API_KEY,10,currentPage);
            call.enqueue(new Callback<NewsResources>() {

                @Override
                public void onResponse(Call<NewsResources> call, Response<NewsResources> response) {

                    if (response.isSuccessful() && response.body().getArticles() != null) {
                        results = fetchResults(response);
                        progresshome.setVisibility(View.GONE);
                        adapter.addAll(results);
                        adapter.addLoadingFooter();

                        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                }

                @Override
                public void onFailure(Call<NewsResources> call, Throwable t) {
                    progresshome.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(mContext, "Internet connection not Available", Toast.LENGTH_SHORT).show();
            progresshome.setVisibility(View.GONE);
        }
    }

    private List<News> fetchResults(Response<NewsResources> response) {
        NewsResources technologiNews = response.body();
        return technologiNews.getArticles();
    }

    private void loadNextPage() {
        if (CoreApp.checkConnection(mContext)) {
            ApiResponse request = ApiClient.getApiService();

            Call<NewsResources> call = request.getNewsBeranda(etSearch,sortString, API_KEY,10,currentPage);
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
                    progresshome.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(mContext, "Internet connection not Available", Toast.LENGTH_SHORT).show();
            progresshome.setVisibility(View.GONE);
        }
    }
}