package com.example.wicrypttechnicaltest.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.example.wicrypttechnicaltest.Injection
import com.example.wicrypttechnicaltest.R
import com.example.wicrypttechnicaltest.databinding.SavedJobSearchFragmentBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SavedJobSearchFragment : Fragment() {


    private lateinit var adapter : JobSearchAdapter
    private lateinit var viewModel : SavedJobSearchViewModel
    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        adapter = JobSearchAdapter(OnClickListener {

        })

        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory2(requireContext()))
                .get(SavedJobSearchViewModel::class.java)

        val binding : SavedJobSearchFragmentBinding =
                DataBindingUtil.inflate(
                        inflater,
                        R.layout.saved_job_search_fragment,
                        container,
                        false)


        binding.recyclerView.adapter = adapter

        submitAdapter()

        binding.lifecycleOwner = this
        return binding.root
    }

    @ExperimentalPagingApi
    fun submitAdapter(){
        lifecycleScope.launch(){
            viewModel.getFavourite().collectLatest {
                adapter.submitData(it)
            }
        }
    }


}