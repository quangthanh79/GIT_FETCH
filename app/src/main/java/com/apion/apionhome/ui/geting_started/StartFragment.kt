package com.apion.apionhome.ui.geting_started

import androidx.navigation.fragment.findNavController
import com.apion.apionhome.R
import com.apion.apionhome.StartNavigationDirections
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.databinding.FragmentStartBinding
import com.apion.apionhome.utils.createProgressDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartFragment : BindingFragment<FragmentStartBinding>(FragmentStartBinding::inflate) {

    override val viewModel by viewModel<RxViewModel>()

    override fun setupView() {
        binding.buttonStart.setOnClickListener {
            findNavController().navigate(StartNavigationDirections.actionToLoginStart())
        }
    }

    override fun onDisConnection() {
        super.onDisConnection()
        requireContext().createProgressDialog()
    }
}
