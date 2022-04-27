package com.apion.apionhome.di

import com.apion.apionhome.data.source.remote.utils.BookMarkAPIService
import com.apion.apionhome.data.source.remote.utils.CommunityAPIService
import com.apion.apionhome.data.source.remote.utils.HouseAPIService
import com.apion.apionhome.data.source.remote.utils.UserAPIService
import org.koin.dsl.module

import retrofit2.Retrofit
// khởi tạo các service
//
val apiModule = module {
    single { get<Retrofit>().create(UserAPIService::class.java) }
    single { get<Retrofit>().create(HouseAPIService::class.java) }
    single { get<Retrofit>().create(BookMarkAPIService::class.java) }
    single { get<Retrofit>().create(CommunityAPIService::class.java) }
}
