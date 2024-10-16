package com.learngram.mvvmnewsapp.View.Fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.learngram.mvvmnewsapp.R
import com.learngram.mvvmnewsapp.View.Activities.NewsActivity
import com.learngram.mvvmnewsapp.ViewModel.NewsViewModel
import com.learngram.mvvmnewsapp.databinding.FragmentArticleBinding
import com.learngram.mvvmnewsapp.databinding.FragmentSavedNewsBinding

class ArticleFragment:Fragment(R.layout.fragment_article) {
    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()
    private lateinit var binding: FragmentArticleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentArticleBinding.bind(view)
        viewModel=(activity as NewsActivity).viewModel
        val article=args.article

        binding.webView.apply {
            webViewClient= WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        binding.fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view,"Article saved successfully",Snackbar.LENGTH_SHORT).show()
        }
    }
}