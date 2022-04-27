package com.apion.apionhome

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import com.apion.apionhome.data.model.House
import com.apion.apionhome.data.model.User_I
import com.apion.apionhome.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                listOf(
                    apiModule,
                    networkModule,
                    viewModelModule,
                    userRepoModule,
                    houseRepoModule,
                    bookMarkRepoModule,
                    communityRepoModule,
                )
            )
        }
    }

    companion object {
        var sessionUser: MutableLiveData<User_I?> = MutableLiveData(null)
        var accesstoken: MutableLiveData<String?> = MutableLiveData(null)
        var tabToNavigate: MutableLiveData<Int?> = MutableLiveData(null)
        var profileUserNavigate: MutableLiveData<User_I?> = MutableLiveData(null)
        var houseNavigate: MutableLiveData<House?> = MutableLiveData(null)

    }
}
