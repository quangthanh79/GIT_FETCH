package com.apion.apionhome.data.repository

import com.apion.apionhome.data.model.dashboard.Dashboard
import com.apion.apionhome.data.model.House
import com.apion.apionhome.data.model.SearchParam
import com.apion.apionhome.data.model.SellHouseRequest
import com.apion.apionhome.data.model.local.District
import com.apion.apionhome.data.model.local.LocationName
import com.apion.apionhome.data.model.local.Province
import io.reactivex.rxjava3.core.Maybe

interface HouseRepository {

    fun getAllHouses(): Maybe<List<House>>

    fun getHouseById(houseId: Int): Maybe<House>

    fun getDashboard(): Maybe<Dashboard>

    fun getHouseByUser(userId: Int): Maybe<List<House>>

    fun getSearchHouse(searchParam: SearchParam): Maybe<List<House>>

    fun createHouse(images: List<String>, house: House): Maybe<House>

    fun updateHouse(images: List<String>, house: House): Maybe<House>

    fun getAllProvince(): Maybe<List<Province>>

    fun searchProvince(query: String): Maybe<List<Province>>

    fun searchDistrict(province: Province? = null, query: String): Maybe<List<District>>

    fun searchWard(
        district: District? = null,
        query: String
    ): Maybe<List<LocationName>>

    fun searchStreet(
        district: District? = null,
        query: String
    ): Maybe<List<LocationName>>

    fun getNotificationByUser(): Maybe<List<House>>

    fun sellHouse(
        houseId: Int,
        house: SellHouseRequest
    ): Maybe<House>

    fun deleteHouse(
        houseId: Int,
    ): Maybe<House>
}
