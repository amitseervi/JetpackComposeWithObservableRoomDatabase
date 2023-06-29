package com.amit.radiuscompose.ui.viewmodels.states.ui

sealed interface HomeUiState {
    val error: String?
    val loading: Boolean

    data class HasFacilities(
        val facilities: List<FacilityCard>,
        override val error: String?,
        override val loading: Boolean
    ) : HomeUiState

    data class Empty(override val error: String? = null, override val loading: Boolean = false) :
        HomeUiState
}

data class FacilityCard(
    val title: String,
    val id: String,
    val options: List<FacilityOptionUiChip>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FacilityCard

        if (id != other.id) return false
        return options == other.options
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + options.hashCode()
        return result
    }
}

data class FacilityOptionUiChip(
    val selected: Boolean,
    val name: String,
    val icon: String,
    val id: String,
    val disabled: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FacilityOptionUiChip

        if (selected != other.selected) return false
        if (id != other.id) return false
        return disabled == other.disabled
    }

    override fun hashCode(): Int {
        var result = selected.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + disabled.hashCode()
        return result
    }
}