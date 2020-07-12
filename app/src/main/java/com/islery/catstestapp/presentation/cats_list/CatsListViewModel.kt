package com.islery.catstestapp.presentation.cats_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.islery.catstestapp.data.model.Cat
import com.islery.catstestapp.data.CatsRepository
import com.islery.catstestapp.presentation.favourites.FavouritesViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CatsViewModelFactory(private val repository: CatsRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CatsListViewModel::class.java) -> {
                CatsListViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FavouritesViewModel::class.java) -> {
                FavouritesViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

class CatsListViewModel(private val repository: CatsRepository) : ViewModel() {

    var catsData: Flow<PagingData<Cat>>? = getCats()


    fun getCats(): Flow<PagingData<Cat>> {
        val lastData = catsData
        if (lastData != null) {
            return lastData
        }

            val newResult = repository.getCatsImagesStream()
                .cachedIn(viewModelScope)
            catsData = newResult
            return newResult


    }



    fun handleFavourites(cat: Cat, isChecked: Boolean){

        if (isChecked){
            addToFavourites(cat)
        }else{
            deleteFromFavourites(cat)
        }
    }

    private fun deleteFromFavourites(cat : Cat){
        viewModelScope.launch {
            repository.deleteFromFav(cat)
        }
    }

    private fun addToFavourites(cat : Cat){
        viewModelScope.launch {
            repository.addToFav(cat)
        }
    }
}