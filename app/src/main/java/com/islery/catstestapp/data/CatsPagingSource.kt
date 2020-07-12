package com.islery.catstestapp.data

import android.util.Log
import androidx.paging.PagingSource
import com.islery.catstestapp.data.db.CatDao
import com.islery.catstestapp.data.db.CatDatabase
import com.islery.catstestapp.data.model.Cat
import com.islery.catstestapp.network.CatsApiService
import retrofit2.HttpException
import java.io.IOException

class CatsPagingSource (private val service: CatsApiService, private val db: CatDatabase) : PagingSource<Int, Cat>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cat> {
        val pageCurr = params.key ?: 0
        return try {
            val response = service.getImages(page = pageCurr,limit = params.loadSize)
            val favsIds = db.catDao().contains(response.map { cat -> cat.id })
            if (favsIds.isNotEmpty()){
            response.forEach{
                if(favsIds.contains(it.id)){it.isFav = true}
            }}

            LoadResult.Page(
                data = response,
                prevKey = if (pageCurr == 0) null else pageCurr - 1,
                nextKey = if (response.isEmpty()) null else pageCurr + 1

            )
        } catch (exception: IOException) {
            Log.d("MY_TAG", "load: ${exception.message}")
        return LoadResult.Error(exception)
    } catch (exception: HttpException) {
            Log.d("MY_TAG", "load: ${exception.message}")
        return LoadResult.Error(exception)
    }
    }
}