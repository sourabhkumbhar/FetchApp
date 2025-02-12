package com.interview.fetchapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.interview.fetchapp.FetchViewModel
import com.interview.fetchapp.data.ListItem
import com.interview.fetchapp.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FetchListScreenCollapsable(viewModel: FetchViewModel = hiltViewModel()) {
    val state by viewModel.items.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchListItems()
    }

    Box {
        when (state) {
            is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            is Resource.Success -> {
                val groupedItems = remember((state as Resource.Success<List<ListItem>>).data) {
                    (state as Resource.Success<List<ListItem>>).data!!
                        .sortedWith(compareBy({ it.listId }, { it.name }))
                        .groupBy { it.listId }  // Group by listId
                }

                CollapsibleLazyColumn(groupedItems)
            }

            is Resource.Error -> Text(
                text = "Error: ${(state as Resource.Error).message}",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun CollapsibleLazyColumn(groupedItems: Map<Int, List<ListItem>>) {
    val expandedStates = remember { mutableStateMapOf<Int, Boolean>() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        groupedItems.forEach { (listId, items) ->
            // Collapsible Group Header
            item {
                val isExpanded = expandedStates[listId] ?: false
                CollapsibleHeader(listId, isExpanded) { expandedStates[listId] = !isExpanded }
            }

            if (expandedStates[listId] == true) {
                // Display Items only when expanded
                items(items, key = { it.id }) { item ->
                    ListItemRowSimpleCard(item)
                }
            }
        }
    }
}

@Composable
fun CollapsibleHeader(listId: Int, isExpanded: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp)) // Rounded corners
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary), // Blue background
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp) // Adds some depth
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "List ID: $listId",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary // White text
                ),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = "Expand/Collapse",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun ListItemRowSimple(item: ListItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp) // Smaller padding
    ) {
        Text(
            text = item.name!!,
            modifier = Modifier.padding(vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        Divider(color = Color.Gray, thickness = 0.5.dp) // Thin line separator
    }
}

@Composable
fun ListItemRowSimpleCard(item: ListItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp)), // Slightly rounded corners
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Soft shadow effect
        colors = CardDefaults.cardColors(containerColor = Color.LightGray) // Light gray background
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