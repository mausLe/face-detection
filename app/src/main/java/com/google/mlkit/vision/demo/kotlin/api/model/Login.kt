package com.google.mlkit.vision.demo.kotlin.api.model

// import api.ThuaNguyenJSONX
// import com.google.gson.annotations.SerializedName

/*
data class UserInfo(
        @SerializedName("token") val token: String?,
        @SerializedName("data") val data: ThuaNguyenJSONX?
)

 */

class Login(var user_name: String, var password: String) {

        fun Login(user_name: String, password: String) {
                this.user_name = user_name
                this.password = password
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