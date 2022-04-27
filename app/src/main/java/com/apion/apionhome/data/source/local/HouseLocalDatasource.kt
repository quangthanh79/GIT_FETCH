package com.apion.apionhome.data.source.local

import android.content.Context
import com.apion.apionhome.data.model.local.*
import com.apion.apionhome.data.source.HouseDatasource
import com.apion.apionhome.utils.removeSpecific
import com.apion.apionhome.utils.smartContains
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Maybe
import java.io.IOException

class HouseLocalDatasource(private val context: Context) : HouseDatasource.Local {

    private val provinces: MutableList<Province> = mutableListOf()

    override fun getAllProvince(): Maybe<List<Province>> {
        return if (provinces.isEmpty()) {
            try {
                val local = context.assets
                    .open(FILE_NAME_LOCAL)
                    .bufferedReader()
                    .readText()

                if (local.isNotEmpty()) {
                    provinces.addAll(
                        Gson().fromJson(
                            local,
                            Array<Province>::class.java
                        ).toList()
                    )
                    provinces.forEach { province ->
                        province.districts.forEach { district ->
                            val provinceCopy = province.copy()
                            district.province = provinceCopy
                            val districtCopy = district.copy()
                            district.wards.forEach {
                                it.district = districtCopy
                            }
                            district.streets.forEach {
                                it.district = districtCopy
                            }
                            district.projects.forEach {
                                it.district = districtCopy
                            }
                        }
                    }
                    Maybe.just(provinces)
                } else {
                    Maybe.empty()
                }
            } catch (e: IOException) {
                Maybe.error(e)
            }
        } else {
            Maybe.just(provinces)
        }
    }

    override fun searchProvince(query: String): Maybe<List<Province>> {
        val queryString = query.removeSpecific()
        return getAllProvince().map { data ->
            data.filter {
                it.toString().smartContains(queryString)
            }
        }
    }

    override fun searchDistrict(province: Province?, query: String): Maybe<List<District>> {
        val queryString = query.removeSpecific()
        return getAllProvince().map { data ->
            val districts = mutableListOf<District>(AllDistrict())
            if (province != null) {
                data.firstOrNull() {
                    it.id == province.id
                }?.districts?.filter {
                    it.toString().smartContains(queryString)
                }?.let {
                    districts.addAll(it)
                }
                return@map districts
            }
            data.forEach { province ->
                districts.addAll(
                    province.districts.filter {
                        it.toString().smartContains(queryString)
                    }
                )
            }
            districts
        }
    }

    override fun searchWard(district: District?, query: String): Maybe<List<LocationName>> {
        val queryString = query.removeSpecific()
        return getAllProvince().map { data ->
            val wards = mutableListOf<LocationName>(AllLocationName())
            if (district != null) {
                data.forEach {
                    it.districts.firstOrNull {
                        it.id == district.id
                    }?.wards?.filter {
                        it.toString().smartContains(queryString)
                    }?.let {
                        wards.addAll(it)
                    }
                }
                return@map wards
            }
            data.forEach { province ->
                province.districts.forEach { district ->
                    wards.addAll(
                        district.wards.filter {
                            it.toString().smartContains(queryString)
                        }
                    )
                }
            }
            wards
        }
    }

    override fun searchStreet(district: District?, query: String): Maybe<List<LocationName>> {
        val queryString = query.removeSpecific()
        return getAllProvince().map { data ->
            val streets = mutableListOf<LocationName>(AllLocationName())

            if (district != null) {
                data.forEach {
                    it.districts.firstOrNull {
                        it.id == district.id
                    }?.streets?.filter {
                        it.toString().smartContains(queryString)
                    }?.let {
                        streets.addAll(it)
                    }
                }
                return@map streets
            }
            data.forEach { province ->
                province.districts.forEach { district ->
                    streets.addAll(
                        district.streets.filter {
                            it.toString().smartContains(queryString)
                        }
                    )
                }
            }
            streets
        }
    }

    override fun searchProject(district: District?, query: String): Maybe<List<LocationName>> {
        TODO("Not yet implemented")
    }

    override fun searchLocation(query: String): Maybe<List<Province>> {
        return getAllProvince().map { provinces ->
            val result = provinces.filter {
                it.toString().smartContains(query)
            }
            provinces
        }
    }

    companion object {
        private const val FILE_NAME_LOCAL = "local.json"
        private const val FILE_NAME_LOCATION = "location.json"
        private const val FILE_NAME_TIME = "time_update.txt"
        private val provinces: MutableList<Province> = mutableListOf()

        fun getAllProvince(context: Context): Maybe<List<Province>> {
            return if (provinces.isEmpty()) {
                try {
                    val local = context.assets
                        .open(FILE_NAME_LOCAL)
                        .bufferedReader()
                        .readText()

                    if (local.isNotEmpty()) {
                        provinces.addAll(
                            Gson().fromJson(
                                local,
                                Array<Province>::class.java
                            ).toList()
                        )
                        provinces.forEach { province ->
                            province.districts.forEach { district ->
                                val provinceCopy = province.copy()
                                district.province = provinceCopy
                                val districtCopy = district.copy()
                                district.wards.forEach {
                                    it.district = districtCopy
                                }
                                district.streets.forEach {
                                    it.district = districtCopy
                                }
                                district.projects.forEach {
                                    it.district = districtCopy
                                }
                            }
                        }
                        Maybe.just(provinces)
                    } else {
                        Maybe.empty()
                    }
                } catch (e: IOException) {
                    Maybe.error(e)
                }
            } else {
                Maybe.just(provinces)
            }
        }

        fun getProvinceByName(context: Context, name: String): Maybe<Province?> {
            return getAllProvince(context).map {
                it.first {
                    it.name.uppercase() == name.uppercase()
                }
            }
        }

        fun getDistrictByName(name: String, province: Province?): District? {
            return province?.districts?.first {
                println( it.name.uppercase())
                it.name.uppercase() == name.uppercase()
            }
        }

        fun getStreetByName(name: String, district: District?): LocationName? {
            return district?.streets?.first {
                it.name.uppercase() == name.uppercase()
            }
        }

        fun getWardByName(name: String, district: District?): LocationName? {
            return district?.wards?.first {
                it.name.uppercase() == name.uppercase()
            }
        }
    }
}
