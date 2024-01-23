package com.tmobile.userapp.repositories.api

import com.tmobile.core.data.models.UserResponse
import com.tmobile.userapp.network.NetworkResponse

interface UserRepository {
    suspend fun getUsers(): NetworkResponse<UserResponse>
}