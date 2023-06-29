package com.amit.radiuscompose.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.amit.radiuscompose.model.repo.FacilityFilterRepository
import com.amit.radiuscompose.model.response.FacilitiesData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FacilityFilterRepository) :
    ViewModel() {

    init {

    }
}