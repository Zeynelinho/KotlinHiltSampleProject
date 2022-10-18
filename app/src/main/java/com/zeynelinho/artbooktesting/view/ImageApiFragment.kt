package com.zeynelinho.artbooktesting.view

import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.zeynelinho.artbooktesting.R
import com.zeynelinho.artbooktesting.adapter.ImageRecyclerAdapter
import com.zeynelinho.artbooktesting.databinding.FragmentImageApiBinding
import com.zeynelinho.artbooktesting.util.Status
import com.zeynelinho.artbooktesting.viewModel.ArtViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageApiFragment @Inject constructor(
    private val imageRecyclerAdapter: ImageRecyclerAdapter
    ): Fragment(R.layout.fragment_image_api) {

    private var fragmentBinding : FragmentImageApiBinding? = null
    lateinit var viewModel : ArtViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ArtViewModel::class.java]
        val binding = FragmentImageApiBinding.bind(view)
        fragmentBinding = binding

        var job : Job? = null

        binding.searchText.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(1000)
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.searchForImage(it.toString())
                    }
                }
            }
        }

        subscribeToObservers()

        binding.imageRecyclerView.adapter = imageRecyclerAdapter
        binding.imageRecyclerView.layoutManager = GridLayoutManager(requireContext(),3)

        imageRecyclerAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setSelectedImage(it)
        }

    }

    fun subscribeToObservers() {
        viewModel.imageList.observe(viewLifecycleOwner, Observer {
            when(it.status) {
                Status.SUCCESS -> {
                    var urls = it.data?.hits?.map { imageResult ->
                        imageResult.previewURL
                    }
                    imageRecyclerAdapter.images = urls ?: listOf()
                    fragmentBinding?.progressBar?.visibility = View.GONE
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(),it.message ?: "Error",Toast.LENGTH_LONG).show()
                    fragmentBinding?.progressBar?.visibility = View.GONE
                }
                Status.LOADING -> {
                    fragmentBinding?.progressBar?.visibility = View.VISIBLE
                }
            }
        })
    }

}