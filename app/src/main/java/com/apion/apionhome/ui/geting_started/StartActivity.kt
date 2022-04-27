package com.apion.apionhome.ui.geting_started

import android.content.Intent
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingActivity
import com.apion.apionhome.databinding.ActivityStartBinding
import com.apion.apionhome.ui.MainActivity
import com.apion.apionhome.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartActivity : BindingActivity<ActivityStartBinding>() {

    val viewModel by viewModel<LoginViewModel>()
    private val navController by lazy {
        this.findNavController(R.id.nav_host_fragment_activity_start)
    }

    override fun getLayoutResId() = R.layout.activity_start
    override fun setupView() {
        setupToCurrentTab()
        println("OK")
    }

    private fun setupToCurrentTab() {
        var intent = intent

        var destination = intent.getStringExtra("destination")
        val houseId = intent.getStringExtra("house_id")

        if(destination.equals("SPLASH")){
            if (viewModel.getPhoneNearest() == null) {
                startActivity(Intent(this, MainActivity::class.java).apply {
                    putExtra("house_id", houseId)
                })
                finish()
            } else {
                    navController.navigate(
                        R.id.actionToPinCode, bundleOf(
                            "flag" to "fromStart",
                            "phone" to viewModel.getPhoneNearest(),
                            "pincode" to viewModel.getPincodeNearest(),
                            "house_id" to houseId
                        )
                    )
            }
        }
        else if(destination.equals("MAIN")){
//            viewModel.fetchUser(viewModel.getPhoneNearest()!!) {
//                navController.navigate(
//                    R.id.actionToPinCode, bundleOf(
//                        "flag" to "fromStart",
//                        "phone" to viewModel.getPhoneNearest(),
//                        "user" to it
//                    )
//                )
//            }
        }

        println("START ACTIVITY~~cPHONE NEAREST: ${viewModel.getPhoneNearest()}")
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.verifyPhoneFragment) {
            navController.popBackStack(R.id.registerFragment, false)
        } else {
            super.onBackPressed()
        }
    }
}