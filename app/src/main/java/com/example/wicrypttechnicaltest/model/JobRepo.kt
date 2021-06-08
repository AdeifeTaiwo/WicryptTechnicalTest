package com.example.wicrypttechnicaltest.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName



data class JobRepo(
        @field:SerializedName("0-legal-notice") val legal_notice: String,
        @field:SerializedName("job-count") val job_count: Int =0,
        @field:SerializedName("jobs") val jobs:List<Jobs> = emptyList()
){
    val nextKey : Int = 1
}


@Entity(tableName = "repos")
data class Jobs(
        @PrimaryKey @field:SerializedName("id") val id: String,
        @field:SerializedName("type") val type: String,
        @field:SerializedName("url") val url: String,
        @field:SerializedName("location") val location: String,
        @field:SerializedName("created_at") val created_at: String,
        @field:SerializedName("company") val companyName: String,
        @field:SerializedName("company_url") val company_url: String?,
        @field:SerializedName("title") val job_title: String?,
        @field:SerializedName("description") val description: String?,
        @field:SerializedName("company_logo") val company_logo:String?,
        @field:SerializedName("how_to_apply") val how_to_apply: String,
        var isChecked: Int =0,
        var applied: Int =0
){


}

