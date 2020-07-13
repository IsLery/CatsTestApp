package com.islery.catstestapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.islery.catstestapp.data.db.CatDatabase
import com.islery.catstestapp.data.model.Cat
import com.islery.catstestapp.network.CatsApiService
import kotlinx.coroutines.flow.Flow

class CatsRepository(private val service: CatsApiService, private val database: CatDatabase) {


    fun getCatsImagesStream(): Flow<PagingData<Cat>> {

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            )
        ) {
            CatsPagingSource(service)
        }
            .flow
    }

    suspend fun addToFav(cat: Cat): Boolean {
        cat.isFav = true
        return database.catDao().insert(cat) >= 0
    }

    suspend fun deleteFromFav(cat: Cat) {
        database.catDao().delete(cat)
    }


    fun getFavCats() = database.catDao().getAll()

    companion object {
        private const val NETWORK_PAGE_SIZE = 9
    }
}