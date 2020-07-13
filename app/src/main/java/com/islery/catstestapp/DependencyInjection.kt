package com.islery.catstestapp

import android.content.Context
import androidx.room.Room
import com.islery.catstestapp.data.CatsRepository
import com.islery.catstestapp.data.db.CatDatabase
import com.islery.catstestapp.data.db.DATABASE_NAME
import com.islery.catstestapp.network.BASE_URL
import com.islery.catstestapp.network.CatsApiService
import com.islery.catstestapp.presentation.cats_list.CatsListFragment
import com.islery.catstestapp.presentation.utils.CatsViewModelFactory
import com.islery.catstestapp.presentation.favourites.FavouritesFragment
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class AppModule(private val context: Context){

    @Provides
    @Singleton
    fun provideContext() = context

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): CatsApiService = retrofit.create(CatsApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(context: Context) = Room.databaseBuilder(context, CatDatabase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun providesRepository(apiService: CatsApiService, database: CatDatabase) = CatsRepository(apiService,database)

    @Provides
    @Singleton
    fun providesViewModelFactory(repository: CatsRepository) =
        CatsViewModelFactory(repository)
}

//Injecting dependencies to fragments
@Component(modules = [AppModule::class])
@Singleton
interface AppComponent{
    fun inject(fragment: CatsListFragment)
    fun inject(fragment: FavouritesFragment)
}
