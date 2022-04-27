package com.apion.apionhome.data.model

data class Range(
    var min: Int = -1,
    var max: Int = -1,
    val dram: String = ""
) {
    override fun toString(): String {
        return "from${min}" + "to${max}" + dram
    }

    fun getData(): String {
        return "$min - $max"
    }
}
