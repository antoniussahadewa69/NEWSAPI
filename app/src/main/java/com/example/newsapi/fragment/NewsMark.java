package com.example.newsapi.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapi.R;
import com.example.newsapi.activity.WebviewNewsActivity;
import com.example.newsapi.adapter.NewsAdapter;
import com.example.newsapi.adapter.NewsRealmAdapter;
import com.example.newsapi.core.CoreApp;
import com.example.newsapi.core.PaginationScrollListener;
import com.example.newsapi.core.RecyclerItemListener;
import com.example.newsapi.model.News;
import com.example.newsapi.model.NewsResources;
import com.example.newsapi.realm.NewsRealm;
import com.example.newsapi.rest.ApiClient;
import com.example.newsapi.rest.ApiResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.newsapi.model.Contants.API_KEY;
import static com.example.newsapi.model.Contants.SORT_PUBLISHEDAT;

/**
 * Created by antoniuskrisnasahadewa on 17/06/2020.
 */

public class NewsMark extends Fragment {
    Context mContext;
    Realm mRealm;
    @BindView(R.id.rvMark)
    RecyclerView rvmark;
    @BindView(R.id.linearNewsmarkEmpty)
    LinearLayout linearNewsmarkEmpty;
    LinearLayoutManager linearLayoutManager;
    private NewsRealmAdapter adapter;
    RealmChangeListener realmChangeListener;
    private RealmResults<NewsRealm> newsRealms;

    public NewsMark() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_newmark, container, false);
        ButterKnife.bind(this, view);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mContext = getContext();
        mRealm = Realm.getDefaultInstance();
        adapter = new NewsRealmAdapter(newsRealms);
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvmark.setLayoutManager(linearLayoutManager);
        rvmark.setItemAnimator(new DefaultItemAnimator());
        rvmark.setAdapter(adapter);
        rvmark.addOnItemTouchListener(new RecyclerItemListener(mContext, rvmark, new RecyclerItemListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                NewsRealm news = newsRealms.get(position);
                Intent title_Intent = new Intent(mContext, WebviewNewsActivity.class);
                title_Intent.putExtra("isMark", 1);
                title_Intent.putExtra("url", news.getUrl());
                title_Intent.putExtra("sourceId",news.getSourceRealm().getId());
                title_Intent.putExtra("sourceName",news.getSourceRealm().getName());
                title_Intent.putExtra("author",news.getAuthor());
                title_Intent.putExtra("title",news.getTitle());
                title_Intent.putExtra("description",news.getDescription());
                title_Intent.putExtra("urltoimage",news.getUrlToImage());
                title_Intent.putExtra("content",news.getContent());
                title_Intent.putExtra("publishedAt",news.getPublishedAt());
                startActivity(title_Intent);
            }

            @Override
            public void onLongClick(View view, final int position) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setTitle("Delete Confirmation?");
                alertDialogBuilder
                        .setMessage("Do you sure delete this news?")
                        .setCancelable(false)
                        .setPositiveButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })

                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mRealm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        if (realm.where(NewsRealm.class).equalTo("id", newsRealms.get(position).getId()).findFirst() != null) {
                                            realm.where(NewsRealm.class).equalTo("id", newsRealms.get(position).getId()).findFirst().deleteFromRealm();
                                        }
                                        Toast.makeText(getContext(), "Delete Item in NewsMark Sucesss", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }));
        newsRealms = mRealm.where(NewsRealm.class).findAll();
        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                adapter.notifyDataSetChanged();
            }
        };
        setRecyclerView();
        return view;
    }

    private void setRecyclerView() {
        adapter = new NewsRealmAdapter(newsRealms);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvmark.setLayoutManager(layoutManager);
        rvmark.setItemAnimator(new DefaultItemAnimator());
        rvmark.setAdapter(adapter);
    }

    private void loadFirstPage() {
        if (CoreApp.checkConnection(mContext)) {

        } else {
            Toast.makeText(mContext, "Internet connection not Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        newsRealms.addChangeListener(realmChangeListener);
        newsRealms = mRealm.where(NewsRealm.class).findAll();
        if(newsRealms.size()==0){
            rvmark.setVisibility(View.GONE);
            linearNewsmarkEmpty.setVisibility(View.VISIBLE);
        }else if (newsRealms.size()>0){
            rvmark.setVisibility(View.VISIBLE);
            linearNewsmarkEmpty.setVisibility(View.GONE);
        }
    }
}