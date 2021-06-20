package com.kotlin.mvvm.di.modules

import android.content.Context
import androidx.room.Room
import com.kotlin.mvvm.BuildConfig
import com.kotlin.mvvm.repository.db.TodoDao
import com.kotlin.mvvm.repository.api.ApiServices
import com.kotlin.mvvm.repository.api.network.LiveDataCallAdapterFactoryForRetrofit
import com.kotlin.mvvm.repository.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Developed by Waheed on 20,June,2021
 * App module
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * Provides ApiServices client for Retrofit
     */
    @Singleton
    @Provides
    fun provideNewsService(): ApiServices {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactoryForRetrofit())
            .build()
            .create(ApiServices::class.java)
    }


    /**
     * Provides AppDatabase (Room DB)
     */
    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "todos-db")
            .fallbackToDestructiveMigration().build()


    /**
     * Provides TodosDao an object to access Todos table from Database
     */
    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase): TodoDao = db.todoDao()
}
