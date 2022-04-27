package com.apion.apionhome.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apion.apionhome.MyApplication
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.User_I
import com.apion.apionhome.data.repository.UserRepository
import com.apion.apionhome.utils.*
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception

class LoginViewModel(val userRepository: UserRepository) : RxViewModel() {


    // khởi tạo biến _users, khai báo users  và gán _users cho nó
    private val _users = MutableLiveData<List<User_I>>()
    val users: LiveData<List<User_I>>
        get() = _users

    private val _user = MutableLiveData<User_I?>()
    val user: LiveData<User_I?>
        get() = _user


    private val _pincode = MutableLiveData<String>()
    val pincode: LiveData<String>
        get() = _pincode

    val codeSent = MutableLiveData<String>()
    val isFirst = MutableLiveData<Int?>(0)

    // khởi tạo biến _loginSuccess, khai báo loginSuccess  và gán _loginSuccess cho nó
    val _loginSuccess = MutableLiveData<Boolean?>()
    val loginSuccess: LiveData<Boolean?>
        get() = _loginSuccess

    val _checkExitSuccess = MutableLiveData<Boolean?>()
    val checkExitSuccess: LiveData<Boolean?>
        get() = _checkExitSuccess




    var _isCreateDone = MutableLiveData<Boolean>()

    val isCreateDone: LiveData<Boolean>
        get() = _isCreateDone

    val phone = MutableLiveData<String>()

    val errorText = MutableLiveData<String?>()
    val errorLogin = MutableLiveData<String?>()


    fun setError(error: String?) {
        errorText.value = error
    }


    fun getPhoneLogin(): String {
        phone.value?.let {
            var length = it.length
            if (length > 1) {
                var subSequence = it.subSequence(1, length)
                return "+84" + subSequence
            }
        }


        return ""
    }

    fun getPincodeNearest(): String? {
        return userRepository.getPincodeNearest()
    }

    fun updatePincodeNearest(pincode: String?) {
        userRepository.updatePincodeNearest(pincode)
    }

    fun updatePhoneNearest(phone: String?) {
        userRepository.updatePhoneNearest(phone)
    }

    fun getPhoneNearest(): String? {
        return userRepository.getPhoneNearest()
    }

    fun fetchUser(userId: Int, onDone: ()->Unit) {
        userRepository
            .getUserById(userId)
            .setup()
            .subscribe(
                {
//                    _isLoading.value = false
                    MyApplication.sessionUser.value = it
                    onDone()
                }, {
                    _isLoading.value = false
                    it.printStackTrace()
                    error.value = it.message
                }
            )
    }


    fun login(phone: String,pincode: String,onDone: () -> Unit) {
        _isLoading.value = true
        userRepository
            .login(phone,pincode)
            .setup()
            .subscribe(
                {
                    println("ĐĂNG NHẬP THÀNH CÔNG")
                    MyApplication.accesstoken.value = it.accessToken
                    it.id?.let{
                        fetchUser(it,onDone)
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
                            println("LOI HTTPEXCEPTION ")
                            errorLogin.value = jsonError.getString("message")
                        } catch (e: Exception) {
                            e.printStackTrace()
                            println("LOI HTTPEXCEPTION ")
                            errorLogin.value = it.message
                        }
                    } else {
                        errorLogin.value = it.message
                        println("LOI KHONG ")
                        println("CAUSE: ${errorLogin.value}")

                    }
                    _isLoading.value = false
                }
            )

    }

//
    fun checkExist() {
        // ? để check null. khi null thì ko thực hiện scope function apply
        //
        validatePhone()?.apply {
            userRepository
                .checkExist(this)
                .setup()
                .doOnTerminate {
                    _isLoading.value = false
                }
                .subscribe(
                    {
                        _user.value = it
                        isFirst.value = it.isFirst
                        _checkExitSuccess.value = true

                        it.following?.forEach {
                            FirebaseMessaging.getInstance()
                                .subscribeToTopic("ApionHome${it.beingFollowedId}")
                                .addOnCompleteListener { task ->
                                    if (!task.isSuccessful) {
                                        task.exception?.printStackTrace()
//                                        throw IllegalArgumentException(task.exception)
                                    }
                                }
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
                                errorLogin.value = jsonError.getString("message")
                            } catch (e: Exception) {
                                e.printStackTrace()
                                errorLogin.value = it.message
                            }
                        } else {
                            errorLogin.value = it.message

                        }
                    }
                )
        }

    }

    private fun validatePhone(): String? {
        val phoneValue = phone.value
        when {
            phoneValue.isNullOrBlank() -> errorText.value = "Yêu cầu nhập số điện thoại!"
            errorText.value == null -> {
                _isLoading.value = true
                return phoneValue
            }

        }
        return null
    }

}
