package com.learngram.mvvmnewsapp.View.Fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learngram.mvvmnewsapp.Adapters.NewsAdapter
import com.learngram.mvvmnewsapp.R
import com.learngram.mvvmnewsapp.View.Activities.NewsActivity
import com.learngram.mvvmnewsapp.ViewModel.NewsViewModel
import com.learngram.mvvmnewsapp.databinding.FragmentSearchNewsBinding
import com.learngram.mvvmnewsapp.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.learngram.mvvmnewsapp.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.learngram.mvvmnewsapp.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment:Fragment(R.layout.fragment_search_news) {
    lateinit var viewModel: NewsViewModel
    private lateinit var binding:FragmentSearchNewsBinding
    lateinit var newsAdapter: NewsAdapter
    val TAG="SearchNewsFragment"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentSearchNewsBinding.bind(view)
        setUpRecyclerView()

        newsAdapter.setOnClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }

            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel=(activity as NewsActivity).viewModel

        var job:Job?=null
        binding.etSearch.addTextChangedListener { editable->
            job?.cancel()
            job= MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }
        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success ->{
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages= newsResponse.totalResults/ QUERY_PAGE_SIZE + 2
                        isLastPage=viewModel.searchNewsPage==totalPages
                        if(isLastPage){
                            binding.rvSearchNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred : $message", Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })

    }

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility=View.INVISIBLE
        isLoading=false
    }

    private fun showProgressBar(){
        binding.paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
    }


    var isLoading=false
    var isLastPage=false
    var isScrolling=false


    val scrollListener=object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalItemCount=layoutManager.itemCount

            val isNotLoadingAndNotLastPage= !isLoading && !isLastPage
            val isAtLastItem= firstVisibleItemPosition+visibleItemCount >= totalItemCount
            val isNotAtBeginning= firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible=totalItemCount >= QUERY_PAGE_SIZE

            val shouldPaginate=isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){
                viewModel.searchNews(binding.etSearch.text.toString())
                isScrolling=false
            }

        }
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = newsAdapter
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }
    }
}