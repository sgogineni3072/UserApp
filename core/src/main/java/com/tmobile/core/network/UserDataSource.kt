package com.tmobile.core.network

import com.tmobile.core.data.models.UserResponse
import com.tmobile.core.utils.NetworkErrorParser

class UserDataSource(
    private val userService: UserService,
    private val networkErrorParser: NetworkErrorParser
) {
    suspend fun getUsers(): NetworkResponse<UserResponse> {
        return try {
            NetworkResponse.Success(userService.getUsers())
        } catch (e: Throwable) {
            NetworkResponse.Error(networkErrorParser.checkError(e))
        }
    }
}