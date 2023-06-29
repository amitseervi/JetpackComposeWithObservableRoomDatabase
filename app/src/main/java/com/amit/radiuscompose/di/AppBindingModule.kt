package com.amit.radiuscompose.di

import com.amit.radiuscompose.model.repo.FacilityFilterRepository
import com.amit.radiuscompose.model.repo.impl.FacilityFilterRepositoryImpl
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
}