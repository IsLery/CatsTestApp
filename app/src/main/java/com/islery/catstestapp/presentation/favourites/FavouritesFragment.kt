package com.islery.catstestapp.presentation.favourites

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.islery.catstestapp.Injection
import com.islery.catstestapp.databinding.FragmentFavouritesBinding
import com.islery.catstestapp.presentation.CatsFragment
import com.islery.catstestapp.presentation.adapters.FavAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException



/**
 * A simple [Fragment] subclass.
 * Use the [FavouritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouritesFragment : CatsFragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FavouritesViewModel>(
        factoryProducer = { Injection.provideViewModelFactory(requireContext().applicationContext) }
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        val adapter = FavAdapter({ cat, _ ->
            viewModel.deleteCat(cat)
        }, { downloadImageToStorage(it) })

        binding.favList.adapter = adapter
        binding.favList.layoutManager = LinearLayoutManager(requireContext())

        viewModel.catData.observe(viewLifecycleOwner, Observer { list ->
            adapter.submitList(list)
        })
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}