package com.google.mlkit.vision.demo.kotlin.api.dashboard

class DashboardFeedback (status: String) {
    private var status = status

    // get and set status
    fun getStatus (): String {
        return status
    }
    fun setStatus (status: String){
        this.status = status
    }
}