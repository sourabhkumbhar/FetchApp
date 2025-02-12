package com.interview.fetchapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.fetch.repository.FetchRepository
import com.interview.fetchapp.data.ListItem
import com.interview.fetchapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: FetchRepository
) : ViewModel() {

    private val _items = MutableStateFlow<Resource<List<ListItem>>>(Resource.Loading())
    val items: StateFlow<Resource<List<ListItem>>> = _items.asStateFlow()

    fun fetchListItems() {
        viewModelScope.launch {
            repository.getFilteredSortedList().collect { _items.value = it }
        }
    }
}