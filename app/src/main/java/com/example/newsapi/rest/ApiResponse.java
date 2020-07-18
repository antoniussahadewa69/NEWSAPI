package com.example.newsapi.rest;

import com.example.newsapi.model.NewsResources;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiResponse {

    @GET("top-headlines")
    Call<NewsResources> getNewsTrending(@Query("country") String country,
                                        @Query("apiKey") String apiKey,
                                        @Query("pageSize") int pageSize,
                                        @Query("page") int page);

    @GET("everything")
    Call<NewsResources> getNewsBeranda(@Query("q") String keyString,
                                       @Query("sortBy") String sortBy,
                                      @Query("apiKey") String apiKey,
                                      @Query("pageSize") int pageSize,
                                      @Query("page") int page);

    @GET("top-headlines")
    Call<NewsResources> getNewsByCategory(@Query("country") String country,
                                      @Query("category") String category,
                                      @Query("apiKey") String apiKey,
                                      @Query("pageSize") int pageSize,
                                      @Query("page") int page);
}
