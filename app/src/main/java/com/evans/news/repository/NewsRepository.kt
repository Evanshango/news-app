package com.evans.news.repository

import com.evans.news.api.RetroClient
import com.evans.news.db.ArticleDatabase

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetroClient.api.getBreakingNews(countryCode, pageNumber)
}