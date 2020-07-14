package com.islery.catstestapp.presentation.cats_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.islery.catstestapp.R
import com.islery.catstestapp.data.CatsRepository
import com.islery.catstestapp.data.model.Cat
import com.islery.catstestapp.presentation.utils.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class CatsListViewModel(private val repository: CatsRepository) : ViewModel() {

    val catsData: Flow<PagingData<Cat>> = repository.getCatsImagesStream()
        .cachedIn(viewModelScope)

    private val _message = MutableLiveData<Event<Int>>()
    val message: LiveData<Event<Int>>
        get() = _message

    /*
        Will show toast as a result: whether favourites already contain this image
    */
    fun addCatToFavourites(cat: Cat) {
        viewModelScope.launch {
            if (repository.addToFav(cat)) {
                _message.value =
                    Event(R.string.fav_success)
            } else {
                _message.value =
                    Event(R.string.fav_error)
            }
        }
    }
}