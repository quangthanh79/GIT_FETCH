package com.apion.apionhome.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.dashboard.Dashboard
import com.apion.apionhome.data.model.local.District
import com.apion.apionhome.data.repository.HouseRepository
import com.apion.apionhome.data.repository.UserRepository
import com.apion.apionhome.utils.setup

class HomeViewModel(
    val userRepository: UserRepository,
    val houseRepository: HouseRepository
) : RxViewModel() {

    private val _dashBoard = MutableLiveData<Dashboard>()

    val dashBoard: LiveData<Dashboard>
        get() = _dashBoard

    private val _countNoti = MutableLiveData<Int>(10)

    val countNoti: LiveData<Int>
        get() = _countNoti

    private val _districts = MutableLiveData<List<District>>()

    val districts: LiveData<List<District>>
        get() = _districts

    private val _showPass = MutableLiveData<Boolean>(false)

    val showPass: LiveData<Boolean>
        get() = _showPass

    override fun initData() {
        getDashboard()
    }

    fun getImages(): List<String> = _dashBoard.value?.feature?.first()?.images ?: emptyList()

    fun setShowPass() {
        _showPass.value?.let {
            _showPass.value = !it
            return
        }
        _showPass.value = false
    }

    fun getDashboard(onRefresh: (() -> Unit)? = null) {
        houseRepository
            .getDashboard()
            .setup()
            .subscribe(
                {
                    _dashBoard.value = it
                    onRefresh?.invoke()
                }, {
                    it.printStackTrace()
                    error.value = it.message
                    onRefresh?.invoke()
                }
            )
    }
}
