package com.islery.catstestapp.presentation.cats_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.islery.catstestapp.App
import com.islery.catstestapp.R
import com.islery.catstestapp.data.model.Cat
import com.islery.catstestapp.databinding.FragmentCatsListBinding
import com.islery.catstestapp.presentation.adapters.CatPagingAdapter
import com.islery.catstestapp.presentation.utils.CatsFragment
import com.islery.catstestapp.presentation.utils.CatsViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Fragment for list of cats from network
 * uses PagingDataAdapter and paging 3 library
 * user can add image to favourites
 */
class CatsListFragment : CatsFragment() {

    init {
        App.appComponent.inject(this)
    }

    private var _binding: FragmentCatsListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: CatsViewModelFactory

    private val viewModel by viewModels<CatsListViewModel>(
        factoryProducer = { factory }
    )

    private val adapter =
        CatPagingAdapter({ cat ->
            addToFavourites(cat)
        }, { downloadImageToStorage(it) })


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCatsListBinding.inflate(inflater, container, false)
        binding.catsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.catsRv.adapter = adapter
        binding.catsRv.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        //submitting new data to adapter from Flow
        lifecycleScope.launch {
            viewModel.catsData.collectLatest {
                adapter.submitData(it)
            }
        }

        binding.retryBtn.setOnClickListener {
            adapter.retry()
        }
        initAdapter()
        return binding.root
    }

    private fun initAdapter() {
        adapter.addLoadStateListener { loadState ->
            binding.catsRv.isVisible = loadState.refresh is LoadState.NotLoading
            binding.progressBar.isVisible = loadState.refresh is LoadState.Loading

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    requireContext(),
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    /*
    Display whether favourites already contain this image
     */
    private fun addToFavourites(cat: Cat) {
        if (viewModel.addToFavourites(cat)) {
            Toast.makeText(requireContext(), R.string.fav_success, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), R.string.fav_error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}