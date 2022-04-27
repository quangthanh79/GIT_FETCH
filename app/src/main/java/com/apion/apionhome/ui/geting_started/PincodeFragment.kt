package com.apion.apionhome.ui.geting_started

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apion.apionhome.MyApplication
import com.apion.apionhome.R
import com.apion.apionhome.StartNavigationDirections
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.User_I
import com.apion.apionhome.databinding.FragmentPincodeBinding
import com.apion.apionhome.viewmodel.LoginViewModel
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PincodeFragment : BindingFragment<FragmentPincodeBinding>(FragmentPincodeBinding::inflate) {

    override val viewModel by viewModel<LoginViewModel>()

    override fun setupView() {
        binding.lifecycleOwner = this
        setupListener()
        viewModel.errorLogin.observe(this) {
            showDialog2("Đăng nhập lỗi!", it ?: "")
        }
    }


    private fun setupListener() {
        binding.buttonlogin.setOnClickListener {
            if (binding.layoutPincode.otp?.length != 4) {
                showDialog2("Thông báo", "Vui lòng nhập đầy đủ mã pin!")
            } else {
                isValidPinCode()
            }
        }
        binding.txtOut.setOnClickListener {
            viewModel.updatePhoneNearest(null)
            viewModel.updatePincodeNearest(null)

            val dialog = AlertDialog.Builder(requireContext())
            dialog.setTitle("Thông báo")
            dialog.setMessage("Bạn có muốn đăng xuất?")
            dialog.setPositiveButton("Đăng xuất") { _, _ ->
                this.findNavController().navigate(StartNavigationDirections.actionToLoginStart())
            }

            dialog.setNegativeButton("Đóng") { _, _ ->
            }
            dialog.show()

            println("BAN VUA CLICK OUT")
        }
    }

    private fun isValidPinCode() {
        var user: User_I? = null
        val phone = arguments?.getString("phone", "") ?: ""
        val flag = arguments?.getString("flag", "") ?: ""
        val pin = binding.layoutPincode.otp.toString()
        if (flag.equals("fromVerify")) {
            viewModel.login(phone, pin) {
                dialog.show()
                println("Chuẩn bị sang màn Verify-> Main")
                viewModel.updatePhoneNearest(phone)
                viewModel.updatePincodeNearest(pin)
                findNavController().navigate(PincodeFragmentDirections.actionToMain())
                activity?.finish()
            }
        } else {
            if (flag.equals("fromStart")) {
                var houseId = activity?.intent?.getStringExtra("house_id")
                if (houseId == null) {
                    arguments?.getString("house_id", "") ?: ""
                }
                viewModel.login(phone, pin) {
                    println("PHONE_PINCODE ${phone}")
                    view?.post {
                        activity?.let {
                            dialog.dismiss()
                        }
                        viewModel.updatePhoneNearest(phone)
                        viewModel.updatePincodeNearest(pin)
                        findNavController().navigate(
                            R.id.actionToMain,
                            bundleOf("house_id" to houseId)
                        )
                        activity?.finish()
                    }
                }
            }
//
//            if (flag.equals("fromLogin")) {
//                MyApplication.sessionUser.value = user
//                findNavController().navigate(PincodeFragmentDirections.actionToMain())
//                activity?.finish()
//            }
        }

    }


    override fun onDestroy() {
        activity?.let {
            dialog.hide()
        }
        super.onDestroy()
        activity?.finish()
    }
}
