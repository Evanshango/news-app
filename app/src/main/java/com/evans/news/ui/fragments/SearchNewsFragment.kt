package com.evans.news.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.evans.news.R
import com.evans.news.adapters.NewsAdapter
import com.evans.news.models.Article
import com.evans.news.ui.NewsActivity
import com.evans.news.ui.NewsViewModel
import com.evans.news.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.evans.news.utils.Resource
import com.evans.news.utils.toast
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news),
    NewsAdapter.ArticleInteraction{

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel

        initSearchView()

        setUpNewsRecycler()

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        requireContext().toast(message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun initSearchView() {
        search_view.setOnClickListener {
            search_view.queryHint = "Search News"
            search_view.isIconified = false
        }

        search_view.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                performSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    delay(SEARCH_NEWS_TIME_DELAY)
                    performSearch(newText)
                }
                return false
            }
        })
    }

    private fun performSearch(query: String?) {
       query?.let {
           if (it.trim().isNotEmpty()){
               viewModel.searchNews(it)
           }
       }
    }

    private fun hideProgressBar() {
        loadingProgress.visibility = View.GONE
    }

    private fun showProgressBar() {
        loadingProgress.visibility = View.VISIBLE
    }

    private fun setUpNewsRecycler() {
        search_news_recycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            newsAdapter = NewsAdapter(requireContext(), this@SearchNewsFragment)
            adapter = newsAdapter
        }
    }

    override fun onItemClicked(article: Article) {
        val bundle = bundleOf("article" to article)
        findNavController().navigate(
            R.id.action_searchNewsFragment_to_articleFragment,
            bundle
        )
    }
}
