package com.learngram.mvvmnewsapp.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learngram.mvvmnewsapp.Repositories.NewsRepository

class NewsViewModelProviderFactory(
    val app:Application,
    val repository: NewsRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return NewsViewModel(app,repository) as T
    }
}