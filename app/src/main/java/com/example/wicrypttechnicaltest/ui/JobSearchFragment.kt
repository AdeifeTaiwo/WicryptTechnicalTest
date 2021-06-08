package com.example.wicrypttechnicaltest.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ActionProvider
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.wicrypttechnicaltest.Injection
import com.example.wicrypttechnicaltest.R
import com.example.wicrypttechnicaltest.databinding.JobSearchFragmentBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class JobSearchFragment : Fragment() {

    private lateinit var  adapter: JobSearchAdapter
    private lateinit var binding: JobSearchFragmentBinding
    private lateinit var viewModel: JobSearchViewModel
    private var searchJob: Job? = null



    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        adapter = JobSearchAdapter()

        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(requireContext()))
                .get(JobSearchViewModel::class.java)

        val binding: JobSearchFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.job_search_fragment, container, false)

        this.binding = binding
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(decoration)
        binding.recyclerView.adapter = adapter
        search("code")


        binding.lifecycleOwner = this

        return binding.root




    }

    @ExperimentalPagingApi
    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo(query).collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow
                    .distinctUntilChangedBy { it.refresh }
                    .filter { it.refresh is LoadState.NotLoading }
                    .collect { binding.recyclerView.scrollToPosition(0) }

        }
    }

    @ExperimentalPagingApi
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

       val menuItem = menu.findItem(R.id.action_search)
       val searchView: SearchView = menuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.e("gas", query.toString())
                search(query.toString())
                //initSearch(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
        super.onCreateOptionsMenu(menu, inflater)
    }



    private fun initSearch(query: String) {


        // Scroll to top when the list is refreshed from network.

    }



    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Android"
    }


}