package com.apion.apionhome.ui.geting_started

import android.app.AlertDialog
import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.R
import com.apion.apionhome.StartNavigationDirections
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.data.model.User_I
import com.apion.apionhome.databinding.FragmentVerifyPhoneBinding
import com.apion.apionhome.viewmodel.ConfirmRegisterViewModel
import com.apion.apionhome.viewmodel.LoginViewModel
import com.apion.apionhome.viewmodel.UserViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class VerifyPhoneFragment : BindingFragment<FragmentVerifyPhoneBinding>(FragmentVerifyPhoneBinding::inflate) {
    override val viewModel by viewModel<UserViewModel>()
    val confirm_viewModel by sharedViewModel<ConfirmRegisterViewModel>()


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
        }
    }

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.verifyVM = viewModel
        viewModel._isLoading.value = false
        viewModel.phoneRegister.value = confirm_viewModel.phone.value// set title phone


        timer.start()

        println("CHECK")
        setListener()
        setupObserver()
    }

    fun setListener() {
        binding.btnDoneVerify.setOnClickListener {
            if(binding.otpView.otp?.length != 6){
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setTitle("Thông báo")
                dialog.setMessage("Vui lòng nhập mã xác thực!")
                dialog.setPositiveButton("Đóng"){ _, _ -> }
                dialog.show()
            }else{
                isValidVerify()
            }
        }
        binding.txtAnswer.setOnClickListener {
            findNavController().navigate(R.id.actionToConfirmRegister)
        }
    }

    fun setVerifyPhone() {
        var mCallbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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
            override fun onCodeSent(verfication: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verfication, p1)
                confirm_viewModel.codeFire.value   =  verfication
                confirm_viewModel.token.value      =  p1
            }
        }
        var mAuth = FirebaseAuth.getInstance()
//        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true)
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(viewModel.getPhoneFirebase()) // Phone number to verify
            .setTimeout(120, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun setupObserver() {
        viewModel.registerSuccess.observe(this) {
            setDialogCreateSuccess()
        }
        viewModel.errorRegister.observe(this) {
            it?.let {
                setRegisterFailed(it)
            }
//            println("ĐÃ CÓ LỖI XẢY RA GỬI API")
        }
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }
    private fun setRegisterFailed(error: String) {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Thông báo")
        dialog.setMessage(error)
        dialog.setPositiveButton("Đóng") { _, _ ->
            findNavController().popBackStack()
        }
        dialog.show()
    }

    private fun setDialogCreateSuccess() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Thông báo")
        dialog.setMessage("Đăng ký thành công.")
        dialog.setPositiveButton("Đóng") { _, _ ->
            this.findNavController().navigate(StartNavigationDirections.actionToLoginStart())
        }
        dialog.show()
    }


    fun isValidVerify() {
        viewModel._isLoading.value = true
        val pin = binding.otpView.otp.toString()
        println("CODE SENT: ${confirm_viewModel.codeFire.value!!}")
        confirm_viewModel.codeFire.value?.let {
            val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(it, pin)
            signIn(credential)
        }

    }

    private fun signIn(credential: PhoneAuthCredential) {
        var mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    println("Xác thực thành công")
                    confirm_viewModel.user.value?.let {
                        viewModel.register(it)
                    }
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


    class GenericKeyEvent internal constructor(
        private val nextView: EditText?,
        private val currentView: EditText,
        private val previousView: EditText?,
    ) : View.OnKeyListener {

        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (currentView.id == R.id.etPassword1) {
                currentView.isCursorVisible = true

            }

            if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.etPassword1 && currentView.text.isEmpty()) {
                //If current is empty then previous EditText's number will also be deleted
                previousView!!.text = null
                previousView.isCursorVisible = true
                previousView.isEnabled = true
                previousView.requestFocus()

                currentView.isEnabled = false

                return true
            }

            if (event!!.action == KeyEvent.ACTION_UP &&currentView.id != R.id.etPassword6 && !currentView.text.isEmpty()) {
                nextView!!.isEnabled = true
                if(currentView.id == R.id.etPassword5){
                    nextView!!.isCursorVisible = true
                }else{
                    nextView!!.isCursorVisible = true
                }

                nextView!!.requestFocus()

                currentView.isCursorVisible = true
                currentView.isEnabled = false


                return false
//                previousView.requestFocus()
            }


            return false
        }


    }
}
