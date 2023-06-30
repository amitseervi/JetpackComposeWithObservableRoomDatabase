package com.amit.radiuscompose.di

import android.content.Context
import androidx.room.Room
import com.amit.radiuscompose.model.db.RadiusDB
import com.amit.radiuscompose.model.network.interceptor.NetworkCacheInterceptor
import com.amit.radiuscompose.model.network.service.RadiusService
import com.amit.radiuscompose.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppProvidingModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RadiusDB {
        return Room.databaseBuilder(
            context, RadiusDB::class.java, RadiusDB.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder().cache(
            Cache(
                directory = File(context.cacheDir, "http_cache"),
                maxSize = 10L * 1024L * 1024L // 10 MiB Cache
            )
        ).addInterceptor(NetworkCacheInterceptor(context))
            .callTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRadiusService(okHttpClient: OkHttpClient): RadiusService {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(Constants.BASE_API)
            .client(okHttpClient).build()
            .create(RadiusService::class.java)
    }
}