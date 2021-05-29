package com.google.mlkit.vision.demo.kotlin.api.service

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RTADClientData (
        @SerializedName("token")
        @Expose
        private var token: String? = null,

        @SerializedName("image_encoded")
        @Expose
        private var image_encoded: String? = null)


/*

        @SerializedName("data")
        @Expose
        private var data: data? = null)


class data (
        @SerializedName("image_encoded")
        @Expose
        private var image_encoded: String? = null,

        @SerializedName("class_id")
        @Expose
        private var class_id: String? = null,

        @SerializedName("model")
        @Expose
        private var model: String? = null,

        @SerializedName("classifier")
        @Expose
        private var classifier: String? = null)
 */