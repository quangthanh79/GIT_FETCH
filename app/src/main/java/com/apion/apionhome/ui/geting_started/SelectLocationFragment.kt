package com.apion.apionhome.ui.geting_started

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.base.BindingFragmentBottomSheet
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.databinding.FragmentSelectLocationBinding
import com.apion.apionhome.databinding.FragmentSelectLocationRegisterBinding
import com.apion.apionhome.ui.search.SearchViewModel
import com.apion.apionhome.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SelectLocationFragment : BindingFragmentBottomSheet<FragmentSelectLocationRegisterBinding>(FragmentSelectLocationRegisterBinding::inflate) {
    override val viewModel by sharedViewModel<UserViewModel>()

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.selectVM = viewModel
//        viewModel.initData()
        listener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initData()
    }
    fun listener(){


        binding.icBack.setOnClickListener {
            this.findNavController().popBackStack()
        }
        binding.edtProvince.setOnClickListener {
            this.findNavController().navigate(R.id.actionToSelectProvince)
        }
        binding.edtDistrict.setOnClickListener {
            this.findNavController().navigate(R.id.actionToSelectDistrictFragment)
        }
        binding.edtWard.setOnClickListener {
            this.findNavController().navigate(R.id.actionToSelectWardFragment)
        }
        binding.edtStreet.setOnClickListener {
            this.findNavController().navigate(R.id.actionToSelectStreetFragment)
        }
        binding.btnUse.setOnClickListener{
            this.findNavController().popBackStack()
//            println(viewModel.province.value)

        }


    }


}