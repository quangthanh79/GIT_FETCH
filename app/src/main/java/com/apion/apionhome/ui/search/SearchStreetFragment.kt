package com.apion.apionhome.ui.search

import com.apion.apionhome.R
import com.apion.apionhome.data.model.local.LocationName

class SearchStreetFragment : SearchLocationFragment<LocationName>() {

    override fun getHint(): String {
        return getString(R.string.title_search_street)
    }

    override fun onItemClick(item: LocationName) {
        setUpViewModel().setStreet(item)
    }

    override fun onGetAll(query: String) {
        setUpViewModel().searchStreet(query, includeAll)
    }
}
