package com.example.wicrypttechnicaltest.model

import java.lang.Exception

sealed class JobRepoSearchResult {
    data class Success(val data: JobRepo): JobRepoSearchResult()
    data class Error(val error: Exception): JobRepoSearchResult()

}


