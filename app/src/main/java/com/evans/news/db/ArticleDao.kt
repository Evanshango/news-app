package com.evans.news.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.evans.news.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}