package com.google.mlkit.vision.demo.kotlin.faceregister

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.demo.R

class FormRegister: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vision_add_face)

        var frontUTF = intent.getByteArrayExtra("frontal UTF")
        var frontFace = BitmapFactory.decodeByteArray(frontUTF, 0, frontUTF!!.size)

        var leftUTF = intent.getStringExtra("left UTF")
        var rightUTF = intent.getStringExtra("right UTF")

        var imageFace = findViewById(R.id.imageFaceRegister) as ImageView
        imageFace.setImageBitmap(frontFace)

    }



}