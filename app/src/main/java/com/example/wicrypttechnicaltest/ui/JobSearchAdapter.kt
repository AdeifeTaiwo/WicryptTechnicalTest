package com.example.wicrypttechnicaltest.ui

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.wicrypttechnicaltest.R
import com.example.wicrypttechnicaltest.databinding.ActivityMainBinding
import com.example.wicrypttechnicaltest.databinding.JobSearchFragmentBinding
import com.example.wicrypttechnicaltest.databinding.JobSearchItemBinding
import com.example.wicrypttechnicaltest.model.Jobs
import kotlinx.coroutines.Job

class JobSearchAdapter : PagingDataAdapter<Jobs, JobRepoViewHolder>(REP0_COMPANION){




    override fun onBindViewHolder(holder: JobRepoViewHolder, position: Int) {
        val jobs = getItem(position)
        if(jobs != null) {
            holder.bind(jobs)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobRepoViewHolder {

        return JobRepoViewHolder.create(parent)

    }

    companion object REP0_COMPANION : DiffUtil.ItemCallback<Jobs>() {
        override fun areItemsTheSame(oldItem: Jobs, newItem: Jobs): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Jobs, newItem: Jobs): Boolean {
            return oldItem.id == newItem.id
        }
    }


}


