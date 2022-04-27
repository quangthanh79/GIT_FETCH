package com.apion.apionhome.ui.person

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apion.apionhome.MyApplication
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.House
import com.apion.apionhome.databinding.FragmentAllMyHouseBinding
import com.apion.apionhome.ui.adapter.HousePostAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyHouseFragment :
    BindingFragment<FragmentAllMyHouseBinding>(FragmentAllMyHouseBinding::inflate) {
    override val viewModel by viewModel<RxViewModel>()

    private val postHouseAdapter = HousePostAdapter(::onItemPostClick)
    private val args: PersonProfileFragmentArgs by navArgs()

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.recyclerViewHousePost.adapter = postHouseAdapter
        binding.user = args.userProfile
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        if (args.userProfile.id == MyApplication.sessionUser.value?.id) {
            binding.user = MyApplication.sessionUser.value
        }
    }

    private fun onItemPostClick(house: House) {
        val action = MyHouseFragmentDirections.actionMyHouseToDetail(house)
        findNavController().navigate(action)
    }
}
