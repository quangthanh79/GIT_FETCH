package com.apion.apionhome.ui.geting_started

import com.apion.apionhome.data.model.local.LocationName

class SelectStreetFragment : SelectLocationDetailFragment<LocationName>() {
    override fun onItemClick(item: LocationName) {
        viewModel.setStreet(item)
    }

    override fun onGetAll(query: String) {
        viewModel.searchStreet("")
    }
}