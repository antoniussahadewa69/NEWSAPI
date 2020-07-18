package com.example.newsapi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class News {
    @SerializedName("title")
    @Expose
    private String mTitle;
    @SerializedName("url")
    @Expose
    private String mUrl;
    @SerializedName("urlToImage")
    @Expose
    private String mUrlToImage;
    private String author;
    private String description;
    private String content;
    private Sources source;
    private String publishedAt;
    private int type;



    public News(String mTitle, String mUrl, String mUrlToImage, int type) {
        this.mTitle = mTitle;
        this.mUrl = mUrl;
        this.mUrlToImage = mUrlToImage;
        this.type = type;
    }

    public News(){

    }

    public String getTitle() { return mTitle; }
    public String getUrl() {
        return mUrl;
    }
    public String getUrlToImage() {
            return mUrlToImage;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Sources getSource() {
        return source;
    }
    public void setSource(Sources source) {
        this.source = source;
    }
    public String getPublishedAt() {
        return publishedAt;
    }
    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}
