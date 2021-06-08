package com.example.wicrypttechnicaltest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.wicrypttechnicaltest.data.JobSearchRepository
import com.example.wicrypttechnicaltest.model.Jobs
import kotlinx.coroutines.flow.Flow

class JobSearchViewModel(private val repository: JobSearchRepository): ViewModel() {

    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<Jobs>>? = null

    @ExperimentalPagingApi
    fun searchRepo(queryString: String): Flow<PagingData<Jobs>> {
        val lastResult = currentSearchResult

        if(queryString == currentQueryValue && lastResult != null){
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<Jobs>> = repository.getSearchResultStream(queryString)
                .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    private val _toggleButtonClicked = MutableLiveData<Jobs?>()

    val toggleButtonClicked : LiveData<Jobs?>
        get() = _toggleButtonClicked

    fun onToggleButtonClicked(jobs: Jobs) {
        _toggleButtonClicked.value = jobs
    }

    fun onToggleFinished() {
        _toggleButtonClicked.value = null
    }

    sealed class UiModel {
        data class RepoItem(val repo: Jobs) : UiModel()
        data class SeparatorItem(val description: String) : UiModel()
    }

}