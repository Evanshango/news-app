package com.evans.news.repository

import com.evans.news.api.RetroClient
import com.evans.news.db.ArticleDatabase
import com.evans.news.models.Article

class NewsRepository(private val db: ArticleDatabase) {
    
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetroClient.apiClient.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetroClient.apiClient.searchNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedArticles() = db.getArticleDao().getArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}