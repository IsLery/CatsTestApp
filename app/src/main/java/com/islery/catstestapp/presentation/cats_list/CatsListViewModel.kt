package com.islery.catstestapp.presentation.cats_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.islery.catstestapp.data.CatsRepository
import com.islery.catstestapp.data.model.Cat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class CatsListViewModel(private val repository: CatsRepository) : ViewModel() {

    private var catsData: Flow<PagingData<Cat>>? = null

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


    fun addToFavourites(cat: Cat): Boolean {
        return runBlocking {
            return@runBlocking withContext(Dispatchers.Default) {
                repository.addToFav(
                    cat
                )
            }
        }
    }
}