package com.evans.news.ui

import androidx.lifecycle.ViewModel
import com.evans.news.repository.NewsRepository

class NewsViewModel(
    val newsRepository: NewsRepository
): ViewModel() {
}