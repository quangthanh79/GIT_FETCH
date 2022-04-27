package com.apion.apionhome.data.source

import com.apion.apionhome.data.model.BookMark
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

interface BookMarkDatasource {

    interface Remote {
        fun getAllBookMarks(): Maybe<List<BookMark>>

        fun getBookMarkById(id: Int): Maybe<BookMark>

        fun createBookMark(userId: Int, houseId: Int): Maybe<BookMark>

        fun updateBookMark(id: Int, userId: Int, houseId: Int): Maybe<BookMark>

        fun unBookMark(userId: Int, houseId: Int): Completable
    }
}
