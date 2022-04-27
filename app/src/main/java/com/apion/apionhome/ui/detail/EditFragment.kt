package com.apion.apionhome.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingFragmentPickImage
import com.apion.apionhome.data.model.RangeUI
import com.apion.apionhome.data.source.local.HouseLocalDatasource
import com.apion.apionhome.data.model.ImagePicker as ImagePickerData
import com.apion.apionhome.databinding.FragmentCreateHomeBinding
import com.apion.apionhome.ui.adapter.ImagePickerAdapter
import com.apion.apionhome.ui.add_home.AddHouseViewModel
import com.apion.apionhome.ui.home.HomeViewModel
import com.apion.apionhome.ui.search.SearchViewModelTmp
import com.apion.apionhome.utils.customview.actionsheet.callback.ActionSheetCallBack
import com.apion.apionhome.utils.setup
import com.apion.apionhome.utils.showAction
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat

class EditFragment :
    BindingFragmentPickImage<FragmentCreateHomeBinding>(FragmentCreateHomeBinding::inflate) {
    override val viewModel by viewModel<AddHouseViewModel>()
    private val searchViewModelTmp by sharedViewModel<SearchViewModelTmp>()
    private val homeViewModel by sharedViewModel<HomeViewModel>()
    private val detailHouse by lazy {
        args.houseDetail
    }
    private val args by navArgs<EditFragmentArgs>()
    private val adapterImage by lazy {
        var list = args.houseDetail.images?.map {
            ImagePickerData(ImagePickerAdapter.VIEW_TYPE_ONE, it)
        }?.toMutableList()?.apply {
            add(ImagePickerData(ImagePickerAdapter.VIEW_TYPE_TWO, null))
        } ?: mutableListOf(ImagePickerData(ImagePickerAdapter.VIEW_TYPE_TWO, null))

        ImagePickerAdapter(
            requireContext(),
            list,
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
        setupData()
    }

    override fun setupView() {
        binding.lifecycleOwner = this
        listener()
        binding.recyclerViewPickerImage.adapter = adapterImage
        binding.addHouseVM = viewModel
        binding.textContent.text = "Chỉnh sửa nhà đã ký"
        binding.btnCreate.text = "Sửa nhà"

        setupObserver()
    }

    private fun setupData() {
        detailHouse.province?.let {
            HouseLocalDatasource.getProvinceByName(requireContext(), it)
                .setup()
                .subscribe(
                    { province ->
                        println(province)
                        searchViewModelTmp.setProvince(province)
                        detailHouse.district?.let { districtName ->
                            val district =
                                HouseLocalDatasource.getDistrictByName(districtName, province)
                            district?.let {
                                searchViewModelTmp.setDistrict(district)
                                detailHouse.ward?.let { wardName ->
                                    val ward =
                                        HouseLocalDatasource.getWardByName(wardName, district)
                                    searchViewModelTmp.setWard(ward)
                                }
                                detailHouse.street?.let { streetName ->
                                    val street =
                                        HouseLocalDatasource.getStreetByName(streetName, district)
                                    searchViewModelTmp.setStreet(street)
                                }
                            }
                        }
                    }, {
                        it.printStackTrace()
                    }
                )
        }

        detailHouse.address.let {
            searchViewModelTmp.detailAddress.value = it
        }

        detailHouse.title?.let {
            viewModel.title.value = it
        }
        detailHouse.houseType?.let { type ->
            val index = RangeUI.houseTypeRangeUis.keys.indexOfFirst {
                it == type
            }
            viewModel.setHouseTypeIndex(index)
        }
        detailHouse.price?.let {
            viewModel.price.value = it.toString()
        }
        detailHouse.acreage?.let {
            viewModel.acreage.value = DecimalFormat("#").apply {
                maximumFractionDigits = 2
            }.format(it)
        }
        detailHouse.frontWidth?.let {
            viewModel.frontWidth.value = DecimalFormat("#").apply {
                maximumFractionDigits = 2
            }.format(it)
        }
        detailHouse.content?.let {
            viewModel.content.value = it.toString()
        }
        detailHouse.homeDirection?.let { type ->
            val index = RangeUI.homeDirectionRangeUis.keys.indexOfFirst {
                it == type
            }
            viewModel.setHouseDirectionIndex(index)
        }
        detailHouse.bedrooms?.let {
            viewModel.bedroom.value = it.toString()
        }
        detailHouse.commissionRate?.let {
            viewModel.commissionRate.value = it
        }
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
            viewModel.updateHouse(
                searchViewModelTmp.province.value?.name,
                searchViewModelTmp.district.value?.name,
                searchViewModelTmp.ward.value?.name,
                searchViewModelTmp.street.value?.name,
                searchViewModelTmp.detailAddress.value,
                adapterImage.list.map { it.data },
                detailHouse
            ){
                showToast("sửa nhà thành công!")
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
        searchViewModelTmp.street.observe(this) {
            if (it != null) {
                binding.edtAddressHouse.text = searchViewModelTmp.getAddress()
            } else if (searchViewModelTmp.province.value != null) {
                binding.edtAddressHouse.text = searchViewModelTmp.getAddress()
            } else if (searchViewModelTmp.detailAddress.value != null) {
                binding.edtAddressHouse.text = searchViewModelTmp.getAddress()
            } else {
                binding.edtAddressHouse.text = getString(R.string.text_select_address)
            }
        }
        searchViewModelTmp.ward.observe(this) {
            if (it != null) {
                binding.edtAddressHouse.text = searchViewModelTmp.getAddress()
            } else if (searchViewModelTmp.province.value != null) {
                binding.edtAddressHouse.text = searchViewModelTmp.getAddress()
            } else if (searchViewModelTmp.detailAddress.value != null) {
                binding.edtAddressHouse.text = searchViewModelTmp.getAddress()
            } else {
                binding.edtAddressHouse.text = getString(R.string.text_select_address)
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
