package com.example.newsapi.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsapi.R;
import com.example.newsapi.model.News;
import com.example.newsapi.realm.NewsRealm;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class NewsRealmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Realm mRealm;
    private RealmResults<NewsRealm> newsRealms;
    private Context mContext;
    ProgressDialog progressDialog;
    public static final int NEWS_IMAGE_TYPE = 2;
    public static final int NEWS_WITHOUT_IMAGE_TYPE = 3;

    public NewsRealmAdapter(RealmResults<NewsRealm> newsRealms1){
        this.newsRealms = newsRealms1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (i) {
            case NEWS_IMAGE_TYPE:
                View v1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_cell, viewGroup, false);
                viewHolder = new NewsRealmAdapter.NewsVH(v1);
                break;
            case NEWS_WITHOUT_IMAGE_TYPE:
                View v2 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_notimage_cell, viewGroup, false);
                viewHolder = new NewsRealmAdapter.NewsVHnotImg(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final NewsRealm result = newsRealms.get(position);
        switch (getItemViewType(position)) {
            case NEWS_IMAGE_TYPE:
                final NewsRealmAdapter.NewsVH newsVH = (NewsRealmAdapter.NewsVH) viewHolder;
                newsVH.titleNewsCell.setText(result.getTitle());
                Picasso.get()
                        .load(result.getUrlToImage())
                        .resize(200, 200)
                        .centerCrop()
                        .into(newsVH.urlToImage);
                break;
            case NEWS_WITHOUT_IMAGE_TYPE:
                final NewsRealmAdapter.NewsVHnotImg newsVHnotImg = (NewsRealmAdapter.NewsVHnotImg) viewHolder;
                newsVHnotImg.tv_news_item.setText(result.getTitle());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (newsRealms.get(position).getUrlToImage() != null)
            return  NEWS_IMAGE_TYPE;
        else {
            return NEWS_WITHOUT_IMAGE_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return newsRealms==null?0:newsRealms.size();
    }

    protected class NewsVH extends RecyclerView.ViewHolder {
        @BindView(R.id.titleNewsCell)
        TextView titleNewsCell;
        @BindView(R.id.urlToImage)
        ImageView urlToImage;

        public NewsVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    protected class NewsVHnotImg extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_news_title)
        TextView tv_news_item;

        public NewsVHnotImg(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}