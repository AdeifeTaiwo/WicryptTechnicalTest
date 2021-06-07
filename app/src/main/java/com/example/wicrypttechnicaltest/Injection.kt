package com.example.wicrypttechnicaltest

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.wicrypttechnicaltest.api.JobSearchService
import com.example.wicrypttechnicaltest.data.JobSearchRepository
import com.example.wicrypttechnicaltest.db.JobRepoDatabase
import com.example.wicrypttechnicaltest.ui.ViewModelFactory
import com.example.wicrypttechnicaltest.ui.ViewModelFactory2
import java.security.AccessControlContext

object Injection {

    private fun provideJobSearchRepository(context: Context): JobSearchRepository{
        return JobSearchRepository(JobSearchService.create(), JobRepoDatabase.getInstance(context) )

    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory{
        return  ViewModelFactory(provideJobSearchRepository(context))
    }

    fun provideViewModelFactory2(context: Context): ViewModelProvider.Factory{
        return  ViewModelFactory2(provideJobSearchRepository(context))
    }

}