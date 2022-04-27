package com.apion.apionhome.data.repository

import com.apion.apionhome.data.model.User_I
import com.apion.apionhome.data.model.UserFollowed
import com.apion.apionhome.data.model.User_B
import com.apion.apionhome.data.source.UserDatasource
import io.reactivex.rxjava3.core.Maybe
import java.lang.Exception

class UserRepositoryImpl(private val remote: UserDatasource.Remote
                        ,private val local: UserDatasource.Local) : UserRepository {

    override fun getAllUsers(): Maybe<List<User_I>> = remote.getAllUsers()

    override fun getUserById(id: Int): Maybe<User_I> = remote.getUserById(id)

    override fun createUser(user: User_I): Maybe<Unit> {
        return try {
            remote.createUser(user)
        } catch (exception: Exception) {
            Maybe.error(exception)
        }
    }

    override fun updateUser(user: User_I): Maybe<User_I> {
        return try {
            remote.updateUser(user)
        } catch (exception: Exception) {
            Maybe.error(exception)
        }
    }

    override fun checkExist(phone: String): Maybe<User_I> {
        return try {
            remote.checkExist(phone)
        } catch (exception: Exception) {
            exception.printStackTrace()
            Maybe.error(exception)
        }
    }

    override fun login(phone: String, pincode: String): Maybe<User_B> {
        return try {
            remote.login(phone,pincode)
        } catch (exception: Exception) {
            exception.printStackTrace()
            Maybe.error(exception)
        }
    }


    override fun follow(followerId: Int, beingFollowedId: Int): Maybe<UserFollowed> {
        return try {
            remote.follow(followerId, beingFollowedId)
        } catch (exception: Exception) {
            exception.printStackTrace()
            Maybe.error(exception)
        }
    }

    override fun unFollow(followerId: Int, beingFollowedId: Int): Maybe<UserFollowed> {
        return try {
            remote.unFollow(followerId, beingFollowedId)
        } catch (exception: Exception) {
            exception.printStackTrace()
            Maybe.error(exception)
        }
    }

    override fun uploadAvatar(id: Int, image: String): Maybe<User_I> {
        return try {
            remote.uploadAvatar(id, image)
        } catch (exception: Exception) {
            exception.printStackTrace()
            Maybe.error(exception)
        }
    }

    override fun logout(id: Int, phone: String): Maybe<User_I> = remote.logout(id, phone)

    override fun updatePincode(id: String, pin: String): Maybe<Unit> = remote.updatePincode(id, pin)
    override fun updatePhoneNearest(phone: String?) {
        local.updatePhoneNearest(phone)
    }

    override fun getPhoneNearest(): String? {
        return local.getPhoneNearest()
    }

    override fun updatePincodeNearest(pincode: String?) {
        local.updatePincodeNearest(pincode)
    }

    override fun getPincodeNearest(): String? {
        return local.getPincodeNearest()
    }



}
