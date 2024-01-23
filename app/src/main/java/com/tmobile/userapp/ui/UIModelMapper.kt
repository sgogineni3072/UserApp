package com.tmobile.userapp.ui

import android.content.res.Resources
import com.tmobile.core.data.models.UserResponse

class UIModelMapper(private val resources: Resources) {
    fun mapNetworkResponseToUIModel(
        userResponse: UserResponse
    ): List<UserUIModel> {
        val userUIModelList = ArrayList<UserUIModel>()

        userResponse.data.forEach { user ->
            userUIModelList.add(
                UserUIModel(
                    firstName = user.first_name,
                    lastName = user.last_name,
                    email = user.email,
                    avatarUrl = user.avatar
                )
            )
        }
        return userUIModelList
    }
}