package com.islery.catstestapp.presentation.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.islery.catstestapp.data.model.Cat

class FavAdapter(
    private val listener: (cat: Cat) -> Unit,
    private val downloadListener: (url: String) -> Unit
) : ListAdapter<Cat, CatViewHolder>(
    CAT_COMPARATOR
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        return CatViewHolder.create(parent, listener, downloadListener)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}