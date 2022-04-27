package com.apion.apionhome.viewmodel

import androidx.lifecycle.MutableLiveData
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.User_I
import com.google.firebase.auth.PhoneAuthProvider

class ConfirmPhoneViewModel: RxViewModel() {
    val token = MutableLiveData<PhoneAuthProvider.ForceResendingToken?>()
    val codeFire = MutableLiveData<String?>()
}