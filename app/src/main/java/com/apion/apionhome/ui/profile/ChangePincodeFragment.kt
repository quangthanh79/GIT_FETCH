package com.apion.apionhome.ui.profile

import androidx.navigation.fragment.findNavController
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.databinding.FragmentChangePincodeBinding
import com.apion.apionhome.databinding.FragmentGetPincodeNewBinding
import com.apion.apionhome.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePincodeFragment :
    BindingFragment<FragmentChangePincodeBinding>(FragmentChangePincodeBinding::inflate) {
    override val viewModel by viewModel<ProfileViewModel>()
    private val userId by lazy {
        arguments?.getInt("userId", -1) ?: -1
    }

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.getPincodeVM = viewModel
        setupListener()
    }

    fun setupListener() {
        binding.btnChangePincode.setOnClickListener {
            if (userId != -1) {
                viewModel.changePin((userId ?: -1).toString()) {
                    showToast("Đổi pin code thành công!")
                    findNavController().popBackStack()
                }
            }
        }
        binding.imageBackForget.setOnClickListener {
            this.findNavController().popBackStack()
        }
    }
}