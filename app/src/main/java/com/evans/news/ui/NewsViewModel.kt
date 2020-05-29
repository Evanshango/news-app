package com.evans.news.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evans.news.models.NewsResponse
import com.evans.news.repository.NewsRepository
import com.evans.news.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

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

    private fun handleBrNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}