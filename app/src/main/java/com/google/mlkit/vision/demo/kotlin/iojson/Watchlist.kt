package com.google.mlkit.vision.demo.kotlin.iojson

import com.google.gson.annotations.SerializedName

// Watchlist contains: Blacklist, CostomerList
class Watchlist (
        @SerializedName("blacklít")
        private var member : ArrayList<Member>)
{
    fun set (name : String, id: String, imageID: String) {
        // this.
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