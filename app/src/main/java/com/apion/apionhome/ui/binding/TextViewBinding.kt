package com.apion.apionhome.ui.binding

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.apion.apionhome.MyApplication
import com.apion.apionhome.R
import com.apion.apionhome.data.model.community.Community
import java.util.*

@BindingAdapter("type")
fun houseType(view: TextView, type: String?) {
    val text = if (type == "For Sale") {
        view.context.getString(R.string.title_for_sale)
    } else {
        view.context.getString(R.string.title_for_rent)
    }
    view.text = text
}

@BindingAdapter("status")
fun houseStatus(view: TextView, status: String?) {
    val text = when (status) {
        "Ready For Sale" -> {
            view.context.getString(R.string.title_ready_for_sale)
        }
        "In Review" -> {
            "Đang review"
        }
        "Removed" -> {
            "Đã xóa"
        }
        "Sold" -> {
            "Đã bán"
        }
        else -> {
            "Đang bán"
        }
    }
    view.text = text
}

@BindingAdapter("textPhone")
fun setTextPhone(view: TextView, phone: String?) {
    var result = ""
    phone?.let {
        if (it.length >= 7) {
            result = "+84 " + phone.subSequence(1, 4) + " " + phone.subSequence(
                4,
                7
            ) + " " + phone.subSequence(7, phone.length)
            view.text = result
        }
    }


//
}

@BindingAdapter("textDob")
fun setTextDob(view: TextView, cal: Calendar?) {

    cal?.let {
        var dom = "" + it.get(Calendar.DAY_OF_MONTH)
        if (dom.toInt() < 10) {
            dom = "0" + dom
        }
        var month = "" + (it.get(Calendar.MONTH) + 1)
        if (month.toInt() < 10) {
            month = "0" + month
        }
        view.text = "" + dom + "/" + month + "/" + it.get(Calendar.YEAR)
        return
    }
    view.text = ""
}


@BindingAdapter(value = ["position", "permission"])
fun userPositionPermission(view: TextView, permission: String?, position: String?) {
    val textPer = if (permission == "Host Side") {
        "đầu chủ"
    } else {
        "đầu khách"
    }
    val textPos = when (position) {
        "Director" -> {
            "Giám đốc"
        }
        "Earl" -> {
            "Bá tước"
        }
        "Manager" -> {
            "Trưởng phòng"
        }
        "Leader" -> {
            "Trưởng nhóm"
        }
        "Captain" -> {
            "Đội trưởng"
        }
        "Expert" -> {
            "Chuyên viên"
        }
        else -> {
            "Chuyên viên"
        }
    }
    view.text = "$textPos $textPer"
}

@BindingAdapter("direction")
fun houseDirection(view: TextView, direction: String?) {
    var text = direction?.replace("East", "Đông", true)
    text = text?.replace("West", "Tây", true)
    text = text?.replace("South", "Nam", true)
    text = text?.replace("North", "Bắc", true)
    text = text?.replace("Direction", "Chưa rõ", true)

    val datas = text?.split("-")
    datas?.let {
        val homeDirection = if (datas.size == 2) "${datas[1]} ${datas[0]}" else datas[0]
        view.text = homeDirection
    }
}

@BindingAdapter("community")
fun joinCommunity(view: Button, communityId: Int?) {
    val isJoin = MyApplication.sessionUser.value?.participants?.firstOrNull {
        it.community.id == communityId
    } != null
    if (isJoin) {
        view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.color_harp))
        view.text = view.context.resources.getString(R.string.title_out)
        view.setTextColor(ContextCompat.getColor(view.context, R.color.color_primary))
    } else {
        view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.color_primary))
        view.text = view.context.resources.getString(R.string.title_join)
        view.setTextColor(Color.WHITE)
    }
}

@BindingAdapter(value = ["textCustom", "tail"])
fun setTextCustom(view: TextView, text: String?, tail: String?) {
    when {
        text?.contains("Tất cả") == true -> {
            view.text = "Tất cả $tail"
        }
        text?.contains("Toàn bộ") == true -> {
            view.text = tail
        }
        else -> {
            view.text = text
        }
    }
}

@BindingAdapter(value = ["moreAction", "statusAction"])
fun setTextCustom(view: TextView, moreAction: Int, statusAction: String) {
    if (moreAction == MyApplication.sessionUser.value?.id
        && statusAction != "Removed"
        && statusAction != "Sold"
    ) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("checkFollow")
fun checkFollow(view: Button, userId: String) {
    view.findViewTreeLifecycleOwner()?.let {
        MyApplication.sessionUser.observe(it) {
            var isFollow = it?.isFollowing(userId) ?: false
            when {
                it?.id.toString() == userId -> {
                    view.text = "Ký nhà"
                    view.background =
                        AppCompatResources.getDrawable(view.context, R.drawable.bg_button)
                    view.setTextColor(view.context.getColor(R.color.white))
                }
                isFollow -> {
                    view.text = "Bỏ theo dõi"
                    view.background =
                        AppCompatResources.getDrawable(view.context, R.drawable.bg_button_outlined)
                    view.setTextColor(view.context.getColor(R.color.color_primary))
                }
                else -> {
                    view.text = "Theo dõi"
                    view.background =
                        AppCompatResources.getDrawable(view.context, R.drawable.bg_button)
                    view.setTextColor(view.context.getColor(R.color.white))
                }
            }
        }
    }
}
