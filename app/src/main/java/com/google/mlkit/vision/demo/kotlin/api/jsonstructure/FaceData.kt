package com.google.mlkit.vision.demo.kotlin.api.jsonstructure

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class FaceData (
    @SerializedName("token")
    @Expose
    private var token: String? = null,

    @SerializedName("data")
    @Expose
    private var data: data? = null)

    /*
    // get and set token
    fun getToken (): String? {
        return token
    }
    fun setToken (token: String){
        this.token = token
    }

    // get and set data
    fun getData (): data? {
        return data
    }
    fun setData (data: data){
        this.data = data
    }

}

     */

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

/*
class data {
    @SerializedName("image_encoded")
    @Expose
    private var image_encoded: String? = null

    @SerializedName("class_id")
    @Expose
    private var class_id: String? = null

    @SerializedName("model")
    @Expose
    private var model: String? = null

    @SerializedName("classifier")
    @Expose
    private var classifier: String? = null


    // Getter and Setter
    fun getImageEncoded (): String? {
        return image_encoded
    }
    fun setImageEncoded (image_encoded: String){
        this.image_encoded = image_encoded
    }

    fun getClassId (): String? {
        return class_id
    }
    fun setClassId (class_id: String){
        this.class_id = class_id
    }

    fun getModel (): String? {
        return model
    }
    fun setModel (model: String){
        this.model = model
    }

    fun getClassifier (): String? {
        return model
    }
    fun setClassifier (classifier: String){
        this.classifier = classifier
    }
}

 */