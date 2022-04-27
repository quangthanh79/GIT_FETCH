package com.apion.apionhome.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apion.apionhome.MyApplication
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.House
import com.apion.apionhome.data.model.SellHouseRequest
import com.apion.apionhome.data.model.User_I
import com.apion.apionhome.data.model.dashboard.Dashboard
import com.apion.apionhome.data.model.local.District
import com.apion.apionhome.data.repository.BookMarkRepository
import com.apion.apionhome.data.repository.HouseRepository
import com.apion.apionhome.data.repository.UserRepository
import com.apion.apionhome.utils.setup
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class DetailViewModel(
    private val userRepository: UserRepository,
    private val houseRepository: HouseRepository,
    private val bookmarkRepository: BookMarkRepository,
) : RxViewModel() {

    private val _houseDetail = MutableLiveData<House>()

    val houseDetail: LiveData<House>
        get() = _houseDetail

    fun setHouseDetail(house: House) {
        _houseDetail.value = house
        _isLoading.value = true
        houseRepository
            .getHouseById(house.id)
            .setup()
            .doOnTerminate {
                _isLoading.value = false
            }.subscribe(
                {
                    _houseDetail.value = it
                }, {
                    it.printStackTrace()
                    error.value = it.message
                }
            )
    }

    fun fetchUser(userId: Int, onDone: (() -> Unit)? = null) {
        userRepository
            .getUserById(userId)
            .setup()
            .doOnTerminate {
                _isLoading.value = false
            }
            .subscribe(
                {
                    MyApplication.sessionUser.value = it
                    onDone?.let { it1 -> it1() }
                }, {
                    it.printStackTrace()
                    error.value = it.message
                }
            )
    }

    fun createBookmark(userId: Int, houseId: Int) {
        _isLoading.value = true
        bookmarkRepository
            .createBookMark(userId, houseId)
            .setup()
            .subscribe(
                {
                    fetchUser(userId)
                }, {
                    it.printStackTrace()
                    error.value = it.message
                    _isLoading.value = false
                }
            )
    }

    fun removeBookmark(userId: Int, houseId: Int) {
        _isLoading.value = true
        bookmarkRepository
            .unBookMark(userId, houseId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    fetchUser(userId)
                }, {
                    it.printStackTrace()
                    error.value = it.message
                    _isLoading.value = false
                }
            )
    }

    fun updateHouse(images: List<String>, house: House) {
        _houseDetail.value = house
        _isLoading.value = true
        houseRepository
            .updateHouse(images, house)
            .setup()
            .doOnTerminate {
                _isLoading.value = false
            }.subscribe(
                {
                    _houseDetail.value = it
                }, {
                    it.printStackTrace()
                    error.value = it.message
                }
            )
    }

    fun soldHouse(onDone: () -> Unit) {
        houseDetail.value?.id?.let {
            _isLoading.value = true
            houseRepository
                .sellHouse(it, SellHouseRequest(MyApplication.sessionUser.value?.phone ?: "-1"))
                .setup()
                .doOnTerminate {
                    _isLoading.value = false
                }.subscribe(
                    {
                        _houseDetail.value = it
                        fetchUser(MyApplication.sessionUser.value?.id ?: -1, onDone)
                    }, {
                        it.printStackTrace()
                        error.value = it.message
                    }
                )
        }
    }

    fun removeHouse(onDone: () -> Unit) {
        houseDetail.value?.id?.let {
            _isLoading.value = true
            houseRepository
                .deleteHouse(it)
                .setup()
                .doOnTerminate {
                    _isLoading.value = false
                }.subscribe(
                    {
                        _houseDetail.value = it
                        fetchUser(MyApplication.sessionUser.value?.id ?: -1, onDone)

                    }, {
                        it.printStackTrace()
                        error.value = it.message
                    }
                )
        }
    }
}
