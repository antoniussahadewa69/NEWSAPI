package com.example.newsapi.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NewsRealm extends RealmObject {

    @PrimaryKey
    int id;
    String title;
    String author;
    String description;
    String url;
    String urlToImage;
    String publishedAt;
    String content;
    SourceRealm sourceRealm;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public SourceRealm getSourceRealm() {
        return sourceRealm;
    }
    public void setSourceRealm(SourceRealm sourceRealm) {
        this.sourceRealm = sourceRealm;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrlToImage() {
        return urlToImage;
    }
    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }
    public String getPublishedAt() {
        return publishedAt;
    }
    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}