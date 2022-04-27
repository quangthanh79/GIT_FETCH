package com.apion.apionhome.ui.search

import androidx.navigation.fragment.findNavController
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.data.model.House
import com.apion.apionhome.databinding.FragmentSearchDetailResultBinding
import com.apion.apionhome.ui.adapter.HousePostAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchDetailResultFragment :
    BindingFragment<FragmentSearchDetailResultBinding>(FragmentSearchDetailResultBinding::inflate) {

    override val viewModel by sharedViewModel<SearchViewModel>()

    private val postHouseAdapter = HousePostAdapter(::onItemPostClick)

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.searchVM = viewModel
        binding.recyclerViewHousePost.adapter = postHouseAdapter
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun onItemPostClick(house:House){
        val destination = SearchDetailResultFragmentDirections.actionToDetailPost(house)
        findNavController().navigate(destination)
    }
}
