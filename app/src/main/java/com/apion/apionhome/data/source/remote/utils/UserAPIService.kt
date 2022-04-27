package com.apion.apionhome.data.source.remote.utils

import com.apion.apionhome.data.model.User_I
import com.apion.apionhome.data.model.UserFollowRequest
import com.apion.apionhome.data.source.remote.response_entity.*
import com.apion.apionhome.utils.ApiEndPoint
import com.apion.apionhome.utils.ApiEndPoint.PATH_CHECK_PHONE_EXIST
import com.apion.apionhome.utils.ApiEndPoint.PATH_FOLLOW
import com.apion.apionhome.utils.ApiEndPoint.PATH_LOGIN
import com.apion.apionhome.utils.ApiEndPoint.PATH_LOGOUT
import com.apion.apionhome.utils.ApiEndPoint.PATH_PARAM_ID
import com.apion.apionhome.utils.ApiEndPoint.PATH_UN_FOLLOW
import com.apion.apionhome.utils.ApiEndPoint.PATH_USERS
import com.apion.apionhome.utils.ApiEndPoint.PATH_USERS_BY_ID
import io.reactivex.rxjava3.core.Maybe
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface UserAPIService {

    @GET(PATH_USERS)
    fun geUsers(): Maybe<AllUserResponse>

    @GET(PATH_USERS_BY_ID)
    fun geUserById(@Path(PATH_PARAM_ID) id: Int): Maybe<UserResponse_I>

    @POST(PATH_USERS)
    fun createUser(@Body user: User_I): Maybe<Unit>

    @POST(PATH_USERS_BY_ID)
    fun updateUser(@Path(PATH_PARAM_ID) id: Int, @Body user: User_I): Maybe<UserResponse_I>

    @POST("update-pincode/{id}")
    fun updatePincode(@Path(PATH_PARAM_ID) id: String, @Body pass: RequestBody): Maybe<Unit>

    @POST(PATH_CHECK_PHONE_EXIST)
    fun checkExist(@Body phone_pincode: RequestBody): Maybe<UserResponse_I>

    @POST(PATH_LOGIN)
    fun login(@Body phone_password: RequestBody): Maybe<UserResponse_B>

    @POST(PATH_FOLLOW)
    fun follow(@Body follow: UserFollowRequest): Maybe<UserFollowResponse>

    @POST(PATH_UN_FOLLOW)
    fun unFollow(@Body follow: UserFollowRequest): Maybe<UserFollowResponse>

    @POST(PATH_LOGOUT)
    fun logout(
        @Body phone: RequestBody
    ): Maybe<UserResponse_I>

    @Multipart
    @POST(ApiEndPoint.PATH_UPLOAD_AVATAR)
    @JvmSuppressWildcards
    fun uploadAvatar(
        @Path(PATH_PARAM_ID) id: Int,
        @Part attachment: MultipartBody.Part
    ): Maybe<UserResponse_I>
}
