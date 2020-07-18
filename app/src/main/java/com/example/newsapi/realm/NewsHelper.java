package com.example.newsapi.realm;

import com.example.newsapi.model.News;

import java.util.List;

public class NewsHelper {
    public int add(News r,List<News> newsResult) {
        newsResult.add(r);
        return newsResult.size() - 1;
    }

    public List<News> addAll(List<News> newsResult) {
        for (News result : newsResult) {
            add(result,newsResult);
        }
        return newsResult;
    }

    public int remove(News r,List<News> newsResult) {
        int position = newsResult.indexOf(r);
        if (position > -1) {
            newsResult.remove(position);
        }
        return position;
    }
}
