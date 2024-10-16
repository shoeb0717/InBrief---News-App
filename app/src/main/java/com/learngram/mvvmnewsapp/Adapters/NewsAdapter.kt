package com.learngram.mvvmnewsapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learngram.mvvmnewsapp.Models.Article
import com.learngram.mvvmnewsapp.R

class NewsAdapter:RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {


    private val differCallback= object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
           return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
           return oldItem==newItem
        }
    }

    val differ=AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
       return ArticleViewHolder(
           LayoutInflater.from(parent.context).inflate(
               R.layout.item_article_preview,
               parent,
               false
           )
       )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article=differ.currentList.get(position)

        holder.apply {
            Glide.with(itemView).load(article.urlToImage).into(articleImage) // Use the appropriate image URL property
            sourceText.text = article.source!!.name
            titleText.text = article.title
            descriptionText.text = article.description
            publishedAtText.text = article.publishedAt
            itemView.setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }

    }

    inner class ArticleViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView) {
        val articleImage: ImageView = itemView.findViewById(R.id.ivArticleImage)
        val sourceText: TextView = itemView.findViewById(R.id.tvSource)
        val titleText: TextView = itemView.findViewById(R.id.tvTitle)
        val descriptionText: TextView = itemView.findViewById(R.id.tvDescription)
        val publishedAtText: TextView = itemView.findViewById(R.id.tvPublishedAt)
    }

    private var onItemClickListener:((Article)->Unit)?=null

    fun setOnClickListener(listener:(Article)->Unit){
        onItemClickListener=listener
    }

}