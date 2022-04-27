package com.apion.apionhome.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.House
import com.apion.apionhome.data.repository.HouseRepository
import com.apion.apionhome.utils.setup

class HouseViewModel(val houseRepository: HouseRepository) : RxViewModel() {


    private val _house = MutableLiveData<House>()

    val house: LiveData<House>
        get() = _house

    private val _notifications = MutableLiveData<List<House>>()

    val notifications: LiveData<List<House>>
        get() = _notifications

    fun getHouseById(houseId: Int, onDone:(House)->Unit) {
        _isLoading.value = true
        houseRepository
            .getHouseById(houseId)
            .setup()
            .subscribe(
                {
                    _house.value = it
                    onDone(it)
                }, {
                    it.printStackTrace()
                    error.value = it.message
                    _isLoading.value = false
                }
            )
    }

    fun getNotification(onRefreshDone: (() -> Unit)?) {
        if (onRefreshDone == null) {
            _isLoading.value = true
        }
        houseRepository
            .getNotificationByUser()
            .setup()
            .doOnTerminate {
                onRefreshDone?.let {
                    it()
                }
                if (onRefreshDone == null) {
                    _isLoading.value = false
                }
            }
            .subscribe(
                {
                    println(it)
                    _notifications.value = it.sortedByDescending {
                        it.updatedAt
                    }
                }, {
                    it.printStackTrace()
                    error.value = it.message
                }
            )
    }

    fun updateHouse(images: List<String>, house: House) {
        houseRepository
            .updateHouse(images, house)
            .setup()
            .subscribe(
                {
                    _house.value = it
                }, {
                    it.printStackTrace()
                    error.value = it.message
                }
            )
    }
}
