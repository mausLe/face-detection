package com.google.mlkit.vision.demo.kotlin.api.model

import api.data

class FaceImage (token: String, data: data) {
    private var token = token
    private var data = data

    // get and set token
    fun getToken (): String {
        return token
    }
    fun setToken (token: String){
        this.token = token
    }

    // get and set token
    fun getData (): data {
        return data
    }
    fun setData (data: data){
        this.data = data
    }
}