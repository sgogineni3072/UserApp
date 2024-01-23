package com.tmobile.core.repositories.api

import com.tmobile.core.data.models.UserResponse
import com.tmobile.core.network.NetworkResponse

interface UserRepository {
    suspend fun getUsers(): NetworkResponse<UserResponse>
}