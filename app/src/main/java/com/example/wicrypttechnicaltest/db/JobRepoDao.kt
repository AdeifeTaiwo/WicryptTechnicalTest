package com.example.wicrypttechnicaltest.db

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.*
import com.example.wicrypttechnicaltest.model.Jobs
import kotlinx.coroutines.flow.Flow

@Dao
interface JobRepoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(repos: List<Jobs>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(repos: Jobs)

    @Query("SELECT * FROM repos WHERE companyName LIKE :queryString OR description LIKE :queryString")
    fun reposByName(queryString: String): PagingSource<Int, Jobs>

    @Query("SELECT * FROM repos WHERE isChecked = 1")
    fun reposByCheckFavourite(): PagingSource<Int, Jobs>

    @Query("SELECT * from repos WHERE id = :key")
    fun getJobsSearchWithId(key: String): Jobs

    @Query("DELETE FROM repos")
    suspend fun clearRepos()
}