package com.islery.catstestapp.presentation.cats_list

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.islery.catstestapp.Injection
import com.islery.catstestapp.databinding.FragmentCatsListBinding
import com.islery.catstestapp.presentation.CatsFragment
import com.islery.catstestapp.presentation.adapters.CatPagingAdapter
import com.islery.catstestapp.presentation.downloadImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*


/**
 * A simple [Fragment] subclass.
 * Use the [CatsListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CatsListFragment : CatsFragment() {

    private var _binding: FragmentCatsListBinding? = null
    private val binding get() = _binding!!


    private val viewModel by viewModels<CatsListViewModel>(
        factoryProducer = { Injection.provideViewModelFactory(requireContext().applicationContext) }
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCatsListBinding.inflate(inflater, container, false)
        val adapter =
            CatPagingAdapter({ cat, isChecked ->
                viewModel.handleFavourites(cat, isChecked)
            }, { downloadImageToStorage(it) })
        binding.catsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.catsRv.adapter = adapter
        lifecycleScope.launch {
            viewModel.getCats().collectLatest {
                adapter.submitData(it)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}