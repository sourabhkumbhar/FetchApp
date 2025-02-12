package com.interview.fetch.repository

import com.interview.fetchapp.data.ListItem
import com.interview.fetchapp.network.ApiService
import com.interview.fetchapp.util.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class FetchRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun getFilteredSortedList(): Flow<Resource<List<ListItem>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getListItems()
            val filteredSorted = response
                .filter { !it.name.isNullOrBlank() }
                .sortedWith(compareBy({ it.listId }, { it.name }))
            emit(Resource.Success(filteredSorted))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown Error"))
        }
    }
}