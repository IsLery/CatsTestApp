package com.islery.catstestapp.presentation.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.islery.catstestapp.data.model.Cat
import com.islery.catstestapp.databinding.CatsRowBinding


class CatViewHolder(
    private val binding: CatsRowBinding,
    val listener: (cat: Cat, isChecked: Boolean) -> Unit,
    val longClickListener:  (url: String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cat: Cat) {
        cat.url.let {
            Glide.with(binding.root.context)
                .load(cat.url)
                .centerCrop()
                .listener(reqListener)
                .into(binding.imageView)
        }
        binding.toFavsBtn.isChecked = cat.isFav
        binding.toFavsBtn.setOnCheckedChangeListener { _, isChecked ->
            listener(cat, isChecked)
        }

        binding.downloadBtn.setOnClickListener {
           longClickListener(cat.url)
        }


    }


    private val reqListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            binding.progressImg.isVisible = false
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            binding.progressImg.isVisible = false
            return false
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: (cat: Cat, isChecked: Boolean) -> Unit,
            longClickListener: (url: String) -> Unit
        ): CatViewHolder {
            val binding = CatsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CatViewHolder(
                binding,
                listener,
                longClickListener
            )
        }

    }

}

val CAT_COMPARATOR = object : DiffUtil.ItemCallback<Cat>() {
    override fun areItemsTheSame(oldItem: Cat, newItem: Cat): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Cat, newItem: Cat): Boolean {
        return oldItem == newItem
    }

}