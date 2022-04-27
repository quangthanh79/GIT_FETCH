package com.apion.apionhome.data.source.remote.utils

import com.apion.apionhome.data.source.remote.response_entity.*
import com.apion.apionhome.utils.ApiEndPoint.PATH_PARAM_ID
import com.apion.apionhome.utils.ApiEndPoint.PATH_BOOKMARK
import com.apion.apionhome.utils.ApiEndPoint.PATH_BOOKMARK_BY_ID
import com.apion.apionhome.utils.ApiEndPoint.PATH_UN_BOOKMARK
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import okhttp3.RequestBody
import retrofit2.http.*

interface BookMarkAPIService {

    @GET(PATH_BOOKMARK)
    fun getAllBookMarks(): Maybe<AllBookMarkResponse>

    @GET(PATH_BOOKMARK_BY_ID)
    fun getBookMarkById(@Path(PATH_PARAM_ID) id: Int): Maybe<BookMarkResponse>

    @POST(PATH_BOOKMARK)
    fun createBookMark(@Body body: RequestBody): Maybe<BookMarkResponse>

    @POST(PATH_BOOKMARK_BY_ID)
    fun updateBookMark(
        @Path(PATH_PARAM_ID) id: Int,
        @Body body: RequestBody
    ): Maybe<BookMarkResponse>

    @POST(PATH_UN_BOOKMARK)
    fun unBookMark(@Body body: RequestBody): Completable
}
