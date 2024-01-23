package com.tmobile.core.network

import com.tmobile.core.data.models.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class UserService(
    private val httpClient: HttpClient,
    private val endpoint: String
) {
    suspend fun getUsers(): UserResponse {
        return httpClient.get(endpoint) {
        }
    }
}