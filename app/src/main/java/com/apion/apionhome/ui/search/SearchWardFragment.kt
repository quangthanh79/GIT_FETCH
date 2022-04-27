package com.apion.apionhome.ui.search

import com.apion.apionhome.R
import com.apion.apionhome.data.model.local.LocationName

class SearchWardFragment : SearchLocationFragment<LocationName>() {

    override fun onItemClick(item: LocationName) {
        setUpViewModel().setWard(item)
    }

    override fun onGetAll(query: String) {
        setUpViewModel().searchWard(query, includeAll)
    }

    override fun getHint(): String {
        return getString(R.string.title_search_ward)
    }
}
