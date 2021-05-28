package com.google.mlkit.vision.demo.kotlin.faceregister

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import api.FaceRegServerData
import com.google.gson.Gson
import com.google.mlkit.vision.demo.R
import com.google.mlkit.vision.demo.kotlin.api.Constants
import com.google.mlkit.vision.demo.kotlin.api.jsonstructure.FaceRegisterData
import com.google.mlkit.vision.demo.kotlin.api.jsonstructure.ImageEncoder
import com.google.mlkit.vision.demo.kotlin.api.jsonstructure.RegisterData
import com.google.mlkit.vision.demo.kotlin.api.service.ImageData
import com.google.mlkit.vision.demo.kotlin.iojson.Watchlist
import com.google.mlkit.vision.demo.kotlin.watchlist
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
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

        val spinner: Spinner = findViewById(R.id.spinerType)
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
                R.array.watchlistType, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing
            }

            override fun onItemSelected(parent: AdapterView<*>?,
                                        view: View?, position: Int, id: Long) {
                type = parent!!.getItemAtPosition(position).toString()

                // Toast.makeText(parent.context, "Type$type", Toast.LENGTH_SHORT).show()
            }
        }





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

        var encodedFace = ImageEncoder(frontUTF, leftUTF, rightUTF)

        btnRegister.setOnClickListener {
            var myRegData = RegisterData(0, txtName.text.toString(),
            txtId.text.toString(), "test", "test",
            "0", "0", encodedFace)

            var sendOutData = FaceRegisterData(Constants.token, myRegData)
            var data = userData.registerFaces(sendOutData)

            try {
                data!!.enqueue(object : Callback<FaceRegServerData?> {
                    override fun onFailure(call: Call<FaceRegServerData?>, t: Throwable) {
                        Log.v("Retrofit sendD Fail Res", t.toString())
                        Toast.makeText(this@FormRegister, "Retrofit Catch Exception",
                                Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<FaceRegServerData?>, response: Response<FaceRegServerData?>) {
                        if (response.isSuccessful) {
                            var receivedData : FaceRegServerData? = response.body()!!
                            Log.v("MMLab response: ", receivedData.toString())

                            var serverCode = receivedData!!.getCode()
                            var serverStatus = receivedData.getStatus()

                            var display = receivedData.toString()
                            // serverResponse = serverData.data.student_id

                            when {
                                serverCode.toInt() == 1000 -> {
                                    var serverData = receivedData.getData()

                                    Log.v("MMLab response: ", "Code $serverCode\nStatus $serverStatus" +
                                            "\nStudent: ${serverData.student_name}")


                                    // Add name to
                                    var type = when(serverData.teacher) {
                                        1 -> "Teacher"
                                        0 -> "Student"
                                        else -> "Other"
                                    }
                                    if (serverData.student_id == "17520237") type = "Teacher" // Teacher will be marked as red


                                    // Check if that's person is on blacklist
                                    var foundMember = false
                                    for (i in 0 until watchlist.size) {
                                        if (watchlist[i].getId() == serverData.student_id) {
                                            foundMember = true
                                            break
                                        }
                                    }
                                    if (!foundMember) {
                                        watchlist.add(Watchlist(txtId.text.toString(), txtName.text.toString(), serverData.image_id, type))
                                        Toast.makeText(this@FormRegister, "Register Successful. Your ID: " + serverData.student_id,
                                                Toast.LENGTH_LONG).show()
                                    }

                                    // Re-assign Name
                                    // broadcastArrayWatchlistChanged(pos, name, type, serverData.student_id)
                                }
                                serverCode.toInt() == 706 -> {
                                    Log.v("MMLab response: ", "Code $serverCode\nStatus $serverStatus\nStudent: Unknown")
                                    Toast.makeText(this@FormRegister, "status: Data Already In Dataset",
                                            Toast.LENGTH_SHORT).show()

                                    // Re-assign Name
                                    // broadcastArrayWatchlistChanged(pos, name, "Other", "Unknown")

                                }
                                else -> {
                                    Log.v("MMLab response: ", "Code $serverCode\nStatus $serverStatus")
                                    Toast.makeText(this@FormRegister, "Error: Code $serverCode Status: $serverStatus",
                                            Toast.LENGTH_LONG).show()

                                    // broadcastArrayWatchlistChanged(pos, name, "Other", "Unknown")

                                }
                            }
                        }
                        else {
                            Log.v("Retrofit sendD Res else", "Fail")
                            Toast.makeText(this@FormRegister, "Can not Search =((",
                                    Toast.LENGTH_SHORT).show()

                            // Re-assign Name
                            // broadcastArrayWatchlistChanged(pos, name, "Other", "Unknown")
                        }
                    }

                })

            } catch (e : Exception) {
                Log.v("Retrofit catch", "Fail")
                Toast.makeText(this, "Retrofit Catch Exception",
                        Toast.LENGTH_SHORT).show()
            }
        }

    }


    fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val text = parent.getItemAtPosition(position).toString()
        Toast.makeText(parent.context, text, Toast.LENGTH_SHORT).show()
    }

    fun onNothingSelected(parent: AdapterView<*>?) {}

    /*override fun onContextItemSelected(item: MenuItem): Boolean {
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
     */

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
