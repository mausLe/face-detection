package com.google.mlkit.vision.demo.kotlin.api.dashboard


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface syncDashboard {
    @POST("api/task-create/")
    open fun syncData(@Body syncDashboard: syncData?): Call<DashboardFeedback?>?

}

