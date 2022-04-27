package com.apion.apionhome.data.repository

import com.apion.apionhome.data.model.community.Community
import com.apion.apionhome.data.model.community.Participant
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

interface CommunityRepository {

    fun getAllCommunities(): Maybe<List<Community>>

    fun getCommunityById(id: Int): Maybe<Community>

    fun createCommunity(
        coverPath: String?,
        avatarPath: String?,
        community: Community
    ): Maybe<Community>

    fun updateCommunity(
        coverPath: String?,
        avatarPath: String?,
        community: Community
    ): Maybe<Community>

    fun getAllParticipants(): Maybe<List<Participant>>

    fun getParticipantById(id: Int): Maybe<Participant>

    fun createParticipant(userId: Int, communityId: Int): Maybe<Participant>

    fun updateParticipant(id: Int, userId: Int, communityId: Int): Maybe<Participant>

    fun leaveCommunity(id: Int, userId: Int, communityId: Int): Completable
}
