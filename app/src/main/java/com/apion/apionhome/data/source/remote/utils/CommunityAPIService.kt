package com.apion.apionhome.data.source.remote.utils

import com.apion.apionhome.data.model.community.Community
import com.apion.apionhome.data.model.community.Participant
import com.apion.apionhome.data.source.remote.response_entity.*
import com.apion.apionhome.utils.ApiEndPoint
import com.apion.apionhome.utils.ApiEndPoint.PATH_COMMUNITY
import com.apion.apionhome.utils.ApiEndPoint.PATH_COMMUNITY_BY_ID
import com.apion.apionhome.utils.ApiEndPoint.PATH_LEAVE_COMMUNITY
import com.apion.apionhome.utils.ApiEndPoint.PATH_PARAM_ID
import com.apion.apionhome.utils.ApiEndPoint.PATH_PARTICIPANT
import com.apion.apionhome.utils.ApiEndPoint.PATH_PARTICIPANT_BY_ID
import com.apion.apionhome.utils.ApiEndPoint.PATH_USERS_BY_ID
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface CommunityAPIService {

    @GET(PATH_COMMUNITY)
    fun getAllCommunities(): Maybe<AllCommunityResponse>

    @GET(PATH_COMMUNITY_BY_ID)
    fun getCommunityById(@Path(PATH_PARAM_ID) id: Int): Maybe<CommunityResponse>

    @POST(PATH_COMMUNITY)
    @Multipart
    @JvmSuppressWildcards
    fun createCommunity(
        @Part cover: MultipartBody.Part?,
        @Part avatar: MultipartBody.Part?,
        @PartMap community: Map<String, RequestBody>
    ): Maybe<CommunityResponse>

    @POST(PATH_USERS_BY_ID)
    @Multipart
    @JvmSuppressWildcards
    fun updateCommunity(
        @Part cover: MultipartBody.Part?,
        @Part avatar: MultipartBody.Part?,
        @PartMap community: Map<String, RequestBody>
    ): Maybe<CommunityResponse>

    @GET(PATH_PARTICIPANT)
    fun getAllParticipants(): Maybe<AllParticipantResponse>

    @GET(PATH_PARTICIPANT_BY_ID)
    fun getParticipantById(@Path(PATH_PARAM_ID) id: Int): Maybe<ParticipantResponse>

    @POST(PATH_PARTICIPANT)
    fun createParticipant(@Body body: RequestBody): Maybe<ParticipantResponse>

    @POST(PATH_PARTICIPANT_BY_ID)
    fun updateParticipant(
        @Path(PATH_PARAM_ID) id: Int,
        @Body body: RequestBody
    ): Maybe<ParticipantResponse>

    @POST(PATH_LEAVE_COMMUNITY)
    fun leaveCommunity(@Path(PATH_PARAM_ID) id: Int, @Body body: RequestBody): Completable
}
