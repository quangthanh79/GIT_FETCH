package com.apion.apionhome.ui.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apion.apionhome.MyApplication
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.User_I
import com.apion.apionhome.data.model.UserFollowed
import com.apion.apionhome.data.model.dashboard.Dashboard
import com.apion.apionhome.data.repository.UserRepository
import com.apion.apionhome.utils.setup

class UserProfileViewModel(
    val userRepository: UserRepository,
) : RxViewModel() {

    private val _userFollowed = MutableLiveData<UserFollowed>()

    val userFollowed: LiveData<UserFollowed>
        get() = _userFollowed


    fun fetchUser(userId: Int) {
        _isLoading.value = true
        userRepository
            .getUserById(userId)
            .setup()
            .doOnTerminate {
                _isLoading.value = false
            }
            .subscribe(
                {
                    MyApplication.sessionUser.value = it
                }, {
                    it.printStackTrace()
                    error.value = it.message
                }
            )
    }

    fun fetchUser2(userId: Int, onDone: (User_I) -> Unit) {
        _isLoading.value = true
        userRepository
            .getUserById(userId)
            .setup()
            .doOnTerminate {
                _isLoading.value = false
            }
            .subscribe(
                {
                    onDone(it)
                }, {
                    it.printStackTrace()
                    error.value = it.message
                }
            )
    }

    fun follow(followerId: Int, beingFollowedId: Int) {
        _isLoading.value = true
        userRepository
            .follow(followerId, beingFollowedId)
            .setup()
            .doOnTerminate {
                _isLoading.value = false
            }
            .subscribe(
                {
                    _userFollowed.value = it
                    fetchUser(followerId)
                }, {
                    it.printStackTrace()
                    error.value = it.message
                    _isLoading.value = false
                }
            )
    }

    fun unFollow(followerId: Int, beingFollowedId: Int) {
        _isLoading.value = true
        userRepository
            .unFollow(followerId, beingFollowedId)
            .setup()
            .subscribe(
                {
                    _userFollowed.value = it
                    fetchUser(followerId)
                }, {
                    it.printStackTrace()
                    error.value = it.message
                    _isLoading.value = false
                }
            )
    }
}
