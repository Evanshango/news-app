package com.evans.news.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evans.news.models.Article
import com.evans.news.models.NewsResponse
import com.evans.news.repository.NewsRepository
import com.evans.news.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    private var breakingNewsPage = 1

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    private var searchNewsPage = 1

    init {
        "us".getBreakingNews()
    }

    private fun String.getBreakingNews() = viewModelScope.launch {
        breakingNews.value = Resource.Loading()
        val response = newsRepository.getBreakingNews(
            this@getBreakingNews,
            breakingNewsPage
        )
        breakingNews.value = handleBrNewsResponse(response)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.value = Resource.Loading()
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        searchNews.value = handleSearchNewsResponse(response)
    }

    private fun handleBrNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedArticles()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
}