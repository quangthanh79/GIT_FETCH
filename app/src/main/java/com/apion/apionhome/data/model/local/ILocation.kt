package com.apion.apionhome.data.model.local

import com.apion.apionhome.data.model.GeneraEntity

interface ILocation : GeneraEntity {
    fun getTitle(): String
    fun getContent(): String
}