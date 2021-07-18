package com.google.mlkit.vision.demo.kotlin.api.dashboard

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class syncData (
        @SerializedName("studentid")
        @Expose
        private var studentid: String? = null,

        @SerializedName("name")
        @Expose
        private var name: String? = null,

        @SerializedName("phone")
        @Expose
        private var phone: String? = null,

        @SerializedName("image")
        @Expose
        private var image: String? = null,

        @SerializedName("type")
        @Expose
        private var type: Int? = null)


