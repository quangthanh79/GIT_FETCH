package com.apion.apionhome.ui.geting_started

import android.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.R
import com.apion.apionhome.StartNavigationDirections
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.databinding.FragmentForgetPincodeBinding
import com.apion.apionhome.viewmodel.UserViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class PhoneForgetFragment :
    BindingFragment<FragmentForgetPincodeBinding>(FragmentForgetPincodeBinding::inflate) {

    override val viewModel by viewModel<UserViewModel>()
    private val mAuth = FirebaseAuth.getInstance()
    private var token: PhoneAuthProvider.ForceResendingToken? = null
    val mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks by lazy {
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                println(p0)
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setTitle("Thông báo")
                dialog.setMessage(p0.message)
                dialog.setPositiveButton("Đóng") { _, _ ->
                    viewModel._isLoading.value = false
                }
                dialog.show()
            }

            override fun onCodeSent(
                verfication: String,
                p1: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verfication, p1)
                token = p1
                viewModel._isLoading.value = false
                viewModel.codeSent.value = verfication
                viewModel.textCheckExistPhone.value = null
                viewModel.checkExistPhone.value = null
                findNavController().navigate(
                    R.id.acttionToVerifyCustom, bundleOf(
                        "codeSent" to verfication,
                        "token" to p1,
                        "phone" to viewModel.phoneRegister.value,
                        "userId" to viewModel.user.value?.id?.toString()
                    )
                )
            }
        }
    }

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.forgetVM = viewModel
        setupListener()
        setupObserver()
    }
    private fun setupObserver(){
        if (!viewModel.checkExistPhone.hasActiveObservers()) {
            viewModel.checkExistPhone.observe(this) {
                if ("Số điện thoại này đã tồn tại." == viewModel.textCheckExistPhone.value && it == true) {
                    verify()
                } else if ("Số điện thoại này không tồn tại trên hệ thống." == viewModel.textCheckExistPhone.value
                    && it == false
                ) {
                    showDialogCustom(
                        null,
                        viewModel.textCheckExistPhone.value
                            ?: "Số điện thoại này không tồn tại trên hệ thông!",
                        null
                    ) {
                    }
                } else if (
                    "Có lỗi xảy ra." == viewModel.textCheckExistPhone.value
                    && it == false
                ) {
                    showDialogCustom(
                        null,
                        viewModel.textCheckExistPhone.value
                            ?: "Có lỗi xảy ra!",
                        null
                    ) {
                    }
                }
            }
        }
    }
    private fun setupListener() {
        binding.imageBackForget.setOnClickListener {
            this.findNavController().popBackStack()
        }
        binding.btnSentForget.setOnClickListener {
            if (viewModel.phoneRegister.value.isNullOrBlank()) {
                viewModel.phoneRule.value = "Vui lòng nhập số điện thoại"
            } else {
                viewModel.checkExits()
            }
//            this.findNavController().navigate(R.id.actionToGetNewPincode)
        }
    }

    private fun verify() {
        viewModel.phoneRegister.value?.let {
            viewModel._isLoading.value = true
            var length = it.length
            var phone = ""
            if (length > 1) {
                var subSequence = it.subSequence(1, length)
                phone = "+84$subSequence"
            }
            val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone) // Phone number to verify
                .setTimeout(120, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(requireActivity()) // Activity (for callback binding)
                .setCallbacks(mCallbacks)
            token?.let {
                options.setForceResendingToken(it)
            }
            PhoneAuthProvider.verifyPhoneNumber(options.build())
        }
    }
}
