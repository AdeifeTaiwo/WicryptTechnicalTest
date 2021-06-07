package com.example.wicrypttechnicaltest.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wicrypttechnicaltest.data.JobSearchRepository

class ViewModelFactory(private val repository: JobSearchRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JobSearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}