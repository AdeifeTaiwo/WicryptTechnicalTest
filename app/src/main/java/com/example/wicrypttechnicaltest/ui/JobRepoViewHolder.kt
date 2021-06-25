package com.example.wicrypttechnicaltest.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.wicrypttechnicaltest.R
import com.example.wicrypttechnicaltest.db.JobRepoDatabase
import com.example.wicrypttechnicaltest.model.Jobs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobRepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val job_title: TextView = view.findViewById(R.id.joblabeltextView)
    private val publicationDate: TextView = view.findViewById(R.id.publication_date)
    private val favourite: ToggleButton = view.findViewById(R.id.favouriteImageView)
    private val location: TextView = view.findViewById(R.id.locationTextView)
    private val appliedTextView: TextView = view.findViewById(R.id.apply_textView)
    private val arrowIcon: ImageView = view.findViewById(R.id.imageView2)
    private val applyLayout: LinearLayout = view.findViewById(R.id.linearLayout3)

    private var repo: Jobs? = null

    init {
        favourite.setOnClickListener {
            if (favourite.isChecked) {
                CoroutineScope(Dispatchers.IO).launch {
                    repo?.id?.let { it1 -> update(itemView.context, it1, 1) }
                }

            }

            if (!favourite.isChecked) {
                CoroutineScope(Dispatchers.IO).launch {
                    repo?.id?.let { it1 -> update(itemView.context, it1, 0) }
                }
            }
        }

        applyLayout.setOnClickListener {

            repo?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                applyLayout.context.startActivity(intent)


            }
            CoroutineScope(Dispatchers.IO).launch {
                repo?.id?.let { it1 -> updateForApplying(itemView.context, it1, 1) }
            }


        }


    }


    fun bind(repo: Jobs?) {
        if (repo == null) {
            val resources = itemView.resources
            job_title.text = "Loading"
            location.text = "?"
            publicationDate.text = "?"
        } else {
            showRepoData(repo)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showRepoData(repo: Jobs) {
        this.repo = repo
        job_title.text = trimmedText(repo.job_title.toString())
        location.text = repo.location
        publicationDate.text = formatDate(repo.created_at)

        if (repo.isChecked == 0) {
            favourite.background = itemView.context.resources.getDrawable(R.drawable.ic_baseline_favorite_empty_24)
        } else {
            favourite.background = itemView.context.resources.getDrawable(R.drawable.ic_baseline_favorite_24)
        }

        if (repo.applied == 1) {
            arrowIcon.visibility = View.GONE
            appliedTextView.text = "Applied   "
            appliedTextView.typeface = Typeface.DEFAULT_BOLD

        } else {
            arrowIcon.visibility = View.VISIBLE
            appliedTextView.text = "Apply"
            appliedTextView.typeface = Typeface.DEFAULT

        }


    }


    companion object {
        fun create(parent: ViewGroup): JobRepoViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.job_search_item, parent, false)
            return JobRepoViewHolder(view)
        }
    }

    private fun trimmedText(jobTitle: String): String {

        return if (jobTitle.length > 25) {
            jobTitle.substring(0, 25) + "..."

        } else {
            jobTitle
        }
    }


    private suspend fun update(context: Context, id: String, value: Int) {
        withContext(Dispatchers.IO) {
            val updateJob = JobRepoDatabase.getInstance(context).reposDao().getJobsSearchWithId(id)
            updateJob.isChecked = value
            JobRepoDatabase.getInstance(context).reposDao().update(updateJob)
        }
    }

    private suspend fun updateForApplying(context: Context, id: String, value: Int) {
        withContext(Dispatchers.IO) {
            val updateJob = JobRepoDatabase.getInstance(context).reposDao().getJobsSearchWithId(id)
            updateJob.applied = value
            JobRepoDatabase.getInstance(context).reposDao().update(updateJob)
        }
    }


    private fun formatDate(date: String): String {

        val split = date.split(" ")
        val mm = split[1]
        val dd = split[2]
        val yy = split[5]

        return "$dd $mm, $yy"

    }


}