package com.google.mlkit.vision.demo.kotlin.api.service

import com.google.mlkit.vision.demo.kotlin.api.model.Login
import com.google.mlkit.vision.demo.kotlin.api.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface UserClient {
    @POST("user_login/post/")
    open fun login(@Body login: Login?): Call<User?>?

    @GET("user_login/post/")
    open fun getSecret(@Header("Authorization") authToken: String?): Call<ResponseBody?>?
    // fun fetchPosts(@Header("Authorization") token: String): Call<ThuaNguyenJSONX>
}

/*
interface NoteClient {
    @GET()
    fun getToken():Call<ThuaNguyenJSON>

}

 */