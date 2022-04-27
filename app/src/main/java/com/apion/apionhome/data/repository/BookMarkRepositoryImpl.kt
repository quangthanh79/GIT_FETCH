package com.apion.apionhome.data.repository

import com.apion.apionhome.data.model.BookMark
import com.apion.apionhome.data.source.BookMarkDatasource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

class BookMarkRepositoryImpl(private val remote: BookMarkDatasource.Remote) : BookMarkRepository {

    override fun getAllBookMarks(): Maybe<List<BookMark>> = remote.getAllBookMarks()

    override fun getBookMarkById(id: Int): Maybe<BookMark> = remote.getBookMarkById(id)

    override fun createBookMark(userId: Int, houseId: Int): Maybe<BookMark> =
        remote.createBookMark(userId, houseId)

    override fun updateBookMark(id: Int, userId: Int, houseId: Int): Maybe<BookMark> =
        remote.updateBookMark(id, userId, houseId)

    override fun unBookMark(userId: Int, houseId: Int): Completable =
        remote.unBookMark(userId, houseId)
}
