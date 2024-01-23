package com.tmobile.userapp.repositories.api

import com.tmobile.core.data.models.UserResponse
import com.tmobile.userapp.network.NetworkResponse
import com.tmobile.userapp.network.UserDataSource

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun getUsers(): NetworkResponse<UserResponse> {
        return userDataSource.getUsers()
    }
}