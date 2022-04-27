package com.apion.apionhome.data.source.remote

import com.apion.apionhome.data.model.dashboard.Dashboard
import com.apion.apionhome.data.model.House
import com.apion.apionhome.data.model.SearchParam
import com.apion.apionhome.data.model.SellHouseRequest
import com.apion.apionhome.data.source.HouseDatasource
import com.apion.apionhome.data.source.remote.response_entity.AllHouseResponse
import com.apion.apionhome.data.source.remote.utils.HouseAPIService
import com.apion.apionhome.utils.ApiEndPoint.PART_ATTACHMENTS
import com.apion.apionhome.utils.toMap
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Maybe
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.*

class HouseRemoteDatasource(private val backend: HouseAPIService) : HouseDatasource.Remote {

    override fun getAllHouses(): Maybe<List<House>> = backend.geHouses().map { it.houses }

    override fun getHouseById(houseId: Int): Maybe<House> =
        backend.geHouseById(houseId).map { it.house }

    override fun getDashboard(): Maybe<Dashboard> = backend.geDashboard().map { it.dashboard }

    override fun getHouseByUser(userId: Int): Maybe<List<House>> =
        backend.getHouseByUser(userId).map { it.houses }

    override fun getSearchHouse(searchParam: SearchParam): Maybe<List<House>> {
        return try {
            backend.getSearchHouse(searchParam).map {
                if (it.isSuccess) it.houses else throw IllegalArgumentException(it.message)
            }
        } catch (exception: Exception) {
            Maybe.error(exception)
        }
    }

    @Throws(IllegalArgumentException::class)
    override fun createHouse(images: List<String>, house: House): Maybe<House> {
        val imagesParts = mutableListOf<MultipartBody.Part>()
        images.forEach {
            val file = File(it)
            println(it)
            println(file.isFile)
            if (file.isFile) {
                val end = file.name.split(".").last()
                val currentTime = Date().time.toString()
                val fileName = "$currentTime.$end"
                val imageRequestBody = RequestBody.create(MediaType.parse("image/*"), file)
                imagesParts.add(
                    MultipartBody.Part.createFormData(PART_ATTACHMENTS, fileName, imageRequestBody)
                )
            }
        }

        val json = Gson().toJson(house)
        val jsonObject = JSONObject(json)
        val body = jsonObject.toMap()

        return try {
            backend.createHouse(imagesParts, body).map {
                if (it.isSuccess) it.house else throw IllegalArgumentException(it.message)
            }
        } catch (exception: Exception) {
            Maybe.error(exception)
        }
    }

    @Throws(IllegalArgumentException::class)
    override fun updateHouse(images: List<String>, house: House): Maybe<House> {
        val imagesParts = mutableListOf<MultipartBody.Part>()

        images.forEach {
            val file = File(it)
            println(it)
            println(file.isFile)
            if (file.isFile) {
                val end = file.name.split(".").last()
                val currentTime = Date().time.toString()
                val fileName = "$currentTime.$end"
                val imageRequestBody = RequestBody.create(MediaType.parse("image/*"), file)
                imagesParts.add(
                    MultipartBody.Part.createFormData(PART_ATTACHMENTS, fileName, imageRequestBody)
                )
            }
        }

        val json = Gson().toJson(house)
        val jsonObject = JSONObject(json)
        val body = jsonObject.toMap()
        println(body)

        return try {
            backend.updateHouse(house.id, imagesParts, body).map {
                if (it.isSuccess) it.house else throw IllegalArgumentException(it.message)
            }
        } catch (exception: Exception) {
            Maybe.error(exception)
        }
    }

    override fun getNotificationByUser(): Maybe<List<House>> =
        backend.getNotificationByUser().map { it.houses }

    override fun sellHouse(houseId: Int, house: SellHouseRequest): Maybe<House> {
        return try {
            backend.sellHouse(houseId, house).map {
                if (it.isSuccess) it.house else throw IllegalArgumentException(it.message)
            }
        } catch (exception: Exception) {
            Maybe.error(exception)
        }
    }

    override fun deleteHouse(houseId: Int): Maybe<House> {
        return try {
            backend.deleteHouse(houseId).map {
                if (it.isSuccess) it.house else throw IllegalArgumentException(it.message)
            }
        } catch (exception: Exception) {
            Maybe.error(exception)
        }
    }
}
