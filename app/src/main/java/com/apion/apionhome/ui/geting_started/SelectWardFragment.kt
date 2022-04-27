package com.apion.apionhome.ui.geting_started

import com.apion.apionhome.data.model.local.LocationName
import com.apion.apionhome.data.model.local.Province

class SelectWardFragment : SelectLocationDetailFragment<LocationName>(){
    override fun onItemClick(item: LocationName) {
        viewModel.setWard(item)
    }

    override fun onGetAll(query: String) {
        viewModel.searchWard("")
    }

}