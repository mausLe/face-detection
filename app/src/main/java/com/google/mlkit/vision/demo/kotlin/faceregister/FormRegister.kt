package com.google.mlkit.vision.demo.kotlin.faceregister

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import api.FaceRegServerData
import com.google.mlkit.vision.demo.R
import com.google.mlkit.vision.demo.kotlin.api.Constants
import com.google.mlkit.vision.demo.kotlin.api.jsonstructure.FaceRegisterData
import com.google.mlkit.vision.demo.kotlin.api.jsonstructure.ImageEncoder
import com.google.mlkit.vision.demo.kotlin.api.jsonstructure.RegisterData
import com.google.mlkit.vision.demo.kotlin.api.service.ImageData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.nio.charset.Charset


class FormRegister: AppCompatActivity() {
    var builder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL) // change this IP for testing by your actual machine IP
            .addConverterFactory(GsonConverterFactory.create())

    var retrofit = builder.build()
    // var userClient = retrofit.create(UserClient::class.java)

    var userData = retrofit.create(ImageData::class.java)
    var type = "Student"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vision_add_face)

        var btnRegister = findViewById<Button>(R.id.btnRegister)
        var txtName = findViewById<EditText>(R.id.nameFaceRegister)
        var txtId = findViewById<EditText>(R.id.idFaceRegister)

        var frontBAFace = intent.getByteArrayExtra("frontal face")
        var frontFace = BitmapFactory.decodeByteArray(frontBAFace, 0, frontBAFace!!.size)

        var leftBAFace = intent.getByteArrayExtra("left face")
        var leftFace = BitmapFactory.decodeByteArray(leftBAFace, 0, leftBAFace!!.size)

        var rightBAFace = intent.getByteArrayExtra("right face")
        var rightFace = BitmapFactory.decodeByteArray(rightBAFace, 0, rightBAFace!!.size)

        var imageFace = findViewById<ImageView>(R.id.imageFaceRegister)
        imageFace.setImageBitmap(frontFace)

        var frontImage = encodeImage(frontFace!!) // Encode Base64 for bitmap
        // Decode to UTF-8
        var frontUTF = String(frontImage!!.toByteArray(), Charset.forName("UTF-8"))

        var leftImage = encodeImage(leftFace!!) // Encode Base64 for leftp
        // Decode to UTF-8
        var leftUTF = String(leftImage!!.toByteArray(), Charset.forName("UTF-8"))

        var rightImage = encodeImage(rightFace!!) // Encode Base64 for bitmap
        // Decode to UTF-8
        var rightUTF = String(rightImage!!.toByteArray(), Charset.forName("UTF-8"))

        var encodedFace = ImageEncoder(frontImage, leftImage, rightImage)

        btnRegister.setOnClickListener {
            var myRegData = RegisterData(1, txtName.text.toString(),
            txtId.text.toString(), "test", "test",
            "0", "0", encodedFace)

            var sendOutData = FaceRegisterData(Constants.token, myRegData)

            var data = userData.registerFaces(sendOutData)

            try {
                data!!.enqueue(object : Callback<FaceRegServerData?>{
                    override fun onFailure(call: Call<FaceRegServerData?>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                    override fun onResponse(call: Call<FaceRegServerData?>, response: Response<FaceRegServerData?>) {
                        TODO("Not yet implemented")
                    }

                })

            } catch (e : Exception) {
                Log.v("Retrofit catch", "Fail")
                Toast.makeText(this, "Retrofit Catch Exception",
                        Toast.LENGTH_SHORT).show()
            }
        }

    }



    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.title) {
            "Teacher" -> {
                type = "Teacher"
                return true
            }
            else -> {
                type = "Student"
                return false
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        // menu.setHeaderTitle("Context Menu")
        menu.add(0, v.id, 0, "Teacher")
        menu.add(0, v.id, 0, "Student")
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }



}