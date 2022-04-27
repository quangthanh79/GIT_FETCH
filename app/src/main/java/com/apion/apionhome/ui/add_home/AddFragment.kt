package com.apion.apionhome.ui.add_home

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingFragmentPickImage
import com.apion.apionhome.data.model.RangeUI
import com.apion.apionhome.data.model.ImagePicker as ImagePickerData
import com.apion.apionhome.databinding.FragmentCreateHomeBinding
import com.apion.apionhome.ui.adapter.ImagePickerAdapter
import com.apion.apionhome.ui.home.HomeViewModel
import com.apion.apionhome.ui.search.SearchViewModel
import com.apion.apionhome.ui.search.SearchViewModelTmp
import com.apion.apionhome.utils.customview.actionsheet.callback.ActionSheetCallBack
import com.apion.apionhome.utils.showAction
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddFragment :
    BindingFragmentPickImage<FragmentCreateHomeBinding>(FragmentCreateHomeBinding::inflate) {
    override val viewModel by viewModel<AddHouseViewModel>()
    private val searchViewModelTmp by sharedViewModel<SearchViewModel>()
    private val homeViewModel by sharedViewModel<HomeViewModel>()

    private val adapterImage by lazy {
        ImagePickerAdapter(
            requireContext(),
            mutableListOf(ImagePickerData(ImagePickerAdapter.VIEW_TYPE_TWO, null)),
            ::onPickerImage
        )
    }

    private var isShowType = false
    private var isShowDirection = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModelTmp.initData()
        searchViewModelTmp.setProvince(null)
        searchViewModelTmp.setDistrict(null)
    }

    override fun setupView() {
        binding.lifecycleOwner = this
        listener()
        binding.recyclerViewPickerImage.adapter = adapterImage
        binding.addHouseVM = viewModel
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        val view = requireActivity().window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//          requireActivity().window.decorView.windowInsetsController?.hide(WindowInsets.Type.statusBars())
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

    fun listener() {
        binding.btnCreate.setOnClickListener {
            viewModel.createHouse(
                searchViewModelTmp.province.value?.name,
                searchViewModelTmp.district.value?.name,
                searchViewModelTmp.ward.value?.name,
                searchViewModelTmp.street.value?.name,
                searchViewModelTmp.detailAddress.value,
                adapterImage.list.map { it.data }
            ) {
                showToast("Ký nhà thành công!")
                homeViewModel.initData()
                findNavController().popBackStack()
            }
        }
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.edtTypeHouse.setOnClickListener {
            val data = ArrayList(RangeUI.houseTypeRangeUis.values)
            data.removeFirst()
            if (!isShowType) {
                isShowType = true
                requireContext().showAction(
                    "Chọn loại nhà đất",
                    data,
                    false,
                    object : ActionSheetCallBack {
                        override fun data(data: String, position: Int) {
                            viewModel.setHouseTypeIndex(position + 1)
                            binding.edtTypeHouse.setText(data)
                            isShowType = false
                        }
                    }) {
                    isShowType = false
                }
            }
        }

        binding.edtDirectionHouse.setOnClickListener {
            val data = ArrayList(RangeUI.homeDirectionRangeUis.values)
            if (!isShowDirection) {
                isShowDirection = true
                requireContext().showAction(
                    "Chọn hướng nhà",
                    data,
                    false,
                    object : ActionSheetCallBack {
                        override fun data(data: String, position: Int) {
                            viewModel.setHouseDirectionIndex(position + 1)
                            binding.edtDirectionHouse.setText(data)
                            isShowDirection = false
                        }
                    }) {
                    isShowDirection = false
                }
            }
        }
        binding.edtAddressHouse.setOnClickListener {
            if (searchViewModelTmp.province.value != null) {
                findNavController().navigate(R.id.actionToSelectLocationCreateHous)
            } else {
                findNavController().navigate(
                    R.id.actionToSelectLocationCreateHous,
                    bundleOf("isClear" to true)
                )
            }
        }
    }

    private fun setupObserver() {
        searchViewModelTmp.province.observe(this) {
            if (it != null) {
                binding.edtAddressHouse.setText(searchViewModelTmp.getAddress())
            } else {
                binding.edtAddressHouse.setText(getString(R.string.text_select_address))
            }
        }
    }

    private fun onPickerImage() {
        pickImageSafety()
    }

    override fun onImagesSelect(path: String) {
        adapterImage.addImage(path)
        println(path)
    }
}
