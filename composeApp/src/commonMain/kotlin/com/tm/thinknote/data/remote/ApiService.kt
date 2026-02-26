package com.tm.thinknote.data.remote

import com.tm.thinknote.data.cache.DataStoreManager
import com.tm.thinknote.model.AuthRequest
import com.tm.thinknote.model.AuthResponse
import com.tm.thinknote.model.SyncRequest
import com.tm.thinknote.model.SyncResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode

expect val BASE_URL_EMULATOR: String
const val BASE_URL = "http://localhost:8080"

class ApiService(val client: HttpClient, val dataStoreManager: DataStoreManager) {

    private val ACTIVE_URL = BASE_URL_EMULATOR
    private val LOGIN_ENDPOINT = "${ACTIVE_URL}/auth/login"
    private val SIGNUP_ENDPOINT = "${ACTIVE_URL}/auth/signup"
    private val SYNC_ENDPOINT = "${ACTIVE_URL}/sync"

    suspend fun login(request: AuthRequest): Result<AuthResponse> {
        return try {
            val response = client.post(urlString = LOGIN_ENDPOINT) {
                setBody(request)
            }
            if (response.status == HttpStatusCode.OK) {
                Result.success(response.body() as AuthResponse)
            } else {
                Result.failure(Exception("Failed to load data"))
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun signup(request: AuthRequest): Result<AuthResponse> {
        return try {
            val response = client.post(urlString = SIGNUP_ENDPOINT) {
                setBody(request)
            }
            if (response.status == HttpStatusCode.Created) {
                Result.success(response.body() as AuthResponse)
            } else {
                Result.failure(Exception("Failed to load data"))
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun sync(request: SyncRequest): Result<SyncResponse> {
        return try {
            val response = client.post(urlString = SYNC_ENDPOINT) {
                setBody(request)
                header(HttpHeaders.Authorization, "Bearer ${dataStoreManager.getToken() ?: ""}")
            }
            if (response.status == HttpStatusCode.OK) {
                Result.success(response.body() as SyncResponse)
            } else {
                Result.failure(Exception("Failed to load data"))
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}