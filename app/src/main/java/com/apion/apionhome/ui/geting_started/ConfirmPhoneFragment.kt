package com.apion.apionhome.ui.geting_started

import android.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.databinding.FragmentConfirmPhoneBinding
import com.apion.apionhome.viewmodel.ConfirmPhoneViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class ConfirmPhoneFragment :  BindingFragment<FragmentConfirmPhoneBinding>(FragmentConfirmPhoneBinding::inflate){
    override val viewModel by sharedViewModel<ConfirmPhoneViewModel>()

    private val phone : String? by lazy{
        arguments?.getString("phone")
    }

    override fun setupView() {
        setupListener()
        setupTextPhone()
    }
    fun getPhoneCofirm(): String {
        phone?.let {
            var length = it.length
            if (length > 1) {
                var subSequence = it.subSequence(1, length)
                return "+84" + subSequence
            }
        }
        return ""
    }
    fun setupTextPhone(){
        var result = ""
        phone?.let{
            if(it.length >=7){
                result = "" + it.subSequence(1,4)+" "+it.subSequence(4,7)+" "+it.subSequence(7,it.length)
                binding.txtPhone2.text = result
            }
        }
    }
    fun setupListener(){
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnDoneConfirm.setOnClickListener {
            setVerifyPhone()

        }
    }

    fun setVerifyPhone() {
        viewModel._isLoading.value = true
        var mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                viewModel._isLoading.value = false
                println(p0)
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setTitle("Thông báo")
                dialog.setMessage(p0.message)
                dialog.setPositiveButton("Đóng") { _, _ ->
                }
                dialog.show()
            }

            override fun onCodeSent(
                verfication: String,
                p1: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verfication, p1)
                viewModel._isLoading.value = false
                viewModel.codeFire.value = verfication
                viewModel.token.value    = p1
                findNavController().popBackStack()
            }
        }

        var mAuth = FirebaseAuth.getInstance()

        phone?.let{
            val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(getPhoneCofirm()) // Phone number to verify
                .setTimeout(120, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(requireActivity()) // Activity (for callback binding)
                .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
            viewModel.token.value?.let {
                options.setForceResendingToken(it)
            }
            PhoneAuthProvider.verifyPhoneNumber(options.build())
        }

    }
}