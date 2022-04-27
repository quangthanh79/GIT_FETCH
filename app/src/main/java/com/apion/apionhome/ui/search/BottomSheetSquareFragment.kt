package com.apion.apionhome.ui.search

import android.widget.SeekBar
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.base.BindingFragmentBottomSheet
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.RangeUI
import com.apion.apionhome.databinding.BottomsheetPriceBinding
import com.apion.apionhome.databinding.BottomsheetSquareBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BottomSheetSquareFragment : BindingFragmentBottomSheet<BottomsheetSquareBinding>(BottomsheetSquareBinding::inflate){
    override val viewModel by sharedViewModel<SearchViewModel>()

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.searchVM = viewModel
        viewModel.acreageIndex.value?.let {
            binding.seekBarHome.progress = it
        }
        listener()
    }
    private fun listener(){
        binding.seekBarHome.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.setAcreageIndex(p1)
                binding.txtSquare.text =
                    RangeUI.acreageRangeUis.values.toMutableList()[viewModel.acreageIndex.value ?: 0]
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        binding.btnDone.setOnClickListener {
            this.findNavController().popBackStack()

        }
    }
}
