package com.example.wicrypttechnicaltest.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.wicrypttechnicaltest.api.JobSearchService
import com.example.wicrypttechnicaltest.db.JobRepoDatabase
import com.example.wicrypttechnicaltest.db.RemoteKeys
import com.example.wicrypttechnicaltest.model.Jobs
import kotlinx.coroutines.Job
import retrofit2.HttpException
import java.io.IOException

private const val GITHUB_STARTING_PAGE_INDEX = 1

@ExperimentalPagingApi
class JobSearchRemoteMediator(
        private val query: String,
        private val service: JobSearchService,
        private val repoDatabase: JobRepoDatabase

) : RemoteMediator<Int, Jobs>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Jobs>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: GITHUB_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKeys = remoteKeys?.prevKey
                if (prevKeys == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKeys
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }
        val apiQuery = query
        try {
            val apiResponse = service.jobSearchRepos(page, apiQuery)
            val repos = apiResponse
            val endOfPagination = repos.isEmpty()

            repoDatabase.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    //repoDatabase.remoteKeysDao().clearRemoteKeys()
                    //repoDatabase.reposDao().clearRepos()
                }
                val prevKey = if (page == GITHUB_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPagination) null else page + 1
                val keys = repos.map {
                    RemoteKeys(
                            repoId = it.id,
                            nextKey = nextKey,
                            prevKey = prevKey
                    )
                }
                repoDatabase.remoteKeysDao().insertAll(keys)
                repoDatabase.reposDao().insertAll(repos)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Jobs>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
                ?.let { repo ->
                    // Get the remote keys of the last item retrieved
                    repoDatabase.remoteKeysDao().remoteKeysRepoId(repo.id)
                }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Jobs>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
                ?.let { repo ->
                    // Get the remote keys of the first items retrieved
                    repoDatabase.remoteKeysDao().remoteKeysRepoId(repo.id)
                }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
            state: PagingState<Int, Jobs>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                repoDatabase.remoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }


}