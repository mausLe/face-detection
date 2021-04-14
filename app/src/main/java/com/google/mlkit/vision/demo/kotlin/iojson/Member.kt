package com.google.mlkit.vision.demo.kotlin.iojson

import com.google.gson.annotations.SerializedName

class Member (
        @SerializedName("name")
        private var mFullName: String,

        @SerializedName("id")
        private var mID: String,

        @SerializedName("imageId")
        private var mImageID: String,

        @SerializedName("characteristic")
        private var characteristic: String)
