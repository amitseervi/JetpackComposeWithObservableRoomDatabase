package com.amit.radiuscompose.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amit.radiuscompose.model.repo.FacilityFilterRepository
import com.amit.radiuscompose.model.response.FacilitiesData
import com.amit.radiuscompose.model.response.RepositoryStatus
import com.amit.radiuscompose.ui.viewmodels.states.ui.FilterUiItem
import com.amit.radiuscompose.ui.viewmodels.states.ui.FilterUiOption
import com.amit.radiuscompose.ui.viewmodels.states.ui.HomeUiState
import com.amit.radiuscompose.ui.viewmodels.states.vm.HomeViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FacilityFilterRepository) :
    ViewModel() {

    private val mViewModelState: MutableStateFlow<HomeViewModelState> =
        MutableStateFlow(HomeViewModelState())


    private fun getFacilityUiData(
        facilitiesData: FacilitiesData,
        selection: Map<String, String>
    ): List<FilterUiItem> {
        /*TODO optimize this algorithm*/

        val result = mutableListOf<FilterUiItem>()
        val disableOptions = mutableMapOf<String, MutableList<String>>()
        selection.entries.forEach { selectedEntry ->
            facilitiesData.exclusion.forEach { exclusionRule ->
                var applyRule = false
                exclusionRule.forEach { exclusionItem ->
                    if (exclusionItem.facilityId == selectedEntry.key && exclusionItem.optionId == selectedEntry.value) {
                        applyRule = true
                    }
                }
                if (applyRule) {
                    exclusionRule.forEach {
                        if (it.facilityId != selectedEntry.key && it.optionId != selectedEntry.value) {
                            val currentDisabledOptionForFacility =
                                disableOptions[it.facilityId] ?: mutableListOf()
                            currentDisabledOptionForFacility.add(it.optionId)
                            disableOptions[it.facilityId] = currentDisabledOptionForFacility
                        }
                    }
                }
            }
        }
        facilitiesData.facilities.forEach { facilityItem ->
            val disabledOptionsForThisFilter =
                disableOptions[facilityItem.facilityId].orEmpty().toHashSet()
            val filterOptionItems = mutableListOf<FilterUiOption>()
            val defaultOption = FilterUiOption(
                "",
                disabled = false,
                text = "Select",
                icon = "default_option",
                selected = selection[facilityItem.facilityId].isNullOrEmpty()
            )
            filterOptionItems.add(defaultOption)
            var selectedOptionIndex: Int = 0

            facilityItem.options.forEachIndexed { index, option ->
                val disabled = disabledOptionsForThisFilter.contains(option.id)
                val selected = selection.get(facilityItem.facilityId) == option.id && !disabled
                if (selected) {
                    selectedOptionIndex = index + 1
                }
                filterOptionItems.add(
                    FilterUiOption(
                        option.id,
                        disabled = disabled,
                        text = option.name,
                        icon = option.icon,
                        selected = selected
                    )
                )
            }
            val item = FilterUiItem(
                facilityItem.facilityId,
                facilityItem.name,
                filterOptionItems.toList(),
                selectedOptionIndex
            )
            result.add(item)
        }
        return result
    }

    private fun HomeViewModelState.toUiState(): HomeUiState {
        return if (this.facilityData != null) {
            val filters = getFacilityUiData(this.facilityData, this.selectedOptionsForFacility)
            if (this.loading) {
                HomeUiState.HasFilters(
                    loading = true,
                    error = null,
                    filters = filters
                )
            } else if (this.error != null) {
                HomeUiState.HasFilters(
                    loading = false,
                    error = this.error,
                    filters = filters
                )
            } else {
                HomeUiState.HasFilters(
                    loading = false,
                    error = null,
                    filters = filters
                )
            }
        } else {
            if (this.loading) {
                HomeUiState.Empty(loading = true, error = null)
            } else {
                HomeUiState.Empty(
                    loading = false,
                    error = this.error ?: "No facility selection found"
                )
            }
        }
    }

    val uiState: StateFlow<HomeUiState>
        get() = mViewModelState.map {
            it.toUiState()
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly, HomeUiState.Empty()
        )

    init {
        viewModelScope.launch {
            repository.status.collectLatest { repoResponse ->
                mViewModelState.update { currentState ->
                    when (repoResponse) {
                        is RepositoryStatus.Error -> {
                            currentState.copy(loading = false, error = repoResponse.message)
                        }

                        is RepositoryStatus.Loading -> {
                            currentState.copy(loading = true, error = null)
                        }

                        is RepositoryStatus.Success -> {
                            currentState.copy(
                                loading = false, error = null, facilityData = repoResponse.data
                            )
                        }
                    }
                }
            }
            repository.refresh()
        }
    }

    fun selectFacilityOption(facilityId: String, optionId: String) {
        mViewModelState.update { currentState ->
            val currentSelection = currentState.selectedOptionsForFacility.toMutableMap()
            currentSelection[facilityId] = optionId
            currentState.copy(selectedOptionsForFacility = currentSelection.toMap())
        }
    }
}