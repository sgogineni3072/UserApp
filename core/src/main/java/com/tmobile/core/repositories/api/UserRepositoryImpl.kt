package com.tmobile.core.repositories.api

import com.tmobile.core.data.models.UserResponse
import com.tmobile.core.network.NetworkResponse
import com.tmobile.core.network.UserDataSource

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun getUsers(): NetworkResponse<UserResponse> {
        return userDataSource.getUsers()
    }
}