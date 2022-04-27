package com.apion.apionhome.utils


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

//fun <X, Y> LiveData<X>.transform(transform: (X) -> Y) {
//
//    MediatorLiveData<Y>().let {
//    it.addSource(this) { value -> it.value = transform(value) }
//    }
//}
//

//fun <X, Y> LiveData<X>.transform(transform: (X) -> Y) = MediatorLiveData<Y>()
//    .also {
//        it.addSource(this,{value -> it.value = transform(value) })
//}

fun <X, Y> LiveData<X>.transform(convert: (X) -> Y): MediatorLiveData<Y> {
    var ab = MediatorLiveData<Y>()
    ab.addSource(this){
        ab.value = convert(it)
    }
    return ab
}
