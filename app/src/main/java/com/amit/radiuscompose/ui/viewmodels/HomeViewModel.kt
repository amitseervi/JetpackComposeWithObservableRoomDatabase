package com.amit.radiuscompose.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amit.radiuscompose.model.repo.FacilityFilterRepository
import com.amit.radiuscompose.model.repo.FacilityUserPreferenceRepository
import com.amit.radiuscompose.model.response.FacilityItem
import com.amit.radiuscompose.model.response.RepositoryStatus
import com.amit.radiuscompose.ui.viewmodels.states.ui.FacilityCard
import com.amit.radiuscompose.ui.viewmodels.states.ui.FacilityOptionUiChip
import com.amit.radiuscompose.ui.viewmodels.states.ui.HomeUiState
import com.amit.radiuscompose.ui.viewmodels.states.vm.HomeViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val facilityRepository: FacilityFilterRepository,
    private val facilityUserPreferenceRepository: FacilityUserPreferenceRepository
) : ViewModel() {

    private val mViewModelState: MutableStateFlow<HomeViewModelState> =
        MutableStateFlow(HomeViewModelState())

    private fun getFacilityCards(
        facilities: List<FacilityItem>,
        disabledOption: Map<String, List<String>>,
        selection: Map<String, String>
    ): List<FacilityCard> {
        return facilities.map { facilityItem ->
            val selectedOption = selection[facilityItem.facilityId]
            FacilityCard(
                facilityItem.name,
                facilityItem.facilityId,
                options = facilityItem.options.map { facilityItemOption ->
                    val selected = selectedOption == facilityItemOption.id
                    val disabled =
                        disabledOption[facilityItem.facilityId]?.contains(facilityItemOption.id) == true
                    FacilityOptionUiChip(
                        selected = selected,
                        name = facilityItemOption.name,
                        icon = facilityItemOption.icon,
                        id = facilityItemOption.id,
                        disabled = disabled,
                    )
                }
            )
        }
    }

    private fun HomeViewModelState.toUiState(): HomeUiState {
        return if (this.facilities.isEmpty()) {
            HomeUiState.Empty(error = this.error, loading = this.loading)
        } else {
            val disabledOptions = mutableListOf<Pair<String, String>>()
            val selectedOptions = mutableMapOf<String, String>()
            userPreference.forEach {
                selectedOptions[it.first] = it.second
            }
            exclusion.forEach { exclusionRule ->
                val haveExcludedSelection =
                    exclusionRule.find { selectedOptions[it.facilityId] == it.optionId }
                if (haveExcludedSelection != null) {
                    exclusionRule.forEach {
                        if (it != haveExcludedSelection) {
                            disabledOptions.add(it.facilityId to it.optionId)
                            if (selectedOptions[it.facilityId] == it.optionId) {
                                selectedOptions.remove(it.facilityId)
                            }
                        }
                    }
                }
            }
            val disabledOption = disabledOptions.groupBy({ it.first }, { it.second })
            HomeUiState.HasFacilities(
                error = this.error,
                loading = this.loading,
                facilities = getFacilityCards(this.facilities, disabledOption, selectedOptions)
            )
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
            facilityRepository.status.collectLatest { repoResponse ->
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
                                loading = false,
                                error = null,
                                exclusion = repoResponse.data.exclusion,
                                facilities = repoResponse.data.facilities
                            )
                        }

                        else -> {
                            currentState
                        }
                    }
                }
            }
        }
        viewModelScope.launch {
            facilityUserPreferenceRepository.status.collectLatest { userPreference ->
                mViewModelState.update { currentState ->
                    currentState.copy(userPreference = userPreference.map { it.facilityId to it.optionId })
                }
            }
        }
        onRefresh()
    }

    fun toggleFacilityOption(facilityId: String, optionId: String, enable: Boolean) {
        viewModelScope.launch {
            facilityUserPreferenceRepository.saveFacilityOptionSelection(
                facilityId,
                optionId,
                enable
            )
        }
    }

    fun onRefresh() {
        viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, exception ->
            Timber.e(exception)
        }) {
            facilityRepository.refresh()
        }
    }
}