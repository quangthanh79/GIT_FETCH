package com.apion.apionhome.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class RxViewModel : ViewModel() {

    val disposables: CompositeDisposable = CompositeDisposable()

    protected val error = MutableLiveData<String>()
    val errorException: LiveData<String>
        get() = error

    val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    open fun initData(){
        println("init data")
    }
}
