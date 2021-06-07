package com.example.wicrypttechnicaltest.api

import com.example.wicrypttechnicaltest.model.JobRepo
import com.example.wicrypttechnicaltest.model.Jobs
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

interface JobSearchService {

    //https://remotive.io/api/remote-jobs?search=front%20end
    //https://jobs.github.com/positions.json?page=1&search=code

@GET("positions.json?")
suspend fun jobSearchRepos(
        @Query("page") page: Int,
        @Query("search") search: String

) : List<Jobs>


    companion object {
        private const val BASE_URL = "https://jobs.github.com/"

        fun create(): JobSearchService{
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
            return  Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(JobSearchService::class.java)
        }
    }
}

