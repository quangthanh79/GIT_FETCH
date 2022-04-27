package com.apion.apionhome.data.repository

import com.apion.apionhome.data.model.dashboard.Dashboard
import com.apion.apionhome.data.model.House
import com.apion.apionhome.data.model.SearchParam
import com.apion.apionhome.data.model.SellHouseRequest
import com.apion.apionhome.data.model.local.District
import com.apion.apionhome.data.model.local.LocationName
import com.apion.apionhome.data.model.local.Province
import com.apion.apionhome.data.source.HouseDatasource
import io.reactivex.rxjava3.core.Maybe
import java.lang.Exception

class HouseRepositoryImpl(
    private val local: HouseDatasource.Local,
    private val remote: HouseDatasource.Remote
) : HouseRepository {

    override fun getAllHouses(): Maybe<List<House>> = remote.getAllHouses()

    override fun getHouseById(houseId: Int): Maybe<House> = remote.getHouseById(houseId)

    override fun getDashboard(): Maybe<Dashboard> = remote.getDashboard()

    override fun getHouseByUser(userId: Int): Maybe<List<House>> = remote.getHouseByUser(userId)

    override fun getSearchHouse(searchParam: SearchParam): Maybe<List<House>> =
        remote.getSearchHouse(searchParam)

    override fun createHouse(images: List<String>, house: House): Maybe<House> {
        return try {
            remote.createHouse(images, house)
        } catch (exception: Exception) {
            Maybe.error(exception)
        }
    }

    override fun updateHouse(images: List<String>, house: House): Maybe<House> {
        return try {
            remote.updateHouse(images, house)
        } catch (exception: Exception) {
            Maybe.error(exception)
        }
    }

    override fun getAllProvince(): Maybe<List<Province>> = local.getAllProvince()

    override fun searchProvince(query: String): Maybe<List<Province>> = local.searchProvince(query)

    override fun searchDistrict(province: Province?, query: String): Maybe<List<District>> =
        local.searchDistrict(province, query)

    override fun searchWard(district: District?, query: String): Maybe<List<LocationName>> =
        local.searchWard(district, query)

    override fun searchStreet(district: District?, query: String): Maybe<List<LocationName>> =
        local.searchStreet(district, query)

    override fun getNotificationByUser(): Maybe<List<House>> = remote.getNotificationByUser()
    override fun sellHouse(houseId: Int, house: SellHouseRequest): Maybe<House> =
        remote.sellHouse(houseId, house)

    override fun deleteHouse(houseId: Int): Maybe<House>  =
        remote.deleteHouse(houseId)
}
