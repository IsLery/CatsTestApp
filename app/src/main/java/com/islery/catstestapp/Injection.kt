package com.islery.catstestapp

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.islery.catstestapp.data.CatsRepository
import com.islery.catstestapp.data.db.CatDatabase
import com.islery.catstestapp.network.CatsApiServiceImpl
import com.islery.catstestapp.presentation.cats_list.CatsViewModelFactory

object Injection {

    private fun provideRepository(context: Context): CatsRepository{
        return CatsRepository(CatsApiServiceImpl.apiService, CatDatabase.getInstance(context))
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory{
        return CatsViewModelFactory(provideRepository(context))
    }
}