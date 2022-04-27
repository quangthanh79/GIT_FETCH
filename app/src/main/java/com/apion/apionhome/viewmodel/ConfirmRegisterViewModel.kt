package com.apion.apionhome.viewmodel

import androidx.lifecycle.MutableLiveData
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.User_I
import com.google.firebase.auth.PhoneAuthProvider
/**
 * Created by @Author: Pham Anh Tuan
 * Project : ApionHome
 * Create Time : 10:34 - 22/10/2021
 * For all issue contact me : phamanhtuan@gmail.com
 */
class ConfirmRegisterViewModel:RxViewModel(){
    val token = MutableLiveData<PhoneAuthProvider.ForceResendingToken?>()
    val codeFire = MutableLiveData<String?>()
    val phone = MutableLiveData<String?>()
    val user = MutableLiveData<User_I?>()


}