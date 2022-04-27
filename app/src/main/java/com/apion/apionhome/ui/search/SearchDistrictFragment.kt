package com.apion.apionhome.ui.search

import com.apion.apionhome.data.model.local.District

class SearchDistrictFragment : SearchLocationFragment<District>() {

    override fun onItemClick(item: District) {
        setUpViewModel().setDistrict(item)
    }

    override fun onGetAll(query: String) {
        setUpViewModel().searchDistrict(query, includeAll)
    }


    override fun getHint(): String {
        return "Tìm kiếm"
    }

}