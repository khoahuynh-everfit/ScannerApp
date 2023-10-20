package com.example.everfitscannerapp.di

import com.example.everfitscannerapp.data.api.ScanServiceAPI
import com.example.everfitscannerapp.data.repository.ScanRepositoryImpl
import com.example.everfitscannerapp.domain.repository.ScanRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideScanRepository(scanServiceAPI: ScanServiceAPI): ScanRepository {
        return ScanRepositoryImpl(scanServiceAPI)
    }

}