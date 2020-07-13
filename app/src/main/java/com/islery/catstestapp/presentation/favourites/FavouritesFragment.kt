package com.islery.catstestapp.presentation.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.islery.catstestapp.App
import com.islery.catstestapp.databinding.FragmentFavouritesBinding
import com.islery.catstestapp.presentation.utils.CatsFragment
import com.islery.catstestapp.presentation.adapters.FavAdapter
import com.islery.catstestapp.presentation.utils.CatsViewModelFactory
import javax.inject.Inject


/**
 *Fragment for displaying favourite cat images
 * uses ListAdapter
 * user can delete image from database
 */
class FavouritesFragment : CatsFragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    init {
        App.appComponent.inject(this)
    }

    @Inject
    lateinit var factory: CatsViewModelFactory

    private val viewModel by viewModels<FavouritesViewModel>(
        factoryProducer = { factory }
    )


    private val adapter = FavAdapter({ cat ->
        viewModel.deleteCat(cat)
    }, { downloadImageToStorage(it) })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        binding.favList.adapter = adapter
        binding.favList.layoutManager = LinearLayoutManager(requireContext())
        binding.favList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.catData.observe(viewLifecycleOwner, Observer { list ->
            adapter.submitList(list)
            binding.emptyFavs.isVisible = list.isEmpty()
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}