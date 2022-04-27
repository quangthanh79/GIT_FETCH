package com.apion.apionhome.ui.profile

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.House
import com.apion.apionhome.data.model.User_I
import com.apion.apionhome.databinding.FragmentAllMyHouseBinding
import com.apion.apionhome.databinding.FragmentAllMyHouseSoldBinding
import com.apion.apionhome.ui.adapter.HousePostAdapter
import com.apion.apionhome.ui.person.PersonProfileFragmentArgs
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyHouseSoldFragment :
    BindingFragment<FragmentAllMyHouseSoldBinding>(FragmentAllMyHouseSoldBinding::inflate) {
    override val viewModel by viewModel<RxViewModel>()

    private val postHouseAdapter = HousePostAdapter(::onItemPostClick)
    private val args: MyHouseSoldFragmentArgs by navArgs()

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.recyclerViewHousePost.adapter = postHouseAdapter
        binding.user = args.userProfile
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun onItemPostClick(house: House) {
        val action = MyHouseSoldFragmentDirections.actionMyHouseToDetail(house)
        findNavController().navigate(action)
    }
}
