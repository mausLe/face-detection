package com.google.mlkit.vision.demo.kotlin.api.jsonstructure

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class FaceRegisterData (
        @SerializedName("token")
        @Expose
        private var token: String? = null,

        @SerializedName("data")
        @Expose
        private var data: RegisterData? = null)

class RegisterData (
        @SerializedName("force")
        @Expose
        private var force: Int? = null,

        @SerializedName("student_name")
        @Expose
        private var student_name: String? = null,

        @SerializedName("student_id")
        @Expose
        private var student_id: String? = null,

        @SerializedName("student_class")
        @Expose
        private var student_class: String? = null,

        @SerializedName("student_faculty")
        @Expose
        private var student_faculty: String? = null,

        @SerializedName("class_id")
        @Expose
        private var class_id: String? = null,

        @SerializedName("device_id")
        @Expose
        private var device_id: String? = null,

        @SerializedName("image_encoded")
        @Expose
        private var image_encoded: ImageEncoder? = null)


class ImageEncoder (
        @SerializedName("frontal")
        @Expose
        private var frontal: String? = null,

        @SerializedName("left")
        @Expose
        private var left: String? = null,

        @SerializedName("right")
        @Expose
        private var right: String? = null

)

