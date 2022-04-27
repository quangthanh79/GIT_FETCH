package com.apion.apionhome.ui.search

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.MobileNavigationDirections
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingFragmentBottomSheet
import com.apion.apionhome.data.model.local.AllDistrict
import com.apion.apionhome.data.model.local.District
import com.apion.apionhome.databinding.FragmentSelectLocationBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SelectLocationFragment :
    BindingFragmentBottomSheet<FragmentSelectLocationBinding>(FragmentSelectLocationBinding::inflate) {

    override val viewModel by sharedViewModel<SearchViewModel>()
    private val searchViewModelTmp by sharedViewModel<SearchViewModelTmp>()

    val includeAll by lazy {
        arguments?.getBoolean("includeAll", true) ?: true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModelTmp.initData()
        viewModel.province.value?.let {
            searchViewModelTmp.setProvince(it)
        }
        viewModel.district.value?.let {
            searchViewModelTmp.setDistrict(it)
        }
        viewModel.ward.value?.let {
            searchViewModelTmp.setWard(it)
        }
        viewModel.street.value?.let {
            searchViewModelTmp.setStreet(it)
        }
    }

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.searchVM = searchViewModelTmp
        listener()
    }

    override fun onResume() {
        super.onResume()
        val view = requireActivity().window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            requireActivity().window.decorView.windowInsetsController?.hide(WindowInsets.Type.statusBars())
            view.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            view.systemUiVisibility =
                view.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun onStop() {
        super.onStop()
        val view = requireActivity().window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view.windowInsetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            var flags = view.systemUiVisibility
            flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            view.systemUiVisibility = flags
        }
    }

    private fun listener() {
        binding.edtProvince.setOnClickListener {
            findNavController().navigate(
                R.id.actionToSearchProvinceFragment,
                bundleOf("shareData" to false)
            )
        }
        binding.edtDistrict.setOnClickListener {
            findNavController().navigate(
                R.id.actionToSearchDistrictFragment,
                bundleOf("shareData" to false, "includeAll" to includeAll)
            )
        }
        binding.edtStreet.setOnClickListener {
            println("stree click")
            if (searchViewModelTmp.district.value != null && searchViewModelTmp.district.value !is AllDistrict) {
                findNavController().navigate(
                    R.id.actionToSearchStreetFragment,
                    bundleOf("shareData" to false, "includeAll" to includeAll)
                )
            } else {
                showCancelDialog(null, getString(R.string.error_select_ward))
            }
        }
        binding.edtWard.setOnClickListener {
            if (searchViewModelTmp.district.value != null && searchViewModelTmp.district.value !is AllDistrict) {
                findNavController().navigate(
                    R.id.actionToSearchWardFragment,
                    bundleOf("shareData" to false, "includeAll" to includeAll)
                )
            } else {
                showCancelDialog(null, getString(R.string.error_select_ward))
            }
        }
        binding.btnSearch.setOnClickListener {
            viewModel.setProvince(searchViewModelTmp.province.value)
            viewModel.setDistrict(searchViewModelTmp.district.value)
            viewModel.setWard(searchViewModelTmp.ward.value)
            viewModel.setStreet(searchViewModelTmp.street.value)
            findNavController().popBackStack()
        }
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
