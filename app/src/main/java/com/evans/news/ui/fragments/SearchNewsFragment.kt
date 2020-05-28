package com.evans.news.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.evans.news.R
import com.evans.news.ui.NewsActivity
import com.evans.news.ui.NewsViewModel

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
    }
}
