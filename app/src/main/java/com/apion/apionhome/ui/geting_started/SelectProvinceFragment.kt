package com.apion.apionhome.ui.geting_started

import com.apion.apionhome.base.BindingFragmentBottomSheet
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.local.Province
import com.apion.apionhome.databinding.BottomsheetSearchLocationRegisterBinding
import com.apion.apionhome.databinding.FragmentSelectLocationRegisterBinding
import com.apion.apionhome.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

//class SelectProvinceFragment : BindingFragmentBottomSheet<BottomsheetSearchLocationRegisterBinding>(BottomsheetSearchLocationRegisterBinding::inflate) {
//    override val viewModel by sharedViewModel<UserViewModel>()
//
//    override fun setupView() {
//        binding.lifecycleOwner = this
//        binding.selectViewModel = viewModel
//    }
//}
class SelectProvinceFragment : SelectLocationDetailFragment<Province>(){
    override fun onItemClick(item: Province) {
        viewModel.setProvince(item)
    }

    override fun onGetAll(query: String) {
        viewModel.searchProvince("")
    }

}