package com.example.newsapi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.newsapi.R;
import com.example.newsapi.core.CoreApp;
import com.example.newsapi.realm.NewsRealm;
import com.example.newsapi.realm.SourceRealm;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.Sort;

public class WebviewNewsActivity extends AppCompatActivity {
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.fab_share)
    FloatingActionButton fabShare;
    @BindView(R.id.fab_save)
    FloatingActionButton fabSave;
    int indikator = 0;
    String title = "";
    String url = "";
    String SourcesId = "";
    String SourcesName = "";
    String author = "";
    String description = "";
    String urlToImage = "";
    String content = "";
    String publishedAt = "";
    int Mark = 0;
    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_webview);
        ButterKnife.bind(this);
        mRealm = Realm.getDefaultInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        author = getIntent().getStringExtra("author");
        urlToImage = getIntent().getStringExtra("urltoimage");
        url = getIntent().getStringExtra("url");
        SourcesId = getIntent().getStringExtra("sourceId");
        SourcesName = getIntent().getStringExtra("sourceName");
        content = getIntent().getStringExtra("content");
        publishedAt = getIntent().getStringExtra("publishedAt");
        Mark = getIntent().getIntExtra("isMark",0);
        getSupportActionBar().setTitle(SourcesName+" News");
        refreshLayout.setRefreshing(true);
        refreshLayout.setColorSchemeResources(
                R.color.Accent,
                R.color.Green,
                R.color.Blue,
                R.color.Orange);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    refreshLayout.setRefreshing(false);
                } else {
                    refreshLayout.setRefreshing(true);
                }
            }
        });
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, url);
                startActivity(Intent.createChooser(shareIntent, "Share link using"));
            }
        });
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Mark != 1){
                    final NewsRealm newsRealm = new NewsRealm();
                    final NewsRealm lastId = mRealm.where(NewsRealm.class).findAllSorted("id", Sort.DESCENDING).where().findFirst();
                    newsRealm.setId(lastId != null ? lastId.getId() + 1 : 1);
                    newsRealm.setTitle(title);
                    newsRealm.setAuthor(author);
                    newsRealm.setDescription(description);
                    newsRealm.setContent(content);
                    newsRealm.setPublishedAt(publishedAt);
                    newsRealm.setUrl(url);
                    newsRealm.setUrlToImage(urlToImage);
                    SourceRealm sourceRealm = new SourceRealm();
                    sourceRealm.setId(SourcesId);
                    sourceRealm.setName(SourcesName);
                    newsRealm.setSourceRealm(sourceRealm);
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            if(realm.where(NewsRealm.class).equalTo("title", title).findFirst() == null){
                                realm.copyToRealmOrUpdate(newsRealm);
                                Toast.makeText(WebviewNewsActivity.this,"Save to Mark Succces", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(WebviewNewsActivity.this,"News already exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(WebviewNewsActivity.this,"Don't mark again this News", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indikator == 0){
                    fabSave.show();
                    fabShare.show();
                    fab.setImageResource(R.drawable.ic_minus);
                    indikator = 1;
                } else {
                    fabSave.hide();
                    fabShare.hide();
                    fab.setImageResource(R.drawable.ic_plus);
                    indikator = 0;
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getCacheDir().deleteOnExit();
        webView.clearCache(true);
    }
}