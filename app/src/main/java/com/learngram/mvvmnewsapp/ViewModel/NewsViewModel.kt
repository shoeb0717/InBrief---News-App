package com.learngram.mvvmnewsapp.ViewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.Network
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learngram.mvvmnewsapp.Models.Article
import com.learngram.mvvmnewsapp.Models.NewsResponse
import com.learngram.mvvmnewsapp.NewsApplication
import com.learngram.mvvmnewsapp.Repositories.NewsRepository
import com.learngram.mvvmnewsapp.utils.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewModel(
    val app:Application,
    val repository: NewsRepository
): AndroidViewModel(app) {
    val breakingNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage=1
    var breakingNewsResponse:NewsResponse?=null

    val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage=1
    var searchNewsResponse:NewsResponse?=null


    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode:String)=viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    fun searchNews(searchQuery:String)=viewModelScope.launch {
       safeSearchNewsCall(searchQuery)
    }

    private fun handleBreakingNewsResponce(response: Response<NewsResponse>): Resource<NewsResponse>{

        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                breakingNewsPage++
                if(breakingNewsResponse==null){
                    breakingNewsResponse=resultResponse
                }
                else{
                    val oldArticles=breakingNewsResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse?:resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponce(response: Response<NewsResponse>): Resource<NewsResponse>{

        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                searchNewsPage++
                if(searchNewsResponse==null){
                    searchNewsResponse=resultResponse
                }
                else{
                    val oldArticles=searchNewsResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse?:resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    private suspend fun safeSearchNewsCall(searchQuery: String){
        searchNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response=repository.searchNews(searchQuery,searchNewsPage)
                searchNews.postValue(handleSearchNewsResponce(response))
            }
            else{
                searchNews.postValue(Resource.Error("No internet connection"))
            }

        }catch (t:Throwable){
            when(t){
                is IOException-> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeBreakingNewsCall(countryCode: String){
        breakingNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response=repository.getBreakingNews(countryCode,breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponce(response))
            }
            else{
                breakingNews.postValue(Resource.Error("No internet connection"))
            }

        }catch (t:Throwable){
           when(t){
               is IOException-> breakingNews.postValue(Resource.Error("Network Failure"))
               else -> breakingNews.postValue(Resource.Error("Conversion Error"))
           }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun hasInternetConnection():Boolean{
      val connectivityManager=getApplication<NewsApplication>().getSystemService(
          Context.CONNECTIVITY_SERVICE
      ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val activeNetwork=connectivityManager.activeNetwork ?: return false
            val capabilities=connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI->true
                    TYPE_MOBILE->true
                    TYPE_ETHERNET->true
                    else -> false
                }
            }
        }

        return false
    }

    fun saveArticle(article: Article)=viewModelScope.launch {
        repository.upsert(article)
    }

    fun getSavedNews()=repository.getSavedNews()

    fun deleteArticle(article: Article)=viewModelScope.launch {
        repository.deleteArticle(article)
    }
}