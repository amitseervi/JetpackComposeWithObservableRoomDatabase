package com.amit.radiuscompose.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amit.radiuscompose.ui.viewmodels.states.ui.FilterUiItem
import com.amit.radiuscompose.ui.viewmodels.states.ui.HomeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacilitiesFilterUiComponent(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    onFilterItemSelect: (categoryId: String, optionId: String) -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text(text = "Facility filter Sample")
            })
        }) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            when (state) {
                is HomeUiState.Empty -> {
                    EmptyScreen(
                        modifier = Modifier.fillMaxSize(),
                        loading = state.loading,
                        error = state.error
                    )
                }

                is HomeUiState.HasFilters -> {
                    HasFilterScreen(modifier.fillMaxSize(), state, onFilterItemSelect)
                }
            }
        }
    }
}

@Composable
private fun EmptyScreen(modifier: Modifier = Modifier, loading: Boolean, error: String?) {
    Box(modifier = modifier) {
        if (loading) {
            Row(modifier = Modifier.align(Alignment.Center)) {
                CircularProgressIndicator(modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Loading...")
            }
        } else if (error != null) {
            Text(
                text = "Error : ${error}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun HasFilterScreen(
    modifier: Modifier = Modifier,
    state: HomeUiState.HasFilters,
    onFilterItemSelect: (categoryId: String, optionId: String) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        if (state.loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else if (state.error != null) {
            Text(text = state.error, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        LazyColumn {
            items(
                state.filters.size,
                contentType = { "filter_item" },
                key = { index -> state.filters[index].id }) { index ->
                FilterItem(
                    modifier = Modifier.fillMaxWidth(),
                    state.filters[index],
                    onFilterItemSelect = onFilterItemSelect
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterItem(
    modifier: Modifier = Modifier,
    filterUiItem: FilterUiItem,
    onFilterItemSelect: (categoryId: String, optionId: String) -> Unit
) {
    var menuExpanded: Boolean by remember {
        mutableStateOf(false)
    }
    val selectedOption = filterUiItem.options[filterUiItem.selectedOptionIndex]
    val selectedText = selectedOption.text
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Property Type")
        ExposedDropdownMenuBox(expanded = menuExpanded, onExpandedChange = {
            menuExpanded = it
        }) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpanded)
                }
            )
            ExposedDropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }) {
                filterUiItem.options.forEach {
                    DropdownMenuItem(text = {
                        Text(text = it.text)
                    }, onClick = {
                        menuExpanded = false
                        onFilterItemSelect(filterUiItem.id, it.id)
                    })
                }
            }
        }

    }
}