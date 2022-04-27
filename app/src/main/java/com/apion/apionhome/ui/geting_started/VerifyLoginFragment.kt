package com.apion.apionhome.ui.geting_started

import android.app.AlertDialog
import android.os.CountDownTimer
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.databinding.FragmentVerifyPhoneLoginBinding
import com.apion.apionhome.viewmodel.ConfirmPhoneViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class VerifyLoginFragment :
    BindingFragment<FragmentVerifyPhoneLoginBinding>(FragmentVerifyPhoneLoginBinding::inflate) {
    //    override val viewModel by viewModel<VerifyPhoneViewModel>()
    override val viewModel by sharedViewModel<ConfirmPhoneViewModel>()

    val timer = object : CountDownTimer(120000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            val second = millisUntilFinished / 1000
            var minute = second / 60
            if (second % 60 < 10) {
                binding.txtWarning.setText("Gửi lại mã xác thực sau: 0" + minute + ":0" + second % 60)
            } else {
                binding.txtWarning.setText("Gửi lại mã xác thực sau: 0" + minute + ":" + second % 60)
            }

        }

        override fun onFinish() {
//            setVerifyPhone()
        }
    }

    override fun setupView() {
        timer.start()
        setupTitlePhone()
        setupListener()
    }


    fun setupTitlePhone(){
        val phone    = arguments?.getString("phone", "") ?: ""
        var result = ""
        phone?.let{
            if(it.length >=7){
                result = "+84 " + phone.subSequence(1,4)+" "+phone.subSequence(4,7)+" "+phone.subSequence(7,phone.length)
                binding.textPhone.text = result
            }
        }
    }

    fun setupListener() {
        binding.btnDoneVerify.setOnClickListener {
            if (binding.layoutOTP.otp?.length != 6) {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setTitle("Thông báo")
                dialog.setMessage("Vui lòng nhập đủ 6 mã xác thực!")
                dialog.setPositiveButton("Đóng") { dialogShow, _ ->
                    dialogShow.dismiss()
                }
                dialog.show()
            } else {
                isValidVerify()
            }
        }
        binding.txtAnswer.setOnClickListener {
            val phone = arguments?.getString("phone", "") ?: ""
            findNavController().navigate(
                R.id.actionToConfirmPhone,
                bundleOf(
                    "phone" to phone,
                )
            )
        }
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun isValidVerify() {
        val codeSent = arguments?.getString("codeSent", "") ?: ""
        viewModel._isLoading.value = true
        val pin = binding.layoutOTP.otp.toString()
        println("CODE SENT: ")
        println(codeSent)
        codeSent?.let {
            val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(it, pin)
            signIn(credential)
        }

    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }
    private fun signIn(credential: PhoneAuthCredential) {
        var mAuth = FirebaseAuth.getInstance()
        val isFirst = arguments?.getString("isFirst", "") ?: ""

        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    //xác thực thành công
                    viewModel._isLoading.value = false
                    // chưa bao giờ đăng nhập trên ứng dụng, tạo pincode, gửi bundle id
                    if (isFirst == "1") {
                        timer.cancel()
                        val phone = arguments?.getString("phone", "") ?: ""
                        val id = arguments?.getString("id", "") ?: ""

                        findNavController().navigate(
                            R.id.actionToVerifyPincodeStartFragment,
                            bundleOf(
                                "phone" to phone,
                                "id" to id
                            )
                        )
                        println("CHUAN BI SANG MAN START PINCODE")
                    }
                    else {
                        // đã từng đăng nhập trên dứng dụng, nhập pincode
                        timer.cancel()
                        val phone = arguments?.getString("phone", "") ?: ""
                        println("VerifyLoginFragment da dang nhap tren ung dung phone:${phone}")
                        findNavController().navigate(
                            R.id.actionToPinCode,
                            bundleOf(
                                "phone" to phone,
                                "flag" to "fromVerify"
                            )
                        )
                        println("CHUAN BI SANG MAN PINCODE")
                    }
                }
                else {
                    // xác thực thất bại
                    viewModel._isLoading.value = false
                    val dialog = AlertDialog.Builder(requireContext())
                    dialog.setTitle("Thông báo ")
                    dialog.setMessage("Xác thực thất bại. Vui lòng thử lại!")
                    dialog.setPositiveButton("Đóng") { _, _ ->
                        timer.cancel()
                        findNavController().popBackStack(R.id.loginFragment, false)
                    }
                    dialog.show()
                }
            }

    }
}