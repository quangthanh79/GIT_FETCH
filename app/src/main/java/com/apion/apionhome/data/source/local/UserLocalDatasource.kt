package com.apion.apionhome.data.source.local

import android.content.Context
import android.content.SharedPreferences
import com.apion.apionhome.data.model.User_I
import com.apion.apionhome.data.source.UserDatasource
import io.reactivex.rxjava3.core.Maybe

class UserLocalDatasource(context: Context) : UserDatasource.Local{
    val myShare1 : SharedPreferences = context.getSharedPreferences("myShare1", Context.MODE_PRIVATE)
    val myShare2 : SharedPreferences = context.getSharedPreferences("myShare2", Context.MODE_PRIVATE)

    override fun updatePhoneNearest(phone: String?): Boolean {
        return myShare1.edit().putString("phone",phone).commit()

    }


    override fun getPhoneNearest(): String? {
        return myShare1.getString("phone",null)
    }

    override fun getPincodeNearest(): String? {
        return myShare2.getString("pincode",null)
    }

    override fun updatePincodeNearest(pincode: String?): Boolean {
        return myShare2.edit().putString("pincode",pincode).commit()
    }

}