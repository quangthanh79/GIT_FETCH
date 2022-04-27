package com.apion.apionhome.data.source.remote

import com.apion.apionhome.data.model.BookMark
import com.apion.apionhome.data.model.community.Community
import com.apion.apionhome.data.model.community.Participant
import com.apion.apionhome.data.source.BookMarkDatasource
import com.apion.apionhome.data.source.CommunityDatasource
import com.apion.apionhome.data.source.remote.response_entity.AllBookMarkResponse
import com.apion.apionhome.data.source.remote.response_entity.BookMarkResponse
import com.apion.apionhome.data.source.remote.utils.BookMarkAPIService
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

class BookMarkRemoteDatasource(private val backend: BookMarkAPIService) :
    BookMarkDatasource.Remote {
    override fun getAllBookMarks(): Maybe<List<BookMark>> =
        backend.getAllBookMarks().map { it.bookMarks }

    override fun getBookMarkById(id: Int): Maybe<BookMark> =
        backend.getBookMarkById(id).map { it.bookMark }

    override fun createBookMark(userId: Int, houseId: Int): Maybe<BookMark> {
        val json = JsonObject().apply {
            addProperty("user_id", userId)
            addProperty("house_id", houseId)
        }

        val body = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(json)
        )

        return backend.createBookMark(body).map {
            it.bookMark
        }
    }

    override fun updateBookMark(id: Int, userId: Int, houseId: Int): Maybe<BookMark> {
        val json = JsonObject().apply {
            addProperty("user_id", userId)
            addProperty("house_id", houseId)
        }

        val body = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(json)
        )

        return backend.updateBookMark(id, body).map {
            it.bookMark
        }
    }


    override fun unBookMark(userId: Int, houseId: Int): Completable {
        val json = JsonObject().apply {
            addProperty("user_id", userId)
            addProperty("house_id", houseId)
        }

        val body = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(json)
        )

        return backend.unBookMark(body)
    }
}
