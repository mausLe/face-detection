package com.google.mlkit.vision.demo.kotlin.api.service

import api.FaceRegServerData
import api.ServerData
import api.data
import com.google.mlkit.vision.demo.kotlin.api.jsonstructure.FaceData
import com.google.mlkit.vision.demo.kotlin.api.jsonstructure.FaceRegisterData
import com.google.mlkit.vision.demo.kotlin.api.model.Login
import com.google.mlkit.vision.demo.kotlin.api.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface UserClient {
    @POST("user_login/post/")
    open fun login(@Body login: Login?): Call<User?>?

    @GET("user_login/post/")
    open fun getSecret(@Header("Authorization") authToken: String?): Call<ResponseBody?>?

}

interface  ImageData {
    @POST("search_face/post/")
    open fun postData(@Body faceData: FaceData?): Call<ServerData?>?

    @POST("register/post/")
    open fun registerFaces(@Body faceData: FaceRegisterData?): Call<FaceRegServerData?>?


    // this one works but server returns wrong input
    /*
    @POST("search_face/post/")
    open fun postData(@Body searchFace: SearchFace?): Call<ServerData?>?

     */

    /*
    @POST("search_face/post/")
    open fun postData(@Field("token") token: String,
                      @Field("data") searchFace: data): Call<ServerData?>?

     */

    // @GET("search_face/post/")
    // open fun getData():  Call<ResponseBody?>?

}

/*
interface NoteClient {
    @GET()
    fun getToken():Call<ThuaNguyenJSON>

}

 */