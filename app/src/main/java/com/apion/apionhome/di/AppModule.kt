package com.apion.apionhome.di

import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.ui.add_home.AddHouseViewModel
import com.apion.apionhome.ui.detail.DetailViewModel
import com.apion.apionhome.ui.home.HomeViewModel
import com.apion.apionhome.ui.person.UserProfileViewModel
import com.apion.apionhome.ui.search.SearchViewModel
import com.apion.apionhome.ui.search.SearchViewModelHome
import com.apion.apionhome.ui.search.SearchViewModelTmp
import com.apion.apionhome.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { RxViewModel() }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { UserViewModel(get(), get()) }
    viewModel { HouseViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { SearchViewModelTmp(get()) }
    viewModel { SearchViewModelHome(get()) }
    viewModel { CommunityViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { UserProfileViewModel(get()) }
    viewModel { AddHouseViewModel(get(), get()) }

    viewModel { UpdatePincodeViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { ConfirmPhoneViewModel() }
    viewModel { ConfirmRegisterViewModel() }
    viewModel { DetailViewModel(get(), get(), get()) }
}
