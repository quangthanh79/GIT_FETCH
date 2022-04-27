package com.apion.apionhome.viewmodel

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
import java.util.*

class ProfileViewModel(val userRepository: UserRepository) : RxViewModel() {
    val email = MutableLiveData<String>()
    val fbId = MutableLiveData<String?>()
    val refer = MutableLiveData<String?>()
    val dob = MutableLiveData<Calendar?>(null)
    val sex = MutableLiveData<String?>()
    val name = MutableLiveData<String?>()
    val address = MutableLiveData<Calendar?>(null)
    val bios = MutableLiveData<String?>()
    val phone = MutableLiveData<String?>()

    val updateDone = MutableLiveData<Boolean>(false)
    val isLogout = MutableLiveData<Boolean>(false)
    val updatePinCodeDone = MutableLiveData<Boolean>(false)

    val pin = MutableLiveData<String>()
    val pin1 = MutableLiveData<String?>()
    val pin2 = MutableLiveData<String?>()

    fun getSexString(): String {
        return if (sex.value == "Male") "Nam" else "Nữ"
    }

    fun updatePhoneNearst(phone: String?) {
        userRepository.updatePhoneNearest(phone)
    }

    fun getDobAPI(): String? {
        var result: String? = null
        dob.value?.let {
            var date = it.time
            result = date.toString(TimeFormat.TIME_FORMAT_API_1)
        }
        return result
    }

    fun getDobShow(): String? {
        var result: String? = null
        dob.value?.let {
            var date = it.time
            result = date.toString(TimeFormat.DATE_FORMAT)
        }
        return result
    }

    val rulePin = pin.transform {
//        && it != MyApplication.sessionUser.value?.pincode
        if (it.length == 4) {
            "Pin code không đúng"
        } else if (it.isNullOrBlank()) {
            "Vui lòng nhập lại mật khẩu"
        } else if (it.length != 4) {
            "Vui lòng nhập 4 số"
        } else {
            null
        }
    }

    val rulePin1 = pin1.transform {
        if (it.isNullOrBlank()) {
            "Vui lòng nhập mật khẩu"
        } else if (it.length != 4) {
            "Vui lòng nhập 4 số"
        } else {
            null
        }
    }

    val rulePin2 = pin2.transform {
        if (it.isNullOrBlank()) {
            "Vui lòng nhập lại mật khẩu"
        } else if (it.length == pin1.value?.length && it != pin1.value) {
            "Mật khẩu không khớp"
        } else if (it.length != 4) {
            "Vui lòng nhập 4 số"
        } else {
            null
        }
    }

    val emailRule = email.transform {
        if (it.isNullOrBlank() || it.isEmailValid) null else "Định dạng không hợp lệ!"
    }

    val referRule = refer.transform {
        if (it.isNullOrBlank() || it.isPhoneValid) null else "Định dạng không hợp lệ!"
    }

    fun fetchUser(userId: Int) {
        userRepository
            .getUserById(userId)
            .setup()
            .subscribe(
                {
                    MyApplication.sessionUser.value = it
                }, {
                    it.printStackTrace()
                    error.value = it.message
                }
            )
    }

    fun getUserById(userId: Int, onDone: (User_I) -> Unit) {
        _isLoading.value = true
        userRepository
            .getUserById(userId)
            .setup()
            .doOnTerminate {
                _isLoading.value = false
            }
            .subscribe(
                {
                    onDone(it)
                }, {
                    it.printStackTrace()
                    error.value = it.message
                }
            )
    }

    fun uploadAvatar(userId: Int, avatar: String) {
        _isLoading.value = true
        userRepository
            .uploadAvatar(userId, avatar)
            .setup()
            .doOnTerminate {
                _isLoading.value = false
            }
            .subscribe(
                {
                    fetchUser(userId)
                    _isLoading.value = false
                }, {
                    it.printStackTrace()
                    error.value = it.message
                    _isLoading.value = false
                }
            )
    }


    fun logout(userId: Int, phone: String) {
        userRepository
            .logout(userId, phone)
            .setup()
            .subscribe(
                {
                    isLogout.value = true
                    MyApplication.sessionUser.value = null
                    MyApplication.houseNavigate.value = null
                    MyApplication.profileUserNavigate.value = null
                    MyApplication.tabToNavigate.value = null
                }, {
                    it.printStackTrace()
                    error.value = it.message
                    println("DANG XUAT THAT BAI")
                    println("CAUSE: ${error.value}")
                }
            )
    }

    fun updateUser(user: User_I?, onDone: () -> Unit) {
        if (email.value.isNullOrBlank()) {
            emailRule.value = "Vui lòng nhập email"
            return
        }
        if (refer.value.isNullOrBlank()) {
            referRule.value = "Vui lòng nhập số điện thoại người giới thiệu"
            return
        }
        if (emailRule.value == null && referRule.value == null) {
            user?.let {
                _isLoading.value = true
                val userTmp = user.copy()
                userTmp.email = email.value
                userTmp.facebook_id = fbId.value
                userRepository
                    .updateUser(userTmp)
                    .setup()
                    .doOnTerminate {
                        _isLoading.value = false
                    }
                    .subscribe(
                        {
                            updateDone.value = true
                            _isLoading.value = false
                            onDone()
                            fetchUser(user.id ?: -1)
                        }, {
                            it.printStackTrace()
                            error.value = it.message
                            _isLoading.value = false
                        }
                    )
            }
        }
    }

    fun updatePin(id: String, onDone: () -> Unit) {
        if (rulePin1.value == null && rulePin2.value == null) {
            _isLoading.value = true
            userRepository
                .updatePincode(id, pin1.value!!)
                .setup()
                .doOnTerminate {
                    _isLoading.value = false
                }
                .subscribe(
                    {
                        updatePinCodeDone.value = true
                        onDone()
                    }, {
                        it.printStackTrace()
                        error.value = it.message
                    }
                )
        }
    }

    fun changePin(id: String, onDone: () -> Unit) {
        if (rulePin.value == null && rulePin1.value == null && rulePin2.value == null) {
            _isLoading.value = true
            userRepository
                .updatePincode(id, pin1.value!!)
                .setup()
                .doOnTerminate {
                    _isLoading.value = false
                }
                .subscribe(
                    {
                        onDone()
                    }, {
                        it.printStackTrace()
                        error.value = it.message
                    }
                )
        }
    }
}