package com.apion.apionhome.ui.profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.MyApplication
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.data.model.RangeUI
import com.apion.apionhome.data.source.local.HouseLocalDatasource
import com.apion.apionhome.databinding.FragmentLevelBinding
import com.apion.apionhome.databinding.FragmentUpdateProfileBinding
import com.apion.apionhome.ui.search.SearchViewModelTmp
import com.apion.apionhome.utils.customview.actionsheet.callback.ActionSheetCallBack
import com.apion.apionhome.utils.setup
import com.apion.apionhome.utils.showAction
import com.apion.apionhome.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ProfileUpdateFragment :
    BindingFragment<FragmentUpdateProfileBinding>(FragmentUpdateProfileBinding::inflate) {
    override val viewModel by viewModel<ProfileViewModel>()
    private val searchViewModelTmp by sharedViewModel<SearchViewModelTmp>()
    val calendar = Calendar.getInstance()
    private var isShowSex = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModelTmp.initData()
        searchViewModelTmp.setProvince(null)
        searchViewModelTmp.setDistrict(null)
        MyApplication.sessionUser.value?.province?.let {
            HouseLocalDatasource.getProvinceByName(requireContext(), it)
                .setup()
                .subscribe(
                    { province ->
                        println(province)
                        searchViewModelTmp.setProvince(province)
                        MyApplication.sessionUser.value?.district?.let { districtName ->
                            val district =
                                HouseLocalDatasource.getDistrictByName(districtName, province)
                            district?.let {
                                searchViewModelTmp.setDistrict(district)
                                MyApplication.sessionUser.value?.ward?.let { wardName ->
                                    val ward =
                                        HouseLocalDatasource.getWardByName(wardName, district)
                                    searchViewModelTmp.setWard(ward)
                                }
                                MyApplication.sessionUser.value?.street?.let { streetName ->
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
        println("address ${MyApplication.sessionUser.value?.address}")
        searchViewModelTmp.detailAddress.value = MyApplication.sessionUser.value?.address
    }

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.user = MyApplication.sessionUser.value
        viewModel.email.value = viewModel.email.value ?: MyApplication.sessionUser.value?.email
        viewModel.fbId.value = viewModel.fbId.value ?: MyApplication.sessionUser.value?.facebook_id
        viewModel.name.value = viewModel.name.value ?: MyApplication.sessionUser.value?.name
        viewModel.sex.value = viewModel.sex.value ?: MyApplication.sessionUser.value?.sex
        viewModel.bios.value = viewModel.bios.value ?: MyApplication.sessionUser.value?.bios
        viewModel.phone.value = viewModel.phone.value ?: MyApplication.sessionUser.value?.phone
        viewModel.refer.value = viewModel.refer.value ?: MyApplication.sessionUser.value?.refer
        binding.userProfileVM = viewModel
        val user = MyApplication.sessionUser.value

        binding.btnDone.setOnClickListener {

            if (binding.edtName.text.toString().isNullOrBlank()) {
                binding.textErrorName.visibility = View.VISIBLE
            } else {
                binding.textErrorName.visibility = View.VISIBLE
                user?.address = searchViewModelTmp.detailAddress.value
                user?.province = searchViewModelTmp.province.value?.name
                user?.district = searchViewModelTmp.district.value?.name
                user?.ward = searchViewModelTmp.ward.value?.name
                user?.street = searchViewModelTmp.street.value?.name

                user?.dateOfBirth =
                    viewModel.getDobAPI() ?: MyApplication.sessionUser.value?.dateOfBirth
                user?.bios = binding.edtDescription.text.toString()
                user?.sex = viewModel.sex.value ?: binding.user?.sex
                user?.name = binding.edtName.text.toString()
                user?.refer = user?.refer ?: binding.edtRefer.text.toString()
                viewModel.updateUser(user) {
                    showToast("Đã cập nhật thông tin!")
                    findNavController().popBackStack()
                }
            }
        }
        binding.btnAfter.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.edtAddress.setOnClickListener {
            println("detail ${searchViewModelTmp.detailAddress.value}")
            if (searchViewModelTmp.province.value != null || searchViewModelTmp.detailAddress.value != null) {
                findNavController().navigate(R.id.actionToSelectLocationCreateHous)
            } else {
                findNavController().navigate(
                    R.id.actionToSelectLocationCreateHous,
                    bundleOf("isClear" to true)
                )
            }
        }
        binding.edtDOB.setOnClickListener {
            setupPickerDialog()
        }
        binding.edtSex.setOnClickListener {
            val data = ArrayList<String>()
            data.add("Nam")
            data.add("Nữ")
            if (!isShowSex) {
                isShowSex = true
                requireContext().showAction(
                    "Chọn hướng nhà",
                    data,
                    false,
                    object : ActionSheetCallBack {
                        override fun data(data: String, position: Int) {
                            binding.edtSex.setText(data)
                            viewModel.sex.value = if (position == 0) "Male" else "Female"
                            isShowSex = false
                        }
                    }) {
                    isShowSex = false
                }
            }
        }
        searchViewModelTmp.street.observe(this) {
            if (it != null) {
                binding.edtAddress.setText(searchViewModelTmp.getAddress())
            } else if (searchViewModelTmp.province.value != null) {
                binding.edtAddress.setText(searchViewModelTmp.getAddress())
            } else if (searchViewModelTmp.detailAddress.value != null) {
                binding.edtAddress.setText(searchViewModelTmp.getAddress())
            } else {
                binding.edtAddress.setText(getString(R.string.text_select_address))
            }
        }
        searchViewModelTmp.ward.observe(this) {
            if (it != null) {
                binding.edtAddress.setText(searchViewModelTmp.getAddress())
            } else if (searchViewModelTmp.province.value != null) {
                binding.edtAddress.setText(searchViewModelTmp.getAddress())
            } else if (searchViewModelTmp.detailAddress.value != null) {
                binding.edtAddress.setText(searchViewModelTmp.getAddress())
            } else {
                binding.edtAddress.setText(getString(R.string.text_select_address))
            }
        }
        searchViewModelTmp.detailAddress.observe(this) {
            if (it != null) {
                binding.edtAddress.setText(searchViewModelTmp.getAddress())
            } else if (searchViewModelTmp.province.value != null) {
                binding.edtAddress.setText(searchViewModelTmp.getAddress())
            } else {
                binding.edtAddress.setText(getString(R.string.text_select_address))
            }
        }
    }

    fun setupPickerDialog() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val calendar1 = Calendar.getInstance()
                calendar1.set(year, monthOfYear, dayOfMonth)
                if (calendar1.timeInMillis > calendar.timeInMillis) {
                    viewModel.dob.value = calendar
                    binding.edtDOB.setText(viewModel.getDobShow())
                } else {
                    viewModel.dob.value = calendar1
                    binding.edtDOB.setText(viewModel.getDobShow())
                }
            }
        var datePickerDialog = DatePickerDialog(
            requireContext(),
            AlertDialog.THEME_HOLO_LIGHT,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
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
}