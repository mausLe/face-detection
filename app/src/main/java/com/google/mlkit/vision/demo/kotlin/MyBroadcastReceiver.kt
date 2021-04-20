package com.google.mlkit.vision.demo.kotlin

import android.R
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.widget.Toast


class MyBroadcastReceiver : BroadcastReceiver() {
    var mp: MediaPlayer? = null
    override fun onReceive(context: Context, intent: Intent) {

        mp!!.start()
        Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show()
    }
}