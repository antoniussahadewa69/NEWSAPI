package com.example.newsapi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewsResources {
    @SerializedName("articles")
    @Expose
    private ArrayList<News> articles;
    private int totalResults;

    public int getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
    public ArrayList<News> getArticles() {
        return articles;
    }
    public void setArticles(ArrayList<News> news) {
        this.articles = news;
    }
}
