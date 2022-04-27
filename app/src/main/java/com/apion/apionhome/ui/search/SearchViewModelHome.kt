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

class SearchViewModelHome(private val houseRepository: HouseRepository) : SearchViewModel(houseRepository) {
    init {
        initData()
    }
}
