package com.apion.apionhome.ui.add_home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apion.apionhome.MyApplication
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.House
import com.apion.apionhome.data.model.RangeUI
import com.apion.apionhome.data.repository.HouseRepository
import com.apion.apionhome.data.repository.UserRepository
import com.apion.apionhome.utils.isTitleValid
import com.apion.apionhome.utils.setup
import com.apion.apionhome.utils.transform

class AddHouseViewModel(
    val houseRepository: HouseRepository,
    val userRepository: UserRepository
) : RxViewModel() {
    val title = MutableLiveData<String>()
    val titleRule = title.transform {
        if (it.isTitleValid) null else 1
    }
    val content = MutableLiveData<String>()

    val contentRule = content.transform {
        if (it.isNotEmpty()) null else "Vui lòng nhập nội dung!"
    }

    val imagesRule = MutableLiveData<Boolean>(false)
    val priceRangeRule = MutableLiveData<String?>()

    val addressRule = MutableLiveData<String?>()
    val price = MutableLiveData<String>()
    val acreage = MutableLiveData<String>()
    val frontWidth = MutableLiveData<String>()
    val bedroom = MutableLiveData<String>()
    val commissionRate = MutableLiveData<String>("3")

    val addHouseDone = MutableLiveData<Boolean>(false)

    private val _houseTypeIndex = MutableLiveData<Int>(1)
    val houseTypeIndex: LiveData<Int>
        get() = _houseTypeIndex

    private val _houseDirectionIndex = MutableLiveData<Int>(0)
    val houseDirectionIndex: LiveData<Int>
        get() = _houseDirectionIndex

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

    fun createHouse(
        province: String?,
        district: String?,
        ward: String?,
        street: String?,
        detailAddress: String?,
        images: List<String?>,
        onDone: () -> Unit
    ) {
        validate()
        if (province == null) {
            addressRule.value = "Vui lòng chọn địa chỉ nhà!"
        } else {
            addressRule.value = null
        }
        val imagesList = mutableListOf<String>()
        images.forEach {
            it?.let {
                imagesList.add(it)
            }
        }
        imagesRule.value = imagesList.isEmpty()
        if (titleRule.value == null &&
            contentRule.value == null &&
            addressRule.value == null &&
            imagesRule.value == false
        ) {
            _isLoading.value = true
            val house = House(
                houseType = RangeUI.houseTypeRangeUis.keys.toMutableList()[houseTypeIndex.value
                    ?: 1],
                homeDirection = RangeUI.homeDirectionRangeUis.keys.toMutableList()[houseDirectionIndex.value
                    ?: 1],
                title = title.value,
                content = content.value,
                province = province,
                district = district,
                ward = ward,
                street = street,
                address = detailAddress,
                price = price.value?.toLong() ?: 0,
                frontWidth = frontWidth.value?.toDouble() ?: 0.0,
                acreage = acreage.value?.toDouble() ?: 0.0,
                bedrooms = bedroom.value?.toInt() ?: 0,
                userId = MyApplication.sessionUser.value?.id,
                commissionRate = commissionRate.value ?: "0.0"
            )
            houseRepository.createHouse(
                imagesList,
                house
            ).setup()
                .doOnTerminate {
                    _isLoading.value = false
                }
                .subscribe(
                    {
                        println(it)
                        onDone()
                        fetchUser(MyApplication.sessionUser.value?.id ?: -1)
                    }, {
                        it.printStackTrace()
                        error.value = it.message
                    }
                )
        }
    }

    fun updateHouse(
        provinceP: String?,
        districtP: String?,
        wardP: String?,
        streetP: String?,
        detailAddress: String?,
        images: List<String?>,
        houseDetail: House,
        onDone: () -> Unit
    ) {
        validate()
        if (provinceP == null) {
            addressRule.value = "Vui lòng chọn địa chỉ nhà!"
        } else {
            addressRule.value = null
        }
        val imagesList = mutableListOf<String>()
        val defaultImages = mutableListOf<String>()
        images.forEach {
            if (it?.contains("http") == true) {
                defaultImages.add(it)
            } else {
                it?.let {
                    imagesList.add(it)
                }
            }
        }
        imagesRule.value = imagesList.isEmpty() && defaultImages.isEmpty()
        if (titleRule.value == null &&
            contentRule.value == null &&
            addressRule.value == null &&
            imagesRule.value == false
        ) {
            _isLoading.value = true
            val house = houseDetail.apply {
                houseType = RangeUI.houseTypeRangeUis.keys.toMutableList()[houseTypeIndex.value
                    ?: 1]
                homeDirection =
                    RangeUI.homeDirectionRangeUis.keys.toMutableList()[houseDirectionIndex.value
                        ?: 1]
                title = this@AddHouseViewModel.title.value
                content = this@AddHouseViewModel.content.value
                province = provinceP
                district = districtP
                ward = wardP
                street = streetP
                address = detailAddress
                price = this@AddHouseViewModel.price.value?.toLong() ?: 0
                frontWidth = this@AddHouseViewModel.frontWidth.value?.toDouble() ?: 0.0
                acreage = this@AddHouseViewModel.acreage.value?.toDouble() ?: 0.0
                bedrooms = bedroom.value?.toInt() ?: 0
                commissionRate = this@AddHouseViewModel.commissionRate.value ?: "0.0"
                this.images = defaultImages
            }
            houseRepository.updateHouse(
                imagesList,
                house
            ).setup()
                .doOnTerminate {
                    _isLoading.value = false
                }
                .subscribe(
                    {
                        println(it)
                        onDone()
                        fetchUser(MyApplication.sessionUser.value?.id ?: -1)
                    }, {
                        it.printStackTrace()
                        error.value = it.message
                    }
                )
        }
    }

    private fun validate(): String? {
        val titleValue = title.value
        if (titleValue.isNullOrBlank()) {
            titleRule.value = 2
        }
        if (price.value.isNullOrEmpty() || (price.value?.toDouble() ?: 0.0 <= 0.0)) {
            priceRangeRule.value = "Vui lòng nhập giá!"
        } else if (acreage.value.isNullOrEmpty() || (acreage.value?.toDouble() ?: 0.0 <= 0.0)) {
            priceRangeRule.value = "Vui lòng nhập diện tích!"
        } else if (frontWidth.value.isNullOrEmpty() || (frontWidth.value?.toDouble() ?: 0.0 <= 0.0)) {
            priceRangeRule.value = "Vui lòng nhập mặt tiền!"
        } else {
            priceRangeRule.value = null
        }
        if (content.value.isNullOrBlank()) {
            contentRule.value = "Vui lòng nhập nội dung!"
        } else {
            contentRule.value = null
        }
        if (titleRule.value == null && contentRule.value == null) {
            return ""
        }
        return null
    }

    fun setHouseTypeIndex(index: Int) {
        _houseTypeIndex.value = index
    }

    fun setHouseDirectionIndex(index: Int) {
        _houseDirectionIndex.value = index
    }
}
