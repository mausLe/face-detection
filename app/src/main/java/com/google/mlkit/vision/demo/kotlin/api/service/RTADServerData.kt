package com.google.mlkit.vision.demo.kotlin.api.service

class RTADServerData (code: String, status: String) {

    private var code = code
    private var status = status
    // get and set code
    fun getCode (): String {
        return code
    }
    fun setCode (code: String){
        this.code = code
    }

    // get and set status
    fun getStatus (): String {
        return status
    }
    fun setStatus (status: String){
        this.status = status
    }

}