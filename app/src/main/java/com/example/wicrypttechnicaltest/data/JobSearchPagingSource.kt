package com.example.wicrypttechnicaltest.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.wicrypttechnicaltest.api.JobSearchService
import com.example.wicrypttechnicaltest.data.JobSearchRepository.Companion.NETWORK_PAGE_SIZE
import com.example.wicrypttechnicaltest.model.JobRepo
import com.example.wicrypttechnicaltest.model.Jobs
import retrofit2.HttpException
import java.io.IOException

private const val GITHUB_STARTING_PAGE_INDEX = 1

class JobSearchPagingSource(
        private val service: JobSearchService,
        private val query: String
) : PagingSource<Int, Jobs>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Jobs> {

        val position = params.key?: GITHUB_STARTING_PAGE_INDEX
        val apiQuery = query

        return  try {
            val response = service.jobSearchRepos(position,apiQuery)
            val repos = response
            val nextKey = if(repos.isEmpty()){
                null
            }

            else{
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }



            LoadResult.Page(
                    data = repos,
                    prevKey = if(position == GITHUB_STARTING_PAGE_INDEX) null else position -1,
                    nextKey = nextKey
            )
        }
        catch (exception: IOException){
            LoadResult.Error(exception)
        }
        catch (exception: HttpException){
            LoadResult.Error(exception)
        }

    }



    override fun getRefreshKey(state: PagingState<Int, Jobs>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }


}