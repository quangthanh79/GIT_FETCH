package com.apion.apionhome.ui.geting_started

import android.app.AlertDialog
import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.databinding.FragmentVerifyPhoneForgetBinding
import com.apion.apionhome.viewmodel.UserViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class VerifyPhoneCustomFragment :
    BindingFragment<FragmentVerifyPhoneForgetBinding>(FragmentVerifyPhoneForgetBinding::inflate) {
    override val viewModel by viewModel<UserViewModel>()
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
            setVerifyPhone()
            start()
        }
    }
    private val codeSent by lazy {
        arguments?.getString("codeSent", null)
    }
    private val token: PhoneAuthProvider.ForceResendingToken? by lazy {
        arguments?.getParcelable("token") as? PhoneAuthProvider.ForceResendingToken
    }
    private val phone by lazy {
        arguments?.getString("phone", null)
    }
    private val userId by lazy {
        arguments?.getString("userId", null)
    }

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.phone = phone
        viewModel._isLoading.value = false
        timer.start()
        setListener()
    }

    fun setListener() {
        binding.btnDoneVerify.setOnClickListener {
            if (binding.otpViewForget.otp?.length != 6) {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setTitle("Thông báo")
                dialog.setMessage("Vui lòng nhập mã xác thực!")
                dialog.setPositiveButton("Đóng") { _, _ -> }
                dialog.show()
            } else {
                isValidVerify()
            }
        }
    }

    fun setVerifyPhone() {
        var mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {
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
                viewModel.codeSent.value = verfication
            }
        }
        var mAuth = FirebaseAuth.getInstance()
        var phoneTmp = ""
        phone?.let {
            if (it.length > 1) {
                var subSequence = it.subSequence(1, it.length)
                phoneTmp = "+84$subSequence"
            }
        }

        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneTmp) // Phone number to verify
            .setTimeout(120, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
        token?.let {
            options.setForceResendingToken(it)
        }
        PhoneAuthProvider.verifyPhoneNumber(options.build())
    }



    fun isValidVerify() {
        viewModel._isLoading.value = true
        val pin = binding.otpViewForget.otp.toString()
        println("CODE SENT: ")
        println(viewModel.codeSent.value)
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
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    viewModel._isLoading.value = false
                    findNavController().navigate(
                        R.id.actionToGetNewPincode, bundleOf(
                            "userId" to userId
                        )
                    )
                } else {
                    viewModel._isLoading.value = false
                    val dialog = AlertDialog.Builder(requireContext())
                    dialog.setTitle("Thông báo ")
                    dialog.setMessage("Xác thực thất bại. Vui lòng thử lại!")
                    dialog.setPositiveButton("Đóng") { _, _ -> }
                    dialog.show()
                }
            }
    }
}
