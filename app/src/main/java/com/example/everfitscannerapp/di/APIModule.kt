package com.example.everfitscannerapp.di

import com.example.everfitscannerapp.data.api.ScanServiceAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): ScanServiceAPI {
        return retrofit.create(ScanServiceAPI::class.java)
    }

}