package com.evans.news.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.evans.news.NewsApplication
import com.evans.news.repository.NewsRepository

class NewsViewModelProviderFactory(
    private val application: NewsApplication,
    private val newsRepository: NewsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(application, newsRepository) as T
    }
}