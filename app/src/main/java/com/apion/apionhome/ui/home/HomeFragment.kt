package com.apion.apionhome.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.apion.apionhome.MobileNavigationDirections
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.data.model.House
import com.apion.apionhome.data.model.RangeUI
import com.apion.apionhome.data.model.User_I
import com.apion.apionhome.data.model.dashboard.Banner
import com.apion.apionhome.databinding.FragmentHomeBinding
import com.apion.apionhome.ui.adapter.HouseAdapter
import com.apion.apionhome.ui.adapter.ImageSliderAdapter
import com.apion.apionhome.ui.adapter.UserOnlineAdapter
import com.apion.apionhome.ui.search.SearchViewModel
import com.apion.apionhome.ui.search.SearchViewModelHome
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.IOException


class HomeFragment :
    BindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override val viewModel by sharedViewModel<HomeViewModel>()

    private val searchViewModel by sharedViewModel<SearchViewModel>()

    private val sliderHandler = Handler(Looper.getMainLooper())

    private val adapterImage = ImageSliderAdapter(::onItemBannerClick)

    private val adapterFeature = HouseAdapter() { house ->
        onItemHouseClick(house)
    }

    private val adapterHanoi = HouseAdapter(::onItemHouseClick)

    private val adapterSaiGon = HouseAdapter(::onItemHouseClick)

    private val adapterUserOnline by lazy {
        UserOnlineAdapter(::onItemUserOnlineClick, ::onChatNowClick, requireContext())
    }

    private var isCheck = false

    private var isFirstCome = true

    private val runnable by lazy {
        Runnable {
            var current = binding.layoutBanner.imageSlider.currentItem
            if (adapterImage.itemCount - 1 == current) {
                current = 0
            } else {
                current++
            }
            binding.layoutBanner.imageSlider.currentItem = current
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("lai vao set up view")
        searchViewModel.initData()
    }

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.layoutFeature.recyclerViewFeature.adapter = adapterFeature
        binding.layoutHanoi.recyclerViewFeature.adapter = adapterHanoi
        binding.layoutSaigon.recyclerViewFeature.adapter = adapterSaiGon
        binding.pagerUserOnline.adapter = adapterUserOnline
        setupBanner()
        setupListener()
        setupRefresh()
        setupSearchView()
        setupObserve()
    }

    override fun onConnectionAvailable() {
        binding.searchVM = searchViewModel
        binding.homeVM = viewModel
        viewModel.initData()
    }

    private fun setupListener() {
        binding.editTextSquare.setOnClickListener {
            this.findNavController().navigate(R.id.actionToBottomSheetSquareFragment)
        }
        binding.editTextCity.setOnClickListener {
            findNavController().navigate(R.id.actionToSearchProvinceFragment)
        }
        binding.editTextDistrict.setOnClickListener {
            findNavController().navigate(R.id.actionToSearchDistrictFragment)
        }
        binding.editTextPrice.setOnClickListener {
            findNavController().navigate(R.id.actionToBottomSheetPriceFragment)
        }
        binding.layoutWardStreet.setOnClickListener {
            findNavController().navigate(R.id.actionToSearchDetailFragment)
        }
        binding.buttonDetailFeature.setOnClickListener {
            viewModel.dashBoard.value?.let{
                val action = MobileNavigationDirections.actionToAllHouseFragment()
//                findNavController().navigate(action)
                searchViewModel._homeFeature.value = it.feature
                findNavController().navigate(action)
            }
        }
        binding.txtSearch.setOnClickListener {
            isFirstCome = false
            searchViewModel.searchHouse()
        }
    }

    override fun onStop() {
        super.onStop()
        //binding.layoutHeader.root.viewTreeObserver.removeOnGlobalLayoutListener(observerVisible)
        sliderHandler.removeCallbacks(runnable)
    }

    private fun setupObserve() {
        searchViewModel.priceIndex.observe(this) {
            binding.editTextPrice.text =
                RangeUI.priceRangeUis.values.toMutableList()[searchViewModel.priceIndex.value ?: 0]
        }
        searchViewModel.acreageIndex.observe(this) {
            binding.editTextSquare.text =
                RangeUI.acreageRangeUis.values.toMutableList()[searchViewModel.acreageIndex.value
                    ?: 0]
        }
        searchViewModel.frontWidthIndex.observe(this) {
            binding.textFront.text =
                RangeUI.frontWidthRangeUis.values.toMutableList()[searchViewModel.frontWidthIndex.value
                    ?: 0]
        }
        searchViewModel.isSearchDone.observe(this) {
            if (it) {
                if (searchViewModel.housesSearch.value?.isNotEmpty() == true) {
                    searchViewModel.setSearchDone(false)
                    findNavController().navigate(R.id.actionToDetailSearchResult)
                } else {
                    showToast("không tìm thấy kết quả nào")
                }
            }
        }
        searchViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) dialog.show() else dialog.dismiss()
        }
    }

    private fun setupRefresh() {
        binding.swipeLayout.apply {
            //binding.layoutHeader.root.viewTreeObserver.addOnGlobalLayoutListener(observerVisible)
            setOnRefreshListener {
                viewModel.getDashboard() {
                    binding.swipeLayout.isRefreshing = false
                    binding.layoutBanner.imageSlider.currentItem = 0
                }
            }
        }
    }

    private fun setupBanner() {
        isCheck = true
        binding.layoutBanner.imageSlider.adapter = adapterImage
        TabLayoutMediator(binding.tabLayout, binding.layoutBanner.imageSlider) { _, _ ->
        }.attach()
        binding.layoutBanner.imageSlider.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(runnable)
                sliderHandler.postDelayed(runnable, 3000)
            }
        })
    }

    private fun setupSearchView() {
        //binding.search.clearFocus()
    }

    private fun onItemBannerClick(banner: Banner) {
        println(banner.link)
    }

    private fun onItemHouseClick(house: House) {
        val destination = HomeFragmentDirections.actionToDetail(house)
        findNavController().navigate(destination)
    }


    private fun onChatNowClick(user: User_I) {
        println(user)
    }

    private fun onItemUserOnlineClick(user: User_I) {
        val action = MobileNavigationDirections.actionToPersonProfile(user)
        findNavController().navigate(action)
    }
}
