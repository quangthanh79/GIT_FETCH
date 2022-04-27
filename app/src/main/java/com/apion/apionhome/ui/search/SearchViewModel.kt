package com.apion.apionhome.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.local.District
import com.apion.apionhome.data.model.local.ILocation
import com.apion.apionhome.data.model.House
import com.apion.apionhome.data.model.Range
import com.apion.apionhome.data.model.RangeUI
import com.apion.apionhome.data.model.SearchParam
import com.apion.apionhome.data.model.local.LocationName
import com.apion.apionhome.data.model.local.Province
import com.apion.apionhome.data.repository.HouseRepository
import com.apion.apionhome.utils.setup

open class SearchViewModel(private val houseRepository: HouseRepository) : RxViewModel() {

    val title = MutableLiveData<String>()

    private val _provinces = MutableLiveData<List<Province>>()

    val provinces: LiveData<List<Province>>
        get() = _provinces

    private val _district = MutableLiveData<District?>()

    val district: LiveData<District?>
        get() = _district

    private val _province = MutableLiveData<Province?>()

    val province: LiveData<Province?>
        get() = _province
    private val _ward = MutableLiveData<LocationName?>()

    val ward: LiveData<LocationName?>
        get() = _ward

    private val _street = MutableLiveData<LocationName?>()

    val street: LiveData<LocationName?>
        get() = _street

    val detailAddress = MutableLiveData<String?>()

    private val _locations = MutableLiveData<List<ILocation>>()
    val locations: LiveData<List<ILocation>>
        get() = _locations

    private val _texts = MutableLiveData<String>()
    val texts: LiveData<String>
        get() = _texts

    private val empty = emptyList<ILocation>()

    private val _priceIndex = MutableLiveData<Int>(0)

    val priceIndex: LiveData<Int>
        get() = _priceIndex

    private val _acreageIndex = MutableLiveData<Int>(0)

    val acreageIndex: LiveData<Int>
        get() = _acreageIndex

    private val _frontWidthIndex = MutableLiveData<Int>(0)

    val frontWidthIndex: LiveData<Int>
        get() = _frontWidthIndex

    private val _bedroomIndex = MutableLiveData<Int>(0)

    val bedroomIndex: LiveData<Int>
        get() = _bedroomIndex

    private val _houseTypeIndex = MutableLiveData<Int>(0)

    val houseTypeIndex: LiveData<Int>
        get() = _houseTypeIndex

    private val _houseDirectionIndex = MutableLiveData<Int>(0)

    val houseDirectionIndex: LiveData<Int>
        get() = _houseDirectionIndex

    private val _housesSearch = MutableLiveData<List<House>>()

    val housesSearch: LiveData<List<House>>
        get() = _housesSearch

    private val _isSearchDone = MutableLiveData<Boolean>(false)

    val isSearchDone: LiveData<Boolean>
        get() = _isSearchDone

    val _homeFeature = MutableLiveData<List<House>>()

    val homeFeature: LiveData<List<House>>
        get() = _homeFeature

    fun setSearchDone(done: Boolean) {
        _isSearchDone.value = done
    }

    fun getAddress(): String {
        var textAddress = ""
        detailAddress.value?.let {
            if (detailAddress.value?.isNotEmpty() == true) {
                textAddress += detailAddress.value ?: ""
                textAddress += ", "
            }
        }
        street.value?.let {
            if (street.value?.name?.isNotEmpty() == true) {
                textAddress += street.value?.prefix + " " + street.value?.name
                textAddress += ", "
            }
        }

        ward.value?.let {
            if (ward.value?.name?.isNotEmpty() == true) {
                textAddress += ward.value?.prefix + " " + ward.value?.name
                textAddress += ", "
            }
        }
        district.value?.let {
            if (district.value?.name?.isNotEmpty() == true) {
                textAddress += district.value?.name
                textAddress += ", "
            }
        }

        province.value?.let {
            textAddress += province.value?.name
        }
        println(" ${province.value}  ${district.value}")
        return textAddress
    }

    override fun initData() {
        super.initData()
//        getAllProvince()
        _province.value = Province(
            id = 2,
            name = "Hà Nội",
            code = "HN",
            districts = mutableListOf()
        )
        _district.value = District(
            id = 25,
            name = "Ba Đình",
            province = Province(
                id = 2,
                name = "Hà Nội",
                code = "HN",
                districts = mutableListOf()
            )
        )
        _ward.value = null
        _street.value = null
        title.value = ""
        _locations.value = emptyList()
        _priceIndex.value = 0
        _frontWidthIndex.value = 0
        _houseDirectionIndex.value = 0
        _housesSearch.value = emptyList()
        _houseTypeIndex.value = 0
        _bedroomIndex.value = 0
        _acreageIndex.value = 0
        _isSearchDone.value = false
        detailAddress.value = null
    }

    fun clearSearch() {
        _locations.value = empty
    }

    private fun getAllProvince() {
        houseRepository
            .getAllProvince()
            .setup()
            .subscribe(
                {

                }, {
                    it.printStackTrace()
                    error.value = it.message
                }
            )
    }


    fun searchDistrict(query: String, includeAll: Boolean) {
        houseRepository
            .searchDistrict(_province.value, query)
            .setup()
            .subscribe(
                {
                    if (!includeAll) {
                        val tpmList = it.subList(1, it.size)
                        _locations.value = tpmList
                    } else {
                        _locations.value = it
                    }
                }, {
                    it.printStackTrace()
                    error.value = it.message
                }
            )
    }

    fun searchProvince(query: String) {
        houseRepository
            .searchProvince(query)
            .setup()
            .subscribe(
                {
                    _locations.value = it
                },
                {
                    it.printStackTrace()
                    error.value = it.message
                })

    }

    fun searchWard(query: String, includeAll: Boolean) {
        houseRepository
            .searchWard(_district.value, query)
            .setup()
            .subscribe(
                {
                    if (!includeAll) {
                        val tpmList = it.subList(1, it.size)
                        _locations.value = tpmList
                    } else {
                        _locations.value = it
                    }
                }, {
                    it.printStackTrace()
                    error.value = it.message
                }
            )
    }

    fun searchStreet(query: String, includeAll: Boolean) {
        println("province $province")
        houseRepository
            .searchStreet(_district.value, query)
            .setup()
            .subscribe(
                {
                    if (!includeAll) {
                        val tpmList = it.subList(1, it.size)
                        _locations.value = tpmList
                    } else {
                        _locations.value = it
                    }
                }, {
                    it.printStackTrace()
                    error.value = it.message
                }
            )
    }

    fun searchHouse() {
        _isLoading.value = true
        _isSearchDone.value = false
        val param = SearchParam(
            title.value,
            province.value?.name,
            district.value?.name,
            ward.value?.name,
            street.value?.name,
            RangeUI.houseTypeRangeUis.entries.toList()[houseTypeIndex.value ?: 0].key,
            RangeUI.homeDirectionRangeUis.entries.toList()[houseDirectionIndex.value ?: 0].key,
            RangeUI.priceRangeUis.entries.toList()[priceIndex.value ?: 0].key,
            RangeUI.acreageRangeUis.entries.toList()[acreageIndex.value ?: 0].key,
            RangeUI.frontWidthRangeUis.entries.toList()[frontWidthIndex.value ?: 0].key,
            RangeUI.bedroomUis.entries.toList()[bedroomIndex.value ?: 0].key,
        )
        println(param)
        houseRepository
            .getSearchHouse(param)
            .setup()
            .doOnTerminate {
                _isLoading.value = false
            }
            .subscribe(
                {
                    _housesSearch.value = it.sortedByDescending {
                        it.updatedAt
                    }
                    _isSearchDone.value = true
                }, {
                    it.printStackTrace()
                    error.value = it.message
                }
            )
    }

    fun setProvince(province: Province?) {
        _province.value = province
        if (province?.districts?.isNotEmpty() == true) {
            _district.value = province.districts.first()
        }
        _ward.value = null
        _street.value = null
    }

    fun setDistrict(district: District?) {
        _district.value = district
        _ward.value = null
        _street.value = null
    }

    fun setWard(locationName: LocationName?) {
        _ward.value = locationName
    }

    fun setStreet(locationName: LocationName?) {
        _street.value = locationName
    }

    fun setFrontWidthIndex(index: Int) {
        _frontWidthIndex.value = index
    }

    fun setPriceIndex(index: Int) {
        _priceIndex.value = index
    }

    fun setAcreageIndex(index: Int) {
        _acreageIndex.value = index
    }

    fun setHouseTypeIndex(index: Int) {
        _houseTypeIndex.value = index
    }

    fun setHouseDirectionIndex(index: Int) {
        _houseDirectionIndex.value = index
    }

    fun setBedroomsIndex(index: Int) {
        _bedroomIndex.value = index
    }

}
