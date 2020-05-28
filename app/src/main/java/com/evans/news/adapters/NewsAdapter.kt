package com.evans.news.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.evans.news.R
import com.evans.news.models.Article
import kotlinx.android.synthetic.main.news_item.view.*

class NewsAdapter(
    private val context: Context,
    private val itemInteraction: ArticleInteraction
) : RecyclerView.Adapter<NewsAdapter.ArticleHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        return ArticleHolder(
            LayoutInflater.from(context).inflate(R.layout.news_item, parent, false),
            itemInteraction
        )
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ArticleHolder(itemView: View, private val interaction: ArticleInteraction?
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        fun bind(article: Article?) {
            article?.let {
                Glide.with(context).load(article.url).into(itemView.article_image)
                itemView.article_title.text = article.title
                itemView.article_description.text = article.description
                itemView.article_source.text = article.source!!.name
            }
        }

        override fun onClick(v: View?) {
            interaction?.onItemClicked(differ.currentList[adapterPosition])
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    interface ArticleInteraction{
        fun onItemClicked(article: Article)
    }
}