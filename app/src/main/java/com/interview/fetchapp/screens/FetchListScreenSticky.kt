package com.interview.fetchapp.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.interview.fetchapp.ListViewModel
import com.interview.fetchapp.data.ListItem
import com.interview.fetchapp.util.Resource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FetchListScreenSticky(viewModel: ListViewModel = hiltViewModel()) {
    val state by viewModel.items.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchListItems()
    }

    Scaffold { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (state) {
                is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                is Resource.Success -> {
                    val groupedItems = remember((state as Resource.Success<List<ListItem>>).data) {
                        (state as Resource.Success<List<ListItem>>).data!!
                            .sortedWith(compareBy({ it.listId }, { it.name }))
                            .groupBy { it.listId }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        groupedItems.forEach { (listId, items) ->
                            // Sticky Header
                            stickyHeader {
                                Text(
                                    text = "List ID: $listId",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .padding(16.dp),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            // List Items
                            items(items, key = { it.id }) { item ->
                                ListItemRow(item)
                            }
                        }
                    }
                }

                is Resource.Error -> Text(
                    text = "Error: ${(state as Resource.Error).message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun ListContent(items: List<ListItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items, key = { it.id }) { item ->  // <--- Add key for performance
            ListItemRow(item)
        }
    }
}

@Composable
fun ListItemRow(item: ListItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ID: ${item.id}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Name: ${item.name!!}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}