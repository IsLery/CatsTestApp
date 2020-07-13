package com.islery.catstestapp.data

import androidx.paging.PagingSource
import com.islery.catstestapp.data.model.Cat
import com.islery.catstestapp.network.CatsApiService
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_INDEX = 0

class CatsPagingSource(private val service: CatsApiService) : PagingSource<Int, Cat>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cat> {
        val pageCurr = params.key ?: STARTING_INDEX
        return try {
            val response = service.getImages(page = pageCurr, limit = params.loadSize)
            LoadResult.Page(
                data = response,
                prevKey = if (pageCurr == STARTING_INDEX) null else pageCurr - 1,
                nextKey = if (response.isEmpty()) null else pageCurr + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}