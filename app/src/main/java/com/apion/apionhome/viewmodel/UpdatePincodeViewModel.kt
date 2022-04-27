package com.apion.apionhome.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apion.apionhome.MyApplication
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.User_I
import com.apion.apionhome.data.model.local.LocationName
import com.apion.apionhome.data.repository.UserRepository
import com.apion.apionhome.utils.setup
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception

class UpdatePincodeViewModel(val userRepository: UserRepository) : RxViewModel() {
    //    var pincodeNew        =  MutableLiveData<String?>()
    var updatePinCodeDone = MutableLiveData<Boolean?>()
    val errorCreate = MutableLiveData<String?>()


    private val _user = MutableLiveData<User_I>()
    val user: LiveData<User_I>
        get() = _user

    fun fetchUser(userId: Int, onDone: () -> Unit) {
        userRepository
            .getUserById(userId)
            .setup()
            .subscribe(
                {
                    MyApplication.sessionUser.value = it
                    onDone()
                }, {
                    it.printStackTrace()
                    error.value = it.message
                }
            )
    }

    fun updatePhoneNearst(phone: String) {
        userRepository.updatePhoneNearest(phone)
    }

    fun updatePincodeNearest(pincode: String?) {
        userRepository.updatePincodeNearest(pincode)
    }


    fun createPincode(phone: String, pincode: String, onDone: () -> Unit) {
        println("CHUẨN BỊ GỌI API CREATE PINCODE")
        _isLoading.value = true
        userRepository
            .login(phone, pincode)
            .setup()
            .doOnTerminate {
                _isLoading.value = false
            }
            .subscribe(
                {
                    println("ĐÃ TẠO PIN CODE THÀNH CÔNG")
                    MyApplication.accesstoken.value = it.accessToken
                    it.id?.let {
                        fetchUser(it, onDone)
                    }
                }, {
                    if (it is HttpException) {
                        try {
                            val jsonError = JSONObject(
                                String(
                                    it.response()?.errorBody()?.bytes() ?: byteArrayOf(),
                                    charset("UTF-8")
                                )
                            )
                            errorCreate.value = jsonError.getString("message")
                        } catch (e: Exception) {
                            e.printStackTrace()
                            errorCreate.value = it.message
                        }
                    } else {
                        errorCreate.value = it.message

                    }
                }
            )

    }
}