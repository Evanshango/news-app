package com.evans.news.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.evans.news.NewsApplication
import com.evans.news.R
import com.evans.news.db.ArticleDatabase
import com.evans.news.repository.NewsRepository
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(
            application as NewsApplication,
            newsRepository
        )
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        bottom_nav.setupWithNavController(fragment.findNavController())
    }
}
