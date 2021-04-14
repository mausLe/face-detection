package com.google.mlkit.vision.demo.kotlin.iojson

import com.google.gson.annotations.SerializedName

class Watchlist (
        @SerializedName("id")
        private var id: String,

        @SerializedName("name")
        private var name: String,

        @SerializedName("imageId")
        private var imageId: String,

        @SerializedName("type")
        private var type: String)
{
    fun add (name : String, id: String, imageID: String, type: String) {
        this.id = id
        this.name = name
        this.imageId = imageID
        this.type = type
    }

    fun getId (): String {
        return this.id
    }
    fun setId (id : String) {
        this.id = id
    }

    fun getName (): String {
        return this.name
    }
    fun setName (name : String) {
        this.name = name
    }

    fun getImageId (): String {
        return this.imageId
    }
    fun setImageId (imageId: String) {
        this.imageId = imageId
    }

    fun getType (): String {
        return this.type
    }
    fun setType (type : String) {
        this.type = type
    }
}
/*
class Watchlist {
        @SerializedName("member")
        private var member : ArrayList<Member>

    fun set (name : String, id: String, imageID: String) {
        this.mFullName = name
        this.mID = id
        this.myImageID = imageID
    }
}
 */