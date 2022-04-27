package com.apion.apionhome.data.source

import com.apion.apionhome.data.model.User_I
import com.apion.apionhome.data.model.UserFollowed
import com.apion.apionhome.data.model.User_B
import io.reactivex.rxjava3.core.Maybe

interface UserDatasource {

    interface Remote {

        fun getAllUsers(): Maybe<List<User_I>>
        fun getUserById(id: Int): Maybe<User_I>
        fun createUser(user: User_I): Maybe<Unit>
        fun updateUser(user: User_I): Maybe<User_I>
        fun uploadAvatar(id: Int, image: String): Maybe<User_I>
        fun checkExist(phone: String): Maybe<User_I>
        fun login(phone: String,pincode: String): Maybe<User_B>

        fun logout(id: Int, phone: String): Maybe<User_I>
        fun updatePincode(id: String, pin: String): Maybe<Unit>
        fun follow(followerId: Int, beingFollowedId: Int): Maybe<UserFollowed>
        fun unFollow(followerId: Int, beingFollowedId: Int): Maybe<UserFollowed>
    }
    interface Local{
        fun updatePhoneNearest(phone: String?): Boolean
        fun getPhoneNearest(): String?
        fun getPincodeNearest(): String?
        fun updatePincodeNearest(pincode: String?): Boolean
    }
}
