package com.example.newsapi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.newsapi.R;
import com.example.newsapi.core.CoreApp;
import com.example.newsapi.model.News;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    public static final int NEWS_IMAGE_TYPE = 2;
    public static final int NEWS_WITHOUT_IMAGE_TYPE = 3;
    private List<News> newsResult;
    private Context context;

    private boolean isLoadingAdded = false;

    public NewsAdapter(Context context) {
        this.context = context;
        newsResult = new ArrayList<>();
    }

    public List<News> getNewsResult() {
        return newsResult;
    }

    public void setNewsResult(List<News> newsResult) {
        this.newsResult = newsResult;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (i) {
            case NEWS_IMAGE_TYPE:
                View v1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_cell, viewGroup, false);
                viewHolder = new NewsVH(v1);
                break;
            case NEWS_WITHOUT_IMAGE_TYPE:
                View v2 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_notimage_cell, viewGroup, false);
                viewHolder = new NewsVHnotImg(v2);
                break;
            case LOADING:
                View v3 = inflater.inflate(R.layout.progress_cell, viewGroup, false);
                viewHolder = new LoadingVH(v3);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater,int i) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.news_cell, parent, false);
        viewHolder = new NewsVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        News result = newsResult.get(i);
        switch (getItemViewType(i)) {
            case NEWS_IMAGE_TYPE:
                final NewsVH newsVH = (NewsVH) viewHolder;
                newsVH.titleNewsCell.setText(result.getTitle());
                newsVH.sourceNewsCell.setText(result.getSource().getName());
                newsVH.timeNewsCell.setText(result.getPublishedAt() != null ? CoreApp.reformatDate(result.getPublishedAt()) : new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault()).format(new Date()));
                Picasso.get()
                        .load(result.getUrlToImage())
                        .resize(200, 200)
                        .centerCrop()
                        .into(newsVH.urlToImage);
                break;
            case NEWS_WITHOUT_IMAGE_TYPE:
                final NewsVHnotImg newsVHnotImg = (NewsVHnotImg) viewHolder;
                newsVHnotImg.tv_news_item.setText(result.getTitle());
                newsVHnotImg.tv_news_source_Noimg.setText(result.getSource().getName());
                newsVHnotImg.tv_news_time_noImg.setText(result.getPublishedAt() != null ? CoreApp.reformatDate(result.getPublishedAt()) : new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault()).format(new Date()));
            case LOADING:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return newsResult == null ? 0 : newsResult.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == newsResult.size() - 1 && isLoadingAdded) {
            return LOADING;
        } else {
            if (newsResult.get(position).getUrlToImage() != null)
                return  NEWS_IMAGE_TYPE;
            else {
                return NEWS_WITHOUT_IMAGE_TYPE;
            }
        }
    }



    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(News r) {
        newsResult.add(r);
        notifyItemInserted(newsResult.size() - 1);
    }

    public void addAll(List<News> newsResult) {
        for (News result : newsResult) {
            add(result);
        }
    }

    public void remove(News r) {
        int position = newsResult.indexOf(r);
        if (position > -1) {
            newsResult.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = newsResult.size() - 1;
        News result = getItem(position);

        if (result != null) {
            newsResult.remove(position);
            notifyItemRemoved(position);
        }
    }

    public News getItem(int position) {
        return newsResult.get(position);
    }

    /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class NewsVH extends RecyclerView.ViewHolder {
        @BindView(R.id.titleNewsCell)
        TextView titleNewsCell;
        @BindView(R.id.urlToImage)
        ImageView urlToImage;
        @BindView(R.id.sourceNewsCell)
        TextView sourceNewsCell;
        @BindView(R.id.timeNewsCell)
        TextView timeNewsCell;

        public NewsVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
    protected class NewsVHnotImg extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_news_title)
        TextView tv_news_item;
        @BindView(R.id.tv_news_source_Noimg)
        TextView tv_news_source_Noimg;
        @BindView(R.id.tv_news_time_noImg)
        TextView tv_news_time_noImg;

        public NewsVHnotImg(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
