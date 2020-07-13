package com.islery.catstestapp.presentation.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.islery.catstestapp.data.CatsRepository
import com.islery.catstestapp.data.model.Cat
import kotlinx.coroutines.launch

class FavouritesViewModel(private val repository: CatsRepository) : ViewModel(){

    val catData: LiveData<List<Cat>> = repository.getFavCats()

    fun deleteCat(cat: Cat){
        viewModelScope.launch {
            repository.deleteFromFav(cat)
        }
    }
}