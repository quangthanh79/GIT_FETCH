package com.apion.apionhome.ui.search

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.SeekBar
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.base.BindingFragmentBottomSheet
import com.apion.apionhome.data.model.RangeUI
import com.apion.apionhome.databinding.FragmentSearchBinding
import com.apion.apionhome.utils.customview.actionsheet.callback.ActionSheetCallBack
import com.apion.apionhome.utils.showAction
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchFragment :
    BindingFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    override val viewModel by sharedViewModel<SearchViewModel>()
    private var isFirstCome = true
    private var isShowType = false
    private var isShowDirection = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initData()
        viewModel.setProvince(null)
        viewModel.setDistrict(null)
    }

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.searchVM = viewModel
        observer()
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
        binding.seekBarPrice.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.setPriceIndex(p1)
                binding.textPrice.text =
                    RangeUI.priceRangeUis.values.toMutableList()[viewModel.priceIndex.value ?: 0]
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        binding.seekBarArg.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.setAcreageIndex(p1)
                binding.textArg.text =
                    RangeUI.acreageRangeUis.values.toMutableList()[viewModel.acreageIndex.value
                        ?: 0]
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        binding.seekBarFront.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.setFrontWidthIndex(p1)
                binding.textFront.text =
                    RangeUI.frontWidthRangeUis.values.toMutableList()[viewModel.frontWidthIndex.value
                        ?: 0]
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        binding.seekBarRoom.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.setBedroomsIndex(p1)
                binding.textRoom.text =
                    RangeUI.bedroomUis.values.toMutableList()[viewModel.bedroomIndex.value ?: 0]
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        binding.textLayoutHouseType.setOnClickListener {
            val data = ArrayList(RangeUI.houseTypeRangeUis.values)
            if (!isShowType) {
                isShowType = true
                requireContext().showAction(
                    "Chọn loại nhà đất",
                    data,
                    false,
                    object : ActionSheetCallBack {
                        override fun data(data: String, position: Int) {
                            isShowType = false
                            viewModel.setHouseTypeIndex(position)
                            binding.textLayoutHouseType.setText(data)
                        }
                    }) {
                    isShowType = false
                }
            }
        }
        binding.textLayoutDirection.setOnClickListener {
            val data = ArrayList(RangeUI.homeDirectionRangeUis.values)
            if (!isShowDirection) {
                isShowDirection = true
                requireContext().showAction("Chọn hướng nhà", data,false, object : ActionSheetCallBack {
                    override fun data(data: String, position: Int) {
                        println(position)
                        viewModel.setHouseDirectionIndex(position)
                        binding.textLayoutDirection.setText(data)
                        isShowDirection = false
                    }
                }) {
                    isShowDirection = false
                }
            }
        }
        binding.textLayoutAddress.setOnClickListener {
            findNavController().navigate(R.id.actionToSelectLocation)
        }
        binding.btnSearch.setOnClickListener {
            isFirstCome = false
            viewModel.searchHouse()
        }
        binding.icDeleteAddress.setOnClickListener {
            viewModel.setProvince(null)
            viewModel.setDistrict(null)
        }
    }

    private fun observer() {
        viewModel.province.observe(this) {
            if (it != null) {
                binding.textLayoutAddress.setText(viewModel.getAddress())
            } else {
                binding.textLayoutAddress.setText(getString(R.string.text_select_address))
            }
        }
        viewModel.isSearchDone.observe(this) {
            if (it) {
                if (viewModel.housesSearch.value?.isNotEmpty() == true) {
                    viewModel.setSearchDone(false)
                    findNavController().navigate(R.id.actionToDetailSearchResult)
                } else {
                    showToast("không tìm thấy kết quả nào")
                }
            }
        }
    }
}
