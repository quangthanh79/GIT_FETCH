package com.apion.apionhome.ui.search

import android.os.Bundle
import android.widget.SeekBar
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.base.BindingFragmentBottomSheet
import com.apion.apionhome.data.model.RangeUI
import com.apion.apionhome.data.model.local.AllDistrict
import com.apion.apionhome.databinding.FragmentSearchDetailBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchDetailFragment :
    BindingFragmentBottomSheet<FragmentSearchDetailBinding>(FragmentSearchDetailBinding::inflate) {
    override val viewModel by sharedViewModel<SearchViewModel>()
    private val searchViewModelTmp by sharedViewModel<SearchViewModelTmp>()

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.searchVM = searchViewModelTmp
        viewModel.frontWidthIndex.value?.let {
            binding.seekBarHome.progress = it
        }
        listener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModelTmp.setProvince(viewModel.province.value)
        searchViewModelTmp.setDistrict(viewModel.district.value)
        searchViewModelTmp.setWard(viewModel.ward.value)
        searchViewModelTmp.setStreet(viewModel.street.value)
        searchViewModelTmp.setAcreageIndex(viewModel.acreageIndex.value ?: 0)
    }

    fun listener() {
        binding.seekBarHome.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                println(p1)
                searchViewModelTmp.setFrontWidthIndex(p1)
                binding.edtFrontWidth.text =
                    RangeUI.frontWidthRangeUis.values.toMutableList()[searchViewModelTmp.frontWidthIndex.value
                        ?: 0]
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        binding.edtWard.setOnClickListener {
            if (viewModel.district.value != null && viewModel.district.value !is AllDistrict) {
                findNavController().navigate(
                    R.id.actionToSearchWardFragment,
                    bundleOf("shareData" to false)
                )
            } else {
                showCancelDialog(null, getString(R.string.error_select_ward))
            }
        }

        binding.edtStreet.setOnClickListener {
            if (viewModel.district.value != null && viewModel.district.value !is AllDistrict) {
                findNavController().navigate(
                    R.id.actionToSearchStreetFragment,
                    bundleOf("shareData" to false)
                )
            } else {
                showCancelDialog(null, getString(R.string.error_select_ward))
            }
        }

        binding.iconBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnUse.setOnClickListener {
            viewModel.setWard(searchViewModelTmp.ward.value)
            viewModel.setStreet(searchViewModelTmp.street.value)
            viewModel.setFrontWidthIndex(searchViewModelTmp.frontWidthIndex.value ?: 0)
            findNavController().popBackStack()
        }
    }
}
