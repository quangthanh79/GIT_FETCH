package com.apion.apionhome.ui.detail

import android.app.AlertDialog
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsetsController
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.apion.apionhome.*
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.data.model.House
import com.apion.apionhome.data.model.RangeUI
import com.apion.apionhome.databinding.FragmentDetailHouseBinding
import com.apion.apionhome.ui.adapter.HouseAdapter
import com.apion.apionhome.ui.adapter.ImageDetailBannerAdapter
import com.apion.apionhome.ui.person.UserProfileViewModel
import com.apion.apionhome.utils.TabApp
import com.apion.apionhome.utils.customview.actionsheet.callback.ActionSheetCallBack
import com.apion.apionhome.utils.showAction
import com.apion.apionhome.utils.toMessage
import com.apion.apionhome.utils.toPhone
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException


class DetailHouseFragment :
    BindingFragment<FragmentDetailHouseBinding>(FragmentDetailHouseBinding::inflate) {
    override val viewModel by viewModel<DetailViewModel>()
    val userViewModel by sharedViewModel<UserProfileViewModel>()

    private val args by navArgs<DetailActivityArgs>()

    private val adapterImage = ImageDetailBannerAdapter(::onItemBannerClick)

    private val adapterRelated = HouseAdapter(::onItemHouseClick)
    private var isShowMore = false
    val dialogLogin by lazy {
        AlertDialog.Builder(requireContext())
            .setTitle("Yêu cầu đăng nhập!")
            .setMessage("Vui lòng đăng nhập để sử dụng tính năng này!")
            .setPositiveButton("Đăng nhập") { _, _ ->
                findNavController().navigate(DetailGraDirections.actionDetailToLogin())
                MyApplication.tabToNavigate.value = TabApp.DETAIL_HOUSE
                MyApplication.houseNavigate.value = viewModel.houseDetail.value
            }
            .setNegativeButton(getString(R.string.tittle_button_cancel)) { dialogShow, _ ->
                MyApplication.tabToNavigate.value = null
                MyApplication.houseNavigate.value = null
                dialogShow.dismiss()
            }
    }
    val dialogConfirm by lazy {
        AlertDialog.Builder(requireContext())
            .setTitle("Thông báo")
            .setMessage("Bạn có chắn chắn muốn xoá?")
            .setPositiveButton("Đồng ý") { _, _ ->
                viewModel.removeHouse() {
                    showToast("Đã bán xóa nhà thành công")
                    activity?.finish()
                }
            }
            .setNegativeButton(getString(R.string.tittle_button_cancel)) { dialogShow, _ ->

            }
    }
    private val callback by lazy {
        OnMapReadyCallback { googleMap ->
            Thread {
                try {
                    val address = Geocoder(
                        requireActivity()
                    ).getFromLocationName(args.houseDetail.getDetailAddress(), 1)
                    var latLng = LatLng(20.995195733794585, 105.86181631094217)
                    if (address.isNotEmpty()) {
                        val fist = address.first()
                        if (fist.hasLatitude() && fist.hasLongitude())
                            latLng = LatLng(fist.latitude, fist.longitude)
                    }
                    activity?.runOnUiThread {
                        googleMap.addMarker(MarkerOptions().position(latLng).title("Apion Home"))
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    override fun setupView() {
        viewModel.setHouseDetail(args.houseDetail)
        binding.lifecycleOwner = this
        binding.detailVM = viewModel
        binding.recyclerViewRelated.adapter = adapterRelated
        setupBanner()
        userViewModel.isLoading.observe(this){
            if (it) dialog.show() else dialog.dismiss()
        }
        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.cardViewPerson.setOnClickListener {
            userViewModel.fetchUser2(viewModel.houseDetail.value?.owner?.id!!){
                val action = DetailGraDirections.actionToPersonProfile(it)
                findNavController().navigate(action)
            }
        }
        binding.buttonFollow.setOnClickListener {

            if (MyApplication.sessionUser.value != null) {
                val isFollow = MyApplication.sessionUser.value!!.isFollowing(viewModel.houseDetail.value?.owner?.id.toString())
                if (MyApplication.sessionUser.value!!.id == viewModel.houseDetail.value?.owner?.id) {

                    findNavController().navigate(MobileNavigationDirections.actionToAdd())
//                      findNavController().popBackStack()

                } else if (isFollow) {
                    userViewModel.unFollow(
                        MyApplication.sessionUser.value?.id!!,
                        viewModel.houseDetail.value?.owner?.id ?: -1
                    )
                } else {
                    userViewModel.follow(
                        MyApplication.sessionUser.value?.id!!,
                        viewModel.houseDetail.value?.owner?.id ?: -1
                    )
                }
            } else {
                dialogLogin.show()
            }
        }
        binding.buttonCall.setOnClickListener {
            requireContext().toPhone(viewModel.houseDetail.value?.owner?.phone!!)
        }
        binding.buttonMessage.setOnClickListener {
            requireContext().toMessage(viewModel.houseDetail.value?.owner?.phone!!)
        }
        binding.buttonBookmark.setOnClickListener {
            if (MyApplication.sessionUser.value != null) {
                if (MyApplication.sessionUser.value?.isBookmark(
                        viewModel.houseDetail.value?.id ?: -1
                    ) == true
                ) {
                    viewModel.removeBookmark(
                        MyApplication.sessionUser.value?.id ?: -1,
                        viewModel.houseDetail.value?.id ?: -1
                    )
                } else {
                    viewModel.createBookmark(
                        MyApplication.sessionUser.value?.id ?: -1,
                        viewModel.houseDetail.value?.id ?: -1
                    )
                }
            } else {
                dialogLogin.show()
            }

        }
        binding.textMore.setOnClickListener {
            val data = arrayListOf<String>("Bán căn này", "Sửa căn này", "Xóa căn này")
            if (!isShowMore) {
                isShowMore = true
                requireContext().showAction(
                    "Chọn hành động",
                    data,
                    false,
                    object : ActionSheetCallBack {
                        override fun data(data: String, position: Int) {
                            when (position) {
                                0 -> viewModel.soldHouse() {
                                    showToast("Đã bán nhà thành công")
                                    activity?.finish()
                                }
                                1 -> {
                                    viewModel.houseDetail.value?.let {
                                        findNavController().navigate(
                                            DetailHouseFragmentDirections.actionDetailHouseFragmentToFragmentEdit(
                                                it
                                            )
                                        )
                                    }
                                }
                                2 -> {
                                    dialogConfirm.show()
                                }


                            }
                            isShowMore = false
                        }
                    }) {
                    isShowMore = false
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
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

    private fun setupBanner() {
        binding.includeImage.imageSlider.adapter = adapterImage
    }

    private fun onItemHouseClick(house: House) {
        val destination = DetailHouseFragmentDirections.actionToSelf(house)
        findNavController().navigate(destination)
    }

    private fun onItemBannerClick(banner: String) {
        val destination =
            DetailHouseFragmentDirections.actionDetailToDetailImage(
                adapterImage.getCurrentIndex(
                    banner
                ),
                viewModel.houseDetail.value?.images?.toTypedArray() ?: emptyArray<String>()
            )
        findNavController().navigate(destination)
    }
}