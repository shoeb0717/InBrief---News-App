package com.learngram.mvvmnewsapp.View.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.learngram.mvvmnewsapp.Adapters.NewsAdapter
import com.learngram.mvvmnewsapp.ViewModel.NewsViewModel
import com.learngram.mvvmnewsapp.ViewModel.NewsViewModelProviderFactory
import com.learngram.mvvmnewsapp.R
import com.learngram.mvvmnewsapp.Repositories.NewsRepository
import com.learngram.mvvmnewsapp.View.Fragments.BreakingNewsFragment
import com.learngram.mvvmnewsapp.databinding.ActivityNewsBinding
import com.learngram.mvvmnewsapp.db.ArticleDatabase

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        val db=ArticleDatabase(this)
        val newsRepository=NewsRepository(db)
        val viewModelProviderFactory= NewsViewModelProviderFactory(application,newsRepository)
        viewModel=ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

        binding.bottomNavigationView.setupWithNavController(navController)

    }
}
