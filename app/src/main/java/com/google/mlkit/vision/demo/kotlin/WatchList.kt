package com.google.mlkit.vision.demo.kotlin

import android.graphics.Bitmap


data class WatchList(var watchlistID: Int, var imageCode: Bitmap?, var name: String, var time: String, var type : String) {

}