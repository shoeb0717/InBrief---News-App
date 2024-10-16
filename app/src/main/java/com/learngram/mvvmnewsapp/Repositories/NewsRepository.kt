package com.learngram.mvvmnewsapp.Repositories

import com.learngram.mvvmnewsapp.Models.Article
import com.learngram.mvvmnewsapp.api.RetrofitInstance
import com.learngram.mvvmnewsapp.db.ArticleDatabase
import retrofit2.Retrofit

class NewsRepository(
    val db:ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode:String,pageNumber:Int)=
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery:String, pageNumber: Int)=
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article: Article)=db.getArticleDao().upsert(article)

    fun getSavedNews()=db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article)=db.getArticleDao().deleteArticle(article)
}