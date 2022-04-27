package com.apion.apionhome.ui.geting_started

import com.apion.apionhome.data.model.local.District

class SelectDistrictFragment : SelectLocationDetailFragment<District>(){
    override fun onItemClick(item: District) {
        viewModel.setDistrict(item)
    }

    override fun onGetAll(query: String) {
        viewModel.searchDistrict(query)
    }

}