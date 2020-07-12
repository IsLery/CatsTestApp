package com.islery.catstestapp.presentation.adapters

import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.islery.catstestapp.data.model.Cat

class CatPagingAdapter(val listener: (cat: Cat, isChecked: Boolean) -> Unit, val longClickListener: (url: String) -> Unit) :
    PagingDataAdapter<Cat, CatViewHolder>(
        CAT_COMPARATOR
    ) {



    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        return CatViewHolder.create(parent, listener, longClickListener)
    }

}






