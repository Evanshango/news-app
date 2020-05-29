package com.evans.news.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.evans.news.R
import com.evans.news.ui.NewsActivity
import com.evans.news.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private lateinit var viewModel: NewsViewModel
    private val args: ArticleFragmentArgs by navArgs()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        val article = args.article
        news_article_toolbar.title = article.title

        save_article.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article Saved", Snackbar.LENGTH_SHORT).show()
        }

        web_view.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(article.url)
        }
    }
}
