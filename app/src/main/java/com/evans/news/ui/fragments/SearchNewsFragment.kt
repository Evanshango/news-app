package com.evans.news.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evans.news.R
import com.evans.news.adapters.NewsAdapter
import com.evans.news.models.Article
import com.evans.news.ui.NewsActivity
import com.evans.news.ui.NewsViewModel
import com.evans.news.utils.Constants
import com.evans.news.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.evans.news.utils.Resource
import com.evans.news.utils.toast
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news),
    NewsAdapter.ArticleInteraction {

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
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages =
                            newsResponse.totalResults?.div(Constants.QUERY_PAGE_SIZE + 2)
                        isLastPage = viewModel.searchNewsPage == totalPages
                        if (isLastPage)
                            search_news_recycler.setPadding(0, 0, 0, 0)
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

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

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
            if (it.trim().isNotEmpty()) {
                viewModel.searchNews(it)
            }
        }
    }

    private fun hideProgressBar() {
        loadingProgress.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        loadingProgress.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.searchNews(search_view.query.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setUpNewsRecycler() {
        search_news_recycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            newsAdapter = NewsAdapter(requireContext(), this@SearchNewsFragment)
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
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
