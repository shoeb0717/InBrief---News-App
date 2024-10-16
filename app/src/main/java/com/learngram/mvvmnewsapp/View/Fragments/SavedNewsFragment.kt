package com.learngram.mvvmnewsapp.View.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.learngram.mvvmnewsapp.Adapters.NewsAdapter
import com.learngram.mvvmnewsapp.R
import com.learngram.mvvmnewsapp.View.Activities.NewsActivity
import com.learngram.mvvmnewsapp.ViewModel.NewsViewModel
import com.learngram.mvvmnewsapp.databinding.FragmentSavedNewsBinding

class SavedNewsFragment:Fragment(R.layout.fragment_saved_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentSavedNewsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentSavedNewsBinding.bind(view)
        setUpRecyclerView()
        viewModel=(activity as NewsActivity).viewModel
        newsAdapter.setOnClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }

            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment,
                bundle
            )
        }

        val itemTouchHelperCallback=object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition
                val article= newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view,"Successfully deleted article",Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }


        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })

    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSavedNews.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = newsAdapter
        }
    }
}