package com.interview.fetchapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.interview.fetchapp.data.ListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(items: List<ListItem>) {
    Scaffold(topBar = { TopAppBar(title = { Text("Fetch Items") }) }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items.groupBy { it.listId }.forEach { (listId, items) ->
                item { Text("List ID: $listId", style = MaterialTheme.typography.titleLarge) }
                items(items) { item ->
                    Text("${item.name}", modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}
