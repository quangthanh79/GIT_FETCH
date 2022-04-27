package com.apion.apionhome.ui.search

import com.apion.apionhome.R
import com.apion.apionhome.data.model.local.Province

class SearchProvinceFragment : SearchLocationFragment<Province>() {

    override fun getHint(): String {
        return getString(R.string.title_search_province)
    }

    override fun onItemClick(item: Province) {
        setUpViewModel().setProvince(item)
    }

    override fun onGetAll(query: String) {
        setUpViewModel().searchProvince(query)
    }
}