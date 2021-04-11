package com.google.mlkit.vision.demo.kotlin.api.model

class User (code: String, status: String, token: String) {

    private var code = code
    private var status = status
    private var token = token

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

    // get and set token
    fun getToken (): String {
        return token
    }
    fun setToken (token: String){
        this.token = token
    }
}



/*
data class LoginResponse (
        @SerializedName("code")
        var code: String,

        @SerializedName("status")
        var status: String,

        @SerializedName("token")
        var token: String
)
 */
