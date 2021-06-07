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

class JobSearchAdapter(private val clickListener: OnClickListener) : PagingDataAdapter<Jobs, JobSearchViewHolder>(REP0_COMPANION){




    override fun onBindViewHolder(holder: JobSearchViewHolder, position: Int) {
        val jobs = getItem(position)


        holder.binding.also {
            it.jobs = jobs
            it.clickListener = clickListener
        }
        holder.binding.favouriteImageView.buttonDrawable= holder.itemView.context.resources.getDrawable(R.drawable.toggle_fav_button)
        holder.binding.favouriteImageView.setOnClickListener(View.OnClickListener {

        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobSearchViewHolder {

        val withDataBinding: JobSearchItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                JobSearchViewHolder.LAYOUT,
                parent,
                false)
        return JobSearchViewHolder(withDataBinding)

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

class JobSearchViewHolder(val binding: JobSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root){

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.job_search_item
    }

}

class OnClickListener(val clickListener: (jobSearch : Jobs) -> Unit) {
    fun onClick(jobClick :Jobs) = clickListener(jobClick)
}