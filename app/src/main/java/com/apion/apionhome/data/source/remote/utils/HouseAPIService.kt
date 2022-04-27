package com.apion.apionhome.data.source.remote.utils

import com.apion.apionhome.data.model.House
import com.apion.apionhome.data.model.SearchParam
import com.apion.apionhome.data.model.SellHouseRequest
import com.apion.apionhome.data.source.remote.response_entity.AllHouseResponse
import com.apion.apionhome.data.source.remote.response_entity.DashboardResponse
import com.apion.apionhome.data.source.remote.response_entity.HouseResponse
import com.apion.apionhome.utils.ApiEndPoint.PATH_DASHBOARD
import com.apion.apionhome.utils.ApiEndPoint.PATH_DELETE_HOUSE
import com.apion.apionhome.utils.ApiEndPoint.PATH_GET_HOUSE_BY_USER
import com.apion.apionhome.utils.ApiEndPoint.PATH_GET_NOTIFICATION_BY_USER
import com.apion.apionhome.utils.ApiEndPoint.PATH_HOUSES
import com.apion.apionhome.utils.ApiEndPoint.PATH_HOUSES_BY_ID
import com.apion.apionhome.utils.ApiEndPoint.PATH_PARAM_ID
import com.apion.apionhome.utils.ApiEndPoint.PATH_SEARCH
import com.apion.apionhome.utils.ApiEndPoint.PATH_SELL_HOUSE
import io.reactivex.rxjava3.core.Maybe
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface HouseAPIService {

    @GET(PATH_HOUSES)
    fun geHouses(): Maybe<AllHouseResponse>

    @GET(PATH_DASHBOARD)
    fun geDashboard(): Maybe<DashboardResponse>

    @GET(PATH_HOUSES_BY_ID)
    fun geHouseById(@Path(PATH_PARAM_ID) houseId: Int): Maybe<HouseResponse>

    @GET(PATH_GET_HOUSE_BY_USER)
    fun getHouseByUser(@Path(PATH_PARAM_ID) userId: Int): Maybe<AllHouseResponse>

    @GET(PATH_GET_NOTIFICATION_BY_USER)
    fun getNotificationByUser(): Maybe<AllHouseResponse>

    @POST(PATH_SEARCH)
    fun getSearchHouse(@Body search: SearchParam): Maybe<AllHouseResponse>

    @Multipart
    @POST(PATH_HOUSES)
    @JvmSuppressWildcards
    fun createHouse(
        @Part attachments: List<MultipartBody.Part>,
        @PartMap house: Map<String, RequestBody>
    ): Maybe<HouseResponse>

    @Multipart
    @POST(PATH_HOUSES_BY_ID)
    @JvmSuppressWildcards
    fun updateHouse(
        @Path(PATH_PARAM_ID) houseId: Int,
        @Part attachments: List<MultipartBody.Part>,
        @PartMap house: Map<String, RequestBody>
    ): Maybe<HouseResponse>

    @POST(PATH_SELL_HOUSE)
    fun sellHouse(
        @Path(PATH_PARAM_ID) houseId: Int,
        @Body house: SellHouseRequest
    ): Maybe<HouseResponse>

    @DELETE(PATH_DELETE_HOUSE)
    fun deleteHouse(
        @Path(PATH_PARAM_ID) houseId: Int,
    ): Maybe<HouseResponse>
}
