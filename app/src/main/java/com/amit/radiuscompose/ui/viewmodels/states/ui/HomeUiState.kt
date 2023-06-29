package com.amit.radiuscompose.ui.viewmodels.states.ui

sealed interface HomeUiState {
    val error: String?
    val loading: Boolean

    data class HasFilters(
        val filters: List<FilterUiItem>,
        override val error: String?,
        override val loading: Boolean
    ) : HomeUiState

    data class Empty(override val error: String? = null, override val loading: Boolean = false) :
        HomeUiState
}

data class FilterUiItem(
    val id: String,
    val name: String,
    val options: List<FilterUiOption>,
    val selectedOptionIndex: Int
)

data class FilterUiOption(
    val id: String,
    val disabled: Boolean,
    val text: String,
    val icon: String,
    val selected: Boolean
)