package com.apion.apionhome.ui.geting_started

import android.R.attr.phoneNumber
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.MobileNavigationDirections
import com.apion.apionhome.MyApplication
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.databinding.FragmentRegisterBinding
import com.apion.apionhome.utils.createProgressDialog
import com.apion.apionhome.utils.isNameValid
import com.apion.apionhome.utils.isPhoneValid
import com.apion.apionhome.viewmodel.ConfirmPhoneViewModel
import com.apion.apionhome.viewmodel.ConfirmRegisterViewModel
import com.apion.apionhome.viewmodel.UserViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit


class RegisterFragment :
    BindingFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {
    override val viewModel by viewModel<UserViewModel>()
    val viewModelAddress by sharedViewModel<UserViewModel>()
    val confirm_viewModel by sharedViewModel<ConfirmRegisterViewModel>()

    val calendar = Calendar.getInstance()
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var mAuth: FirebaseAuth
    private var isNavigateToVerify = false

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.registerVM = viewModel
        viewModel._isCreateDone.value = false
        isNavigateToVerify = false
        println(viewModel.province.value)
        setupListerner()
        setObserve()
        setRadio()
    }

    fun setupListerner() {
        binding.imageBack.setOnClickListener {
            this.findNavController().popBackStack()
            println("OK BACK")
        }


        binding.btnRegister.setOnClickListener {
            println("NAME: ${viewModel.nameRegister.value}")
            println("PHONE: ${viewModel.phoneRegister}")
            setupCheck()
            viewModel.setCreateDone()
            println("CHECK REGISTER: ${viewModel.isCreateDone.value}")

//            println("OK da vao day")
        }

        binding.edtDateOfBirth.setOnClickListener {
            setupPickerDialog()
        }
        binding.edtAddress.setOnClickListener {
            this.findNavController().navigate(R.id.actionToSelectLocation)
        }
        binding.icDeleteAddress.setOnClickListener {
//            binding.edtAddress.setText(getString(R.string.text_select_address))
            viewModel.setDistrict(null)
            viewModel.setProvince(null)
        }

    }

    fun setRadio() {
        binding.radioGroup2.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.radio_male -> viewModel.setSexIndex(0)
                R.id.radio_female -> viewModel.setSexIndex(1)
            }
        }
        binding.radioGroupJob.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.radio_otherJob -> viewModel.setJobIndex(1)
                R.id.radio_findJob -> viewModel.setJobIndex(0)
            }
        }
        binding.radioCollege.setOnClickListener({
            viewModel.setLevelIndex(1)
            binding.radioUniversity.isChecked = false
            binding.radioIntermediate.isChecked = false
            binding.radioHigh.isChecked = false
        })
        binding.radioUniversity.setOnClickListener({
            viewModel.setLevelIndex(0)
            binding.radioCollege.isChecked = false
            binding.radioIntermediate.isChecked = false
            binding.radioHigh.isChecked = false
        })
        binding.radioIntermediate.setOnClickListener({
            viewModel.setLevelIndex(2)
            binding.radioCollege.isChecked = false
            binding.radioUniversity.isChecked = false
            binding.radioHigh.isChecked = false
        })
        binding.radioHigh.setOnClickListener({
            viewModel.setLevelIndex(3)
            binding.radioCollege.isChecked = false
            binding.radioIntermediate.isChecked = false
            binding.radioUniversity.isChecked = false
        })

    }

    private fun verificationCallbacks() {
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                println(p0)
                viewModel._isLoading.value = false
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
                viewModel.checkExistPhone.value = null
                viewModel._isCreateDone.value = false

                confirm_viewModel.codeFire.value = verfication
                confirm_viewModel.token.value    = p1
                confirm_viewModel.phone.value    = viewModel.phoneRegister.value
                confirm_viewModel.user.value     = viewModel.getUser()
                findNavController().navigate(R.id.actionToVerifyPhone)
                println("confirm_viewModel.phone.value : ${confirm_viewModel.phone.value}")
            }
        }
    }

    private fun verify() {
        viewModel._isLoading.value = true
        verificationCallbacks()
        mAuth = FirebaseAuth.getInstance()
        println("PHONE FIREBASE ${viewModel.getPhoneFirebase()}")
//        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true)
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(viewModel.getPhoneFirebase()) // Phone number to verify
            .setTimeout(120, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
        confirm_viewModel.token.value?.let {
            options.setForceResendingToken(it)
        }
        PhoneAuthProvider.verifyPhoneNumber(options.build())
    }

    fun setupCheck() {

        val dialog = AlertDialog.Builder(requireContext())
        dialog.setPositiveButton("Đóng") { _, _ ->
        }

        var check1 = viewModel.phoneRegister.value.isNullOrBlank()
        var check2 = viewModel.nameRegister.value.isNullOrBlank()
        if (check1 && check2) {
            dialog.setMessage("Vui lòng nhập đầy đủ thông tin.")
        } else if (check1) {
            dialog.setMessage("Vui lòng nhập số điện thoại.")
        } else if (check2) {
            dialog.setMessage("Vui lòng nhập đầy đủ họ tên.")
        } else {
            return
        }
        dialog.show()
    }

    fun setObserve() {
        if (!viewModelAddress.province.hasObservers()) {
            viewModelAddress.province.observe(this) {
                if (it != null) {
                    binding.edtAddress.setText(viewModelAddress.getAddress())
                } else {
                    binding.edtAddress.setText(getString(R.string.hint_address_register))
                }
            }
        }
        if (!viewModel.isCreateDone.hasObservers()) {
            viewModel.isCreateDone.observe(this) {
                println("IS CREATE DONE: ${it}")
                if (it) {
                    println("SĐT : ${viewModel.getPhoneFirebase()}")
                    viewModel.checkExits()
                    //verify()

                }
            }
        }
        if (!viewModel.checkExistPhone.hasObservers()) {
            viewModel.checkExistPhone.observe(this) {


                it?.let {
                    if (it) {
                        val dialog = AlertDialog.Builder(requireContext())
                        dialog.setTitle("Thông báo")
                        dialog.setMessage(viewModel.textCheckExistPhone.value)
                        dialog.setPositiveButton("Đóng") { _, _ ->
                        }
                        dialog.show()
                    } else {
                        println("DA VAO DEN VẺIFY")
                        verify()
                    }
                }
            }
        }

        if (!viewModel.phoneRegister.hasObservers()) {
            viewModel.phoneRegister.observe(this) {
                val errorTextPhone = if (it.isPhoneValid) null else "Định dạng không hợp lệ."
                viewModel.setErrorPhone(errorTextPhone)
                if (it.isPhoneValid)
                    binding.validatePhone.visibility = View.INVISIBLE
                else
                    binding.validatePhone.visibility = View.VISIBLE
                binding.validatePhone.text = "Định dạng không hợp lệ!"
            }
        }

        if (!viewModel.nameRegister.hasObservers()) {
            viewModel.nameRegister.observe(this) {
                val errorTextName =
                    if (it.isNameValid) null else "Vui lòng không nhập các ký tự đặc biệt."
                viewModel.setErrorName(errorTextName)
                if (it.isNameValid)
                    binding.validateName.visibility = View.INVISIBLE
                else
                    binding.validateName.visibility = View.VISIBLE
                binding.validateName.text = "Vui lòng không nhập các ký tự đặc biệt."

            }
        }
    }

    fun setupPickerDialog() {
        val dateSetListener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val calendar1 = Calendar.getInstance()
            calendar1.set(year, monthOfYear, dayOfMonth, 23, 59, 59)
            if (calendar1.timeInMillis > calendar.timeInMillis) {
                viewModel.dobRegister.value = calendar
            } else {
                viewModel.dobRegister.value = calendar1
            }
        }
        var datePickerDialog = DatePickerDialog(
            requireContext(),
            AlertDialog.THEME_HOLO_LIGHT,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

}