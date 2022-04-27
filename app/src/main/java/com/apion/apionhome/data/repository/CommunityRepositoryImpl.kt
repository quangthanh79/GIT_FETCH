package com.apion.apionhome.data.repository

import com.apion.apionhome.data.model.community.Community
import com.apion.apionhome.data.model.community.Participant
import com.apion.apionhome.data.source.CommunityDatasource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

class CommunityRepositoryImpl(private val remote: CommunityDatasource.Remote) :
    CommunityRepository {

    override fun getAllCommunities(): Maybe<List<Community>> = remote.getAllCommunities()

    override fun getCommunityById(id: Int): Maybe<Community> = remote.getCommunityById(id)

    override fun createCommunity(
        coverPath: String?,
        avatarPath: String?,
        community: Community
    ): Maybe<Community> = remote.createCommunity(coverPath, avatarPath, community)

    override fun updateCommunity(
        coverPath: String?,
        avatarPath: String?,
        community: Community
    ): Maybe<Community> = remote.updateCommunity(coverPath, avatarPath, community)

    override fun getAllParticipants(): Maybe<List<Participant>> = remote.getAllParticipants()

    override fun getParticipantById(id: Int): Maybe<Participant> = remote.getParticipantById(id)

    override fun createParticipant(userId: Int, communityId: Int): Maybe<Participant> =
        remote.createParticipant(userId, communityId)

    override fun updateParticipant(id: Int, userId: Int, communityId: Int): Maybe<Participant> =
        remote.updateParticipant(id, userId, communityId)

    override fun leaveCommunity(id: Int, userId: Int, communityId: Int): Completable =
        remote.leaveCommunity(id, userId, communityId)
}
