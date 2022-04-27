package com.apion.apionhome.viewmodel

import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.community.Community
import com.apion.apionhome.data.repository.CommunityRepository
import com.apion.apionhome.utils.setup

class CommunityViewModel(val communityRepository: CommunityRepository) : RxViewModel() {

    fun createCommunity(avatar: String?, cover: String?, community: Community) {
        communityRepository
            .createCommunity(cover, avatar, community)
            .setup()
            .subscribe(
                {
                    println(it)
                },
                {
                    it.printStackTrace()
                }
            )
    }
}
