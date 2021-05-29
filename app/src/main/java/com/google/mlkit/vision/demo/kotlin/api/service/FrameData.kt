package com.google.mlkit.vision.demo.kotlin.api.service


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface FrameData {
    @POST("predict")
    open fun postData(@Body rtadClientData: RTADClientData?): Call<RTADServerData?>?



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