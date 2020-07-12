package com.islery.catstestapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.islery.catstestapp.data.model.Cat
import com.islery.catstestapp.data.db.CatDatabase
import com.islery.catstestapp.network.CatsApiService
import kotlinx.coroutines.flow.Flow

class CatsRepository(private val service: CatsApiService, private val database: CatDatabase) {

    private var _deletedFromFavs: MutableList<Cat> = mutableListOf()
    private var deletedFromFavs: List<Cat> = _deletedFromFavs

    fun getCatsImagesStream(): Flow<PagingData<Cat>>{

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
               enablePlaceholders = false
            )){
                CatsPagingSource(service,database)
            }
            .flow
    }

    suspend fun addToFav(cat: Cat){
        cat.isFav = true
        database.catDao().insert(cat)
    }

    suspend fun deleteFromFav(cat: Cat, fromFavFragment: Boolean = false){
        if (fromFavFragment){
            _deletedFromFavs.add(cat)
        }
        database.catDao().delete(cat)
    }

    fun updateThatWereDeleted(): List<Cat>{
        val list = deletedFromFavs
        _deletedFromFavs.clear()
        return list
    }

    fun getFavCats() = database.catDao().getAll()

    companion object{
        private const val NETWORK_PAGE_SIZE = 7
    }
}