package com.apion.apionhome.utils

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject

fun Any.toMap(): Map<String, RequestBody> {
    try {
        val json = Gson().toJson(this)
        val jsonObject = JSONObject(json)
        return jsonObject.keys().asSequence().associateWith {
            RequestBody.create(MediaType.parse("text/plain"), jsonObject[it].toString())
        }
    } catch (exception: JsonIOException) {
        return mapOf()
    }
}

fun JsonObject.toMap(): Map<String, RequestBody> {
    try {
        val jsonObject = JSONObject(this.toString())
        return jsonObject.keys().asSequence().associateWith {
            val result = when (val value = jsonObject[it]) {
                is JSONArray -> value.toString()
                is JSONObject -> value.toString()
                else -> value.toString()
            }
            RequestBody.create(MediaType.parse("text/plain"), result)
        }
    } catch (exception: JsonIOException) {
        return mapOf()
    }
}

fun JSONObject.toMap(): Map<String, RequestBody> {

    return keys().asSequence().associateWith {
        RequestBody.create(MediaType.parse("text/plain"), this[it].toString())
    }
}
