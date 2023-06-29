package com.amit.radiuscompose.di

import com.amit.radiuscompose.model.repo.FacilityFilterRepository
import com.amit.radiuscompose.model.repo.FacilityUserPreferenceRepository
import com.amit.radiuscompose.model.repo.impl.FacilityFilterRepositoryImpl
import com.amit.radiuscompose.model.repo.impl.FacilityUserPreferenceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppBindingModule {

    @Singleton
    @Binds
    fun provideFacilityRepository(facilityFilterRepository: FacilityFilterRepositoryImpl): FacilityFilterRepository

    @Singleton
    @Binds
    fun provideFacilityUserPreferenceRepository(facilityUserPreferenceRepository: FacilityUserPreferenceRepositoryImpl): FacilityUserPreferenceRepository
}