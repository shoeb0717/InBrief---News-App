package com.learngram.mvvmnewsapp.api

import com.learngram.mvvmnewsapp.Models.NewsResponse
import com.learngram.mvvmnewsapp.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.Query
import retrofit2.http.GET

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String ="us",
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey:String =API_KEY
    ) : Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey:String =API_KEY
    ) : Response<NewsResponse>
}