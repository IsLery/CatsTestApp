package com.islery.catstestapp.presentation.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.islery.catstestapp.data.CatsRepository
import com.islery.catstestapp.presentation.cats_list.CatsListViewModel
import com.islery.catstestapp.presentation.favourites.FavouritesViewModel

@Suppress("UNCHECKED_CAST")
class CatsViewModelFactory(private val repository: CatsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CatsListViewModel::class.java) -> {
                CatsListViewModel(
                    repository
                ) as T
            }
            modelClass.isAssignableFrom(FavouritesViewModel::class.java) -> {
                FavouritesViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}