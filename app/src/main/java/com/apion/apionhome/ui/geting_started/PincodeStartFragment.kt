package com.apion.apionhome.ui.geting_started

import android.content.Context
import android.content.SharedPreferences
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.databinding.FragmentStartPincodeBinding
import com.apion.apionhome.viewmodel.UpdatePincodeViewModel
import com.apion.apionhome.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PincodeStartFragment : BindingFragment<FragmentStartPincodeBinding>(FragmentStartPincodeBinding::inflate) {
    override val viewModel by viewModel<UpdatePincodeViewModel>()

    override fun setupView() {
        binding.lifecycleOwner = this
        setupListener()
        setupObserver()
    }
    fun setupObserver(){
//        viewModel.updatePinCodeDone.observe(this){
//            viewModel.updatePinCodeDone.value?.let{
//                if(it){
//                    val phone    = arguments?.getString("phone", "") ?: ""
//                    val id    = arguments?.getString("id", "") ?: ""
//
//                    println("PHONE TO SHAREPHERENCE ${phone}")
//                    viewModel.updatePhoneNearst(phone)
//                    viewModel.updatePincodeNearest(viewModel.pincodeNew.value)
//                    findNavController().navigate(R.id.actionToMain)
//                    activity?.finish()
//                }
//            }
//        }
        viewModel.errorCreate.observe(this){
            showDialog2("Lỗi tạo tài khoản!", it?:"")
        }
    }
    fun setupListener(){
        val phone    = arguments?.getString("phone", "") ?: ""

        binding.buttonCreate.setOnClickListener {
            if(binding.layoutPincodeStart.otp?.length != 4){
                showDialog2("Thông báo","Vui lòng tạo mã pin!")
            }else{

                var idUser = arguments?.getString("idUser","")?: ""
                var pin    = binding.layoutPincodeStart.otp.toString()
//                viewModel.updatePin(idUser,pin){
//
//                    viewModel.updatePhoneNearst(phone)
//                    viewModel.updatePincodeNearest(pin)
//                    findNavController().navigate(R.id.actionToMain)
//                    activity?.finish()
//                }
                viewModel.createPincode(phone,pin){
                    viewModel.updatePhoneNearst(phone)
                    viewModel.updatePincodeNearest(pin)
                    println("Chuẩn bị sang màn MAin")
                    findNavController().navigate(R.id.actionToMain)
                    activity?.finish()
                }
            }
        }
        binding.txtCancel.setOnClickListener {
            this.findNavController().navigate(R.id.actionToLogin)
        }

    }

}