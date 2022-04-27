package com.apion.apionhome.data.source.remote

import com.apion.apionhome.data.model.community.Community
import com.apion.apionhome.data.model.community.Participant
import com.apion.apionhome.data.source.CommunityDatasource
import com.apion.apionhome.data.source.remote.utils.CommunityAPIService
import com.apion.apionhome.data.source.remote.utils.HttpUtil
import com.apion.apionhome.utils.ApiEndPoint
import com.apion.apionhome.utils.toMap
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception
import java.lang.IllegalArgumentException

class CommunityRemoteDatasource(private val backend: CommunityAPIService) :
    CommunityDatasource.Remote {

    override fun getAllCommunities(): Maybe<List<Community>> = backend.getAllCommunities().map {
        it.communities
    }

    override fun getCommunityById(id: Int): Maybe<Community> =
        backend.getCommunityById(id).map { it.community }

    override fun createCommunity(
        coverPath: String?,
        avatarPath: String?,
        community: Community
    ): Maybe<Community> {

        val coverPart: MultipartBody.Part? = HttpUtil.createPart(ApiEndPoint.PART_COVER, coverPath)

        val avatarPart: MultipartBody.Part? =
            HttpUtil.createPart(ApiEndPoint.PART_AVATAR, avatarPath)

        println("community $community")
        val body = community.toMap()

        return try {
            backend.createCommunity(coverPart, avatarPart, body).map {
                if (it.isSuccess) it.community else throw IllegalArgumentException(it.message)
            }
        } catch (exception: Exception) {
            Maybe.error(exception)
        }
    }

    override fun updateCommunity(
        coverPath: String?,
        avatarPath: String?,
        community: Community
    ): Maybe<Community> {
        val coverPart: MultipartBody.Part? = HttpUtil.createPart(ApiEndPoint.PART_COVER, coverPath)

        val avatarPart: MultipartBody.Part? =
            HttpUtil.createPart(ApiEndPoint.PART_AVATAR, avatarPath)

        val body = community.toMap()

        return try {
            backend.updateCommunity(coverPart, avatarPart, body).map {
                if (it.isSuccess) it.community else throw IllegalArgumentException(it.message)
            }
        } catch (exception: Exception) {
            Maybe.error(exception)
        }
    }

    override fun getAllParticipants(): Maybe<List<Participant>> =
        backend.getAllParticipants().map { it.participants }

    override fun getParticipantById(id: Int): Maybe<Participant> =
        backend.getParticipantById(id).map { it.participant }

    override fun createParticipant(userId: Int, communityId: Int): Maybe<Participant> {
        val json = JsonObject().apply {
            addProperty("user_id", userId)
            addProperty("community_id", communityId)
        }

        val body = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(json)
        )
        return backend.createParticipant(body).map {
            it.participant
        }
    }

    override fun updateParticipant(id: Int, userId: Int, communityId: Int): Maybe<Participant> {
        val json = JsonObject().apply {
            addProperty("user_id", userId)
            addProperty("community_id", communityId)
        }

        val body = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(json)
        )

        return backend.updateParticipant(id, body).map {
            it.participant
        }
    }

    override fun leaveCommunity(id: Int, userId: Int, communityId: Int): Completable {
        val json = JsonObject().apply {
            addProperty("user_id", userId)
            addProperty("community_id", communityId)
        }
        val body = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(json)
        )
        return backend.leaveCommunity(id, body)
    }
}
