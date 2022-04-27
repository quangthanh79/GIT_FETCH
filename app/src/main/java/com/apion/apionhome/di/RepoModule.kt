package com.apion.apionhome.di

import com.apion.apionhome.data.repository.*
import com.apion.apionhome.data.source.BookMarkDatasource
import com.apion.apionhome.data.source.CommunityDatasource
import com.apion.apionhome.data.source.HouseDatasource
import com.apion.apionhome.data.source.UserDatasource
import com.apion.apionhome.data.source.local.HouseLocalDatasource
import com.apion.apionhome.data.source.local.UserLocalDatasource
import com.apion.apionhome.data.source.remote.BookMarkRemoteDatasource
import com.apion.apionhome.data.source.remote.CommunityRemoteDatasource
import com.apion.apionhome.data.source.remote.HouseRemoteDatasource
import com.apion.apionhome.data.source.remote.UserRemoteDatasource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

// khởi tạo repository và datasource
val userRepoModule = module {

    single<UserDatasource.Remote> { UserRemoteDatasource(get()) }
    single<UserDatasource.Local> { UserLocalDatasource(androidContext()) }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }
}

val houseRepoModule = module {
    single<HouseDatasource.Local> { HouseLocalDatasource(androidContext()) }

    single<HouseDatasource.Remote> { HouseRemoteDatasource(get()) }

    single<HouseRepository> { HouseRepositoryImpl(get(), get()) }
}

val bookMarkRepoModule = module {

    single<BookMarkDatasource.Remote> { BookMarkRemoteDatasource(get()) }

    single<BookMarkRepository> { BookMarkRepositoryImpl(get()) }
}

val communityRepoModule = module {

    single<CommunityDatasource.Remote> { CommunityRemoteDatasource(get()) }

    single<CommunityRepository> { CommunityRepositoryImpl(get()) }
}