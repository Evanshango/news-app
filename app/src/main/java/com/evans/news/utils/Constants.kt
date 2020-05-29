package com.evans.news.utils

import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Constants {

    companion object {
        const val API_KEY = "2f2ee4e3b3b44bc29ceca8b8155e6815"
        const val BASE_URL = "https://newsapi.org"
        const val SEARCH_NEWS_TIME_DELAY = 500L

        fun getFormattedDate(publishedAt: String): String? {
            val prettyTime = PrettyTime(Locale(getCountry()))
            var isTime: String? = null
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
                val date = sdf.parse(publishedAt)
                isTime = prettyTime.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return isTime
        }

        private fun getCountry(): String {
            val locale = Locale.getDefault()
            val country = locale.country
            return country.toLowerCase(Locale.ROOT)
        }
    }
}