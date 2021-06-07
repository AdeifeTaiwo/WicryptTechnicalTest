package com.example.wicrypttechnicaltest.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.wicrypttechnicaltest.data.JobSearchRepository
import com.example.wicrypttechnicaltest.model.Jobs
import kotlinx.coroutines.flow.Flow

class SavedJobSearchViewModel(private val repository: JobSearchRepository) : ViewModel() {
    @ExperimentalPagingApi
    fun getFavourite(): Flow<PagingData<Jobs>> {
        return repository.getSavedSearch()
                .cachedIn(viewModelScope)
    }
}