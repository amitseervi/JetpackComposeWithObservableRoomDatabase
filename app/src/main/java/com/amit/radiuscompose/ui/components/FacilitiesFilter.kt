package com.amit.radiuscompose.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amit.radiuscompose.R
import com.amit.radiuscompose.ui.viewmodels.states.ui.FacilityCard
import com.amit.radiuscompose.ui.viewmodels.states.ui.FacilityOptionUiChip
import com.amit.radiuscompose.ui.viewmodels.states.ui.HomeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacilitiesFilterUiComponent(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    onFilterItemSelect: (categoryId: String, optionId: String, enable: Boolean) -> Unit
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

                is HomeUiState.HasFacilities -> {
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
    state: HomeUiState.HasFacilities,
    onFilterItemSelect: (categoryId: String, optionId: String, enable: Boolean) -> Unit
) {
    val maxWidthModifier = Modifier.fillMaxWidth()
    Column(modifier = modifier.fillMaxSize().padding(20.dp)) {
        if (state.loading) {
            LinearProgressIndicator(modifier = maxWidthModifier)
        } else if (state.error != null) {
            Text(text = state.error, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(
                state.facilities.size,
                contentType = { "filter_item" },
                key = { index -> state.facilities[index].id }) { index ->
                FacilityCardItem(
                    modifier = maxWidthModifier,
                    state.facilities[index],
                    onFilterItemSelect = onFilterItemSelect
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun FacilityCardItem(
    modifier: Modifier = Modifier,
    facilityCard: FacilityCard,
    onFilterItemSelect: (categoryId: String, optionId: String, enable: Boolean) -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = facilityCard.title,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                facilityCard.options.forEach { facilityOptionItem ->
                    FacilityOptionChip(
                        modifier = Modifier,
                        selected = facilityOptionItem.selected,
                        onClick = {
                            onFilterItemSelect(
                                facilityCard.id,
                                facilityOptionItem.id,
                                !facilityOptionItem.selected
                            )
                        },
                        name = facilityOptionItem.name,
                        icon = facilityOptionItem.icon,
                        disabled = facilityOptionItem.disabled
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacilityOptionChip(
    modifier: Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    name: String,
    icon: String,
    disabled: Boolean
) {
    ElevatedFilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                OptionIcon(icon = icon, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = name)
            }
        },
        modifier = modifier,
        enabled = !disabled
    )
}

@Composable
fun OptionIcon(modifier: Modifier = Modifier, icon: String) {
    val resourceId = iconNameToResource(icon) ?: return
    Icon(painterResource(id = resourceId), contentDescription = icon, modifier = modifier)
}

@Composable
@Preview
fun PreviewFacilityCard(modifier: Modifier = Modifier) {
    FacilityCardItem(
        modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        FacilityCard(
            title = "Property type",
            id = "2",
            options = listOf(
                FacilityOptionUiChip(
                    false,
                    "1 to 2",
                    "land",
                    "2",
                    false
                ),
                FacilityOptionUiChip(
                    true,
                    "Condo",
                    "condo",
                    "6",
                    false
                ),
                FacilityOptionUiChip(
                    false,
                    "Sweet Home",
                    "apartment",
                    "9",
                    false
                ),

                )
        )
    ) { x, y, z ->

    }
}

@DrawableRes
private fun iconNameToResource(name: String): Int? {
    return when (name.lowercase()) {
        "boat" -> R.drawable.boat
        "land" -> R.drawable.land
        "condo" -> R.drawable.condo
        "no-room" -> R.drawable.no_room
        "apartment" -> R.drawable.apartment
        "garden" -> R.drawable.garden
        "swimming" -> R.drawable.swimming
        "rooms" -> R.drawable.rooms
        "garage" -> R.drawable.garage
        else -> null
    }
}