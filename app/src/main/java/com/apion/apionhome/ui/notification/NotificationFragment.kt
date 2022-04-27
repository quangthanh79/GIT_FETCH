package com.apion.apionhome.ui.notification

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.MobileNavigationDirections
import com.apion.apionhome.MyApplication
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.data.model.House
import com.apion.apionhome.databinding.FragmentNotificationBinding
import com.apion.apionhome.ui.adapter.NotificationAdapter
import com.apion.apionhome.viewmodel.HouseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment :
    BindingFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {

    override val viewModel by viewModel<HouseViewModel>()
    private val adapter = NotificationAdapter(::onItemClick)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.sessionUser.observe(this){
            if(it?.id != null){
                viewModel.getNotification(null)
            }
        }
    }

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.notificationVM = viewModel
        binding.recyclerViewNotification.adapter = adapter
        setupRefresh()
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
    private fun setupRefresh(){
        binding.notificationRefresh.apply {
            //binding.layoutHeader.root.viewTreeObserver.addOnGlobalLayoutListener(observerVisible)
            setOnRefreshListener {
                viewModel.getNotification {
                   this.isRefreshing = false
                }
            }
        }
    }

    private fun onItemClick(house: House) {
        val action = MobileNavigationDirections.actionMainToDetail(house)
        findNavController().navigate(action)
    }
}