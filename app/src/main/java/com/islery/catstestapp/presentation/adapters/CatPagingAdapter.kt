package com.islery.catstestapp.presentation.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.islery.catstestapp.data.model.Cat

//adapter for images from network
class CatPagingAdapter(
    private val favsListener: (cat: Cat) -> Unit,
    private val downloadListener: (url: String) -> Unit
) :
    PagingDataAdapter<Cat, CatViewHolder>(
        CAT_COMPARATOR
    ) {


    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        return CatViewHolder.create(parent, favsListener, downloadListener)
    }

}






