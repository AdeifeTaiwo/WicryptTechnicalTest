package com.example.wicrypttechnicaltest.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.ToggleButton
import androidx.databinding.BindingAdapter
import com.example.wicrypttechnicaltest.R
import com.example.wicrypttechnicaltest.db.JobRepoDatabase
import com.example.wicrypttechnicaltest.model.Jobs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.lang.StringBuilder


@BindingAdapter("trimmedText")
fun TextView.trimmedText(jobTitle: String){


    val split = jobTitle.split(" ", "-")


    text = if(jobTitle.length > 25){
        jobTitle.substring(0,25) +"..."
    } else{
        jobTitle
    }

}

@BindingAdapter("getDates")
fun TextView.getDate(date: String){

    val yy = date.substring(0,4)
    val months = resources.getStringArray(R.array.months)
    val mm = months[date.substring(5,7).toInt()-1]
    val dd = date.substring(8,10)

    val dateText = "$dd $mm, $yy"

    text = dateText
}

@BindingAdapter("toogleButton")
fun ToggleButton.toggle(jobSearch: Jobs){

    if(isChecked){
        CoroutineScope(Dispatchers.IO).launch {
            //update(context, jobSearch.id, 1)
        }
    }





}

private suspend fun update(context: Context, id: String, value: Int){
    withContext(Dispatchers.IO){
        val updateJob = JobRepoDatabase.getInstance(context).reposDao().getJobsSearchWithId(id)
        updateJob.isChecked = value
        JobRepoDatabase.getInstance(context).reposDao().update(updateJob)
    }
}