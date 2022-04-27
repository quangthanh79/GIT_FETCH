package com.apion.apionhome.ui.home

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.House
import com.apion.apionhome.databinding.FragmentAllHouseFeatureBinding
import com.apion.apionhome.ui.adapter.HousePostAdapter
import com.apion.apionhome.ui.profile.MyHouseSoldFragmentDirections
import com.apion.apionhome.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllHouseFeatureFragment : BindingFragment<FragmentAllHouseFeatureBinding>(FragmentAllHouseFeatureBinding::inflate) {
    override val viewModel by sharedViewModel<SearchViewModel>()
    private val postHouseAdapter = HousePostAdapter(::onItemPostClick)


    override fun setupView() {
        binding.lifecycleOwner = this
        binding.searchViewModel = viewModel
        binding.recyclerViewHousePost.adapter = postHouseAdapter
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun onItemPostClick(house: House) {
        val action = AllHouseFeatureFragmentDirections.actionHouseToDetail(house)
        findNavController().navigate(action)
    }
}