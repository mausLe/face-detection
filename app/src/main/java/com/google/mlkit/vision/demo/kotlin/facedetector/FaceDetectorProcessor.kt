/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.mlkit.vision.demo.kotlin.facedetector

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.text.Html
import android.util.Base64
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import api.ServerData
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.demo.GraphicOverlay
import com.google.mlkit.vision.demo.R
import com.google.mlkit.vision.demo.kotlin.*
import com.google.mlkit.vision.demo.kotlin.api.Constants
import com.google.mlkit.vision.demo.kotlin.api.jsonstructure.FaceData
import com.google.mlkit.vision.demo.kotlin.api.jsonstructure.data
import com.google.mlkit.vision.demo.kotlin.api.service.ImageData
import com.google.mlkit.vision.demo.kotlin.iojson.Watchlist
import com.google.mlkit.vision.face.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

var index = 0
var passedFrame = 0
var maxHuman = 30
// var watchlist = ArrayList<Watchlist>()
var repeatFaces = ArrayList<String>()


/** Face Detector Demo.  */
class FaceDetectorProcessor(context: Context, detectorOptions: FaceDetectorOptions?) :
  VisionProcessorBase<List<Face>>(context) {

  lateinit var imageBitmap: Bitmap
  private val detector: FaceDetector
  private var check = false
  var context = context
  val landMarkTypes = intArrayOf(
          FaceLandmark.MOUTH_BOTTOM,
          FaceLandmark.MOUTH_RIGHT,
          FaceLandmark.MOUTH_LEFT,
          FaceLandmark.RIGHT_EYE,
          FaceLandmark.LEFT_EYE,
          FaceLandmark.RIGHT_EAR,
          FaceLandmark.LEFT_EAR,
          FaceLandmark.RIGHT_CHEEK,
          FaceLandmark.LEFT_CHEEK,
          FaceLandmark.NOSE_BASE
  )
  val landMarkTypesStrings = arrayOf(
          "MOUTH_BOTTOM",
          "MOUTH_RIGHT",
          "MOUTH_LEFT",
          "RIGHT_EYE",
          "LEFT_EYE",
          "RIGHT_EAR",
          "LEFT_EAR",
          "RIGHT_CHEEK",
          "LEFT_CHEEK",
          "NOSE_BASE"
  )

  private var TAG = "FaceDetectorProcessor"



  // Using trackingId to determine if someone continue to appear on the screen view
  // If he/she is out, then
  private var currentID : ArrayList<Int> = arrayListOf()
  // private var appearTime : Array<IntArray> = Array(1000) {IntArray(2) {} }
  private var appearTime : Array<IntArray> = Array(1000) {IntArray(2) {0} }

  var gson : Gson = Gson()

  private var editTextView: TextView? = null
  private var editImageView: ImageView? = null
  private  var isShowDialog = false

  private var totalWatchlist = 0
  private var totalBlacklist = 0
  private var totalVIP = 0



  init {
    val options = detectorOptions
      ?: FaceDetectorOptions.Builder()
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .enableTracking()
        .build()

    detector = FaceDetection.getClient(options)

    Log.v(MANUAL_TESTING_LOG, "Face detector options: $options")

    // filePath: "/data/user/0/com.google.mlkit.vision.demo/files/out.json"
    /*
    val file = File(context.filesDir, "watchlist.json")
    var json = ""
    try {
      json = getJsonData(context, file.toString())
      if (json == "[]") {
        json = getAssetJsonData(context, "watchlist.json")
      }
    } catch (e : Exception) {
      json = getAssetJsonData(context, "watchlist.json")
    }
    watchlist = gson.fromJson(json, Array<Watchlist>::class.java).toList() as ArrayList<Watchlist>
     */


    // Init appearTime to skip the first 4 times of every human
    // and record a 5th time
    /*
    for (i in 1 until maxHuman) {
      appearTime[i - 1] = 1
    }

     */
  }

  var builder = Retrofit.Builder()
          .baseUrl(Constants.BASE_URL) // change this IP for testing by your actual machine IP
          .addConverterFactory(GsonConverterFactory.create())

  var retrofit = builder.build()
  // var userClient = retrofit.create(UserClient::class.java)

  var imageData = retrofit.create(ImageData::class.java)

  override fun stop() {
    super.stop()
    detector.close()
  }

  override fun detectInImage(image: InputImage): Task<List<Face>> {
    // Modify detectInImage method to save an instance of the bitmap
    // being detected and save it in a global variable

    // imageBitmap = image.bitmapInternal
    /*imageBitmap = Bitmap.createBitmap(image)
    Log.v ("\n\nImage Info", "imageBitmap: " + imageBitmap.toString())
    Log.v ("\nBitmap Info", "image: " + image.toString())
     */

    return detector.process(image)
  }

  @RequiresApi(Build.VERSION_CODES.KITKAT)
  override fun onSuccess(originalCameraImage: Bitmap?, results: List<Face>, graphicOverlay: GraphicOverlay) {
    // currentBitmap is our originalCameraImage
    /*
    Log.v (MANUAL_TESTING_LOG, "\n\nImage: " + originalCameraImage)
    var image = InputImage.fromBitmap(originalCameraImage, 0)

    // .load(originalCameraImage)
    Glide.with(detectedImage)
            .load(image)
            .into(detectedImage)
     */

    // graphicOverlay.clear()
    // Log.v (MANUAL_TESTING_LOG, "Results: " + results.toString())

    var croppedImage : Bitmap? = originalCameraImage


    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    val currentDate = sdf.format(Date())
    var faceChanged: Boolean = false

    for (face in results) {
      // Log.v (MANUAL_TESTING_LOG, "face: " + face.toString())

      graphicOverlay.add(FaceGraphic(graphicOverlay, face))

      // Display landmark/contour info
      // logExtrasForTesting(face)
      // Euler Y angle      -           Detectable landmarks
      // -36 degrees to -12 degrees
      // ---> left mouth, nose base, bottom mouth, right eye, left eye, left cheek, left ear tip

      // -12 degrees to 12 degrees
      // ---> right eye, left eye, nose base, left cheek, right cheek, left mouth,
      // right mouth, bottom mouth

      // 12 degrees to 36 degrees
      // ---> right mouth, nose base, bottom mouth, left eye, right eye, right cheek, right ear tip

      if (face.headEulerAngleY > 45 || face.headEulerAngleY < -45) continue

      // Facing upward
      if (face.headEulerAngleX > 20 || face.headEulerAngleY < -15) continue



      var rect = face.boundingBox
      var rightcoord = rect.right
      var leftcoord = rect.left
      var topcoord = rect.top
      var bottomcoord = rect.bottom


      if (rect.left < 0) leftcoord = 0
      if (rect.top < 0) topcoord = 0
      var w = rightcoord - leftcoord + 1
      var h = bottomcoord - topcoord + 1

      if (h + topcoord >= originalCameraImage!!.height) {
        h = originalCameraImage!!.height - topcoord
      }
      if (w + leftcoord >= originalCameraImage!!.width) {
        w = originalCameraImage!!.width - leftcoord
      }

      try {
        // Try to increase h to 0.7*originalCameraImage!!.height
        if (h > minHeight && h > 0.1*originalCameraImage.height
                && h/w >= 0.3 && w/h >= 0.3) {
          croppedImage = cropBitmap(originalCameraImage!!, leftcoord, topcoord, w, h)
        }
        else continue
      }
      catch (e : Exception) {
        // do nothing
        continue
      }

      /*

      val time = currentID.indexOf(face.trackingId)
      if (face.trackingId in currentID && appearTime[time] >= 5) {
        if (currentID.size > 30)  currentID.drop(29)
        currentID.add(0, face.trackingId)

        // arrayWatchlist.add(0, WatchList(face.trackingId, croppedImage, "Maus", currentDate))
        arrayWatchlist.add(0, WatchList(index, croppedImage, face.trackingId.toString(), currentDate))

        adapter?.notifyDataSetChanged()

        /*
        var decodedImage = encodeImage(croppedImage!!) // Encode Base64 for bitmap
        // Decode to UTF-8
        var utf = String(decodedImage!!.toByteArray(), Charset.forName("UTF-8"))
        sendData(utf, index)

         */
      }

       */

      /*
      if (face.trackingId in currentID) {
        for (i in appearTime.indices) {
          if (appearTime[i][0] == face.trackingId) {
            if (appearTime[i][1] >= 30) {
              passedFrame = 0
              faceChanged = true


              var decodedImage = encodeImage(croppedImage!!) // Encode Base64 for bitmap
              // Decode to UTF-8
              var utf = String(decodedImage!!.toByteArray(), Charset.forName("UTF-8"))

              var new_index = 0
              for (new_i in 0 until arrayWatchlist.size) {
                if (arrayWatchlist[new_i].watchlistID  == face.trackingId) {
                  new_index = new_i
                }
              }
              sendData(utf, new_index)
            } else passedFrame += 1
          } else passedFrame +=1
        }
      }*/


      if (face.trackingId !in currentID) {
        if (currentID.size > 30)  currentID.drop(29)
          currentID.add(0, face.trackingId)

        totalWatchlist += 1
        var textViewContent = "<font color=#ffffff>Total: $totalWatchlist</font> | <font color=#008000>$totalVIP</font> | <font color=#b32d00>$totalBlacklist</font>"
        txtView!!.setText(Html.fromHtml(textViewContent))

        arrayWatchlist.add(0, WatchList(face.trackingId, croppedImage, face.trackingId.toString(), currentDate, "Other"))



        // Add id and a time show up to appearTime to manage appear time
        appearTime[index][0] = face.trackingId
        appearTime[index][1] = 1

        faceChanged = true


        var decodedImage = encodeImage(croppedImage!!) // Encode Base64 for bitmap
        // Decode to UTF-8
        var utf = String(decodedImage!!.toByteArray(), Charset.forName("UTF-8"))
        sendData(utf, index)

        Log.v(
                MANUAL_TESTING_LOG,
                "face Euler Angle X: " + face.headEulerAngleX
        )
        Log.v(
                MANUAL_TESTING_LOG,
                "face Euler Angle Y: " + face.headEulerAngleY
        )
        Log.v(
                MANUAL_TESTING_LOG,
                "face Euler Angle Z: " + face.headEulerAngleZ
        )

        index += 1
      }

      }
    if (faceChanged) {
      adapter?.notifyDataSetChanged()
      faceChanged = false
    }
    // frameNum += 1
  }

  fun encodeImage(bm: Bitmap): String? {
    val baos = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
  }

  override fun onFailure(e: Exception) {
    Log.e(TAG, "Face detection failed $e")
  }


  //Load JSON file from Assets folder.
  /*
  fun getAssetJsonData(context: Context, fileName : String): String {
    val json: String
    try {
      val inputStream = context.getAssets().open(fileName)
      val size = inputStream.available()
      val buffer = ByteArray(size)
      inputStream.use { it.read(buffer) }
      json = String(buffer)
    } catch (ioException: IOException) {
      ioException.printStackTrace()
      return ""
    }
    // print the data
    Log.i("data", json)
    return json
  }

   */

  //Load JSON file from Assets folder.
  /*
  fun getJsonData(context: Context, filePath : String): String {
    val json: String
    try {
      val inputStream = FileInputStream(filePath)
      val size = inputStream.available()
      val buffer = ByteArray(size)
      inputStream.use { it.read(buffer) }
      json = String(buffer)
    } catch (ioException: IOException) {
      ioException.printStackTrace()
      return ""
    }
    // print the data
    Log.i("data", json)
    return json
  }
   */

  /*
  private fun login() {
    var login = Login(Constants.user_name, Constants.password)
    var call = userClient.login(login)
    call!!.enqueue(object : Callback<User?>{
      override fun onFailure(call: Call<User?>, t: Throwable) {
        Log.v("Retrofit Res", t.toString())
        Toast.makeText(context, "Error =(((",
                Toast.LENGTH_SHORT).show()
      }

      override fun onResponse(call: Call<User?>, response: Response<User?>) {

        if (response.isSuccessful) {
          token = response.body()!!.getToken()

          Toast.makeText(context, token,
                  Toast.LENGTH_SHORT).show()

          Log.v("Retrofit Res", response.body().toString())
        }
        else {
          Log.v("Retrofit Res", "Fail")
          Toast.makeText(context, "Login not correct =((",
                  Toast.LENGTH_SHORT).show()
        }
      }
    })
  }
   */

  private var token : String = "Token is Null"

  /*
  private fun getSecret() {
    var call = userClient.getSecret(token)

    call!!.enqueue(object : Callback<ResponseBody?>{
      override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
        Log.v("Retrofit Res", "Fail")
        Toast.makeText(context, "Error =(((",
                Toast.LENGTH_SHORT).show()

      }

      override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
        if (response.isSuccessful) {
          try {
            Toast.makeText(context, token,
                    Toast.LENGTH_SHORT).show()
            Log.v("Retrofit Res", token)
          }
          catch (e : java.lang.Exception) {
            Log.v("Retrofit Res", response.toString())
            Toast.makeText(context, "Token not correct =((",
                    Toast.LENGTH_SHORT).show()
          }
        }
      }
    })
  }
   */

  private  fun broadcastArrayWatchlistChanged(pos : Int, name : String, type : String, student_id: String) {
    // Re-assign Name
    // println("WatchList: " + arrayWatchlist)
    arrayWatchlist[arrayWatchlist.size - pos - 1].name = name
    arrayWatchlist[arrayWatchlist.size - pos - 1].type = type



    if (name == "Lê Tuấn Anh") {
      isShowDialog = true

      if (isSpeakerOn == true) {
        try {
          var mediaPlayer = MediaPlayer.create(context, R.raw.long_beep)
          mediaPlayer.start()
        } catch (e: Exception) {
          // do nothing
          Log.e("Media Player Error", "Can not play muzik")
        }
      }

      // showSthg()
      showDialog("ABC")
    } else if (type == "Teacher" && !isShowDialog) {
      totalBlacklist += 1
      var textViewContent = "<font color=#ffffff>Total: $totalWatchlist</font> | <font color=#008000>$totalVIP</font> | <font color=#b32d00>$totalBlacklist</font>"
      txtView!!.setText(Html.fromHtml(textViewContent))

      isShowDialog = true

      if (isSpeakerOn == true) {
        try {
          var mediaPlayer = MediaPlayer.create(context, R.raw.long_beep)
          mediaPlayer.start()
        } catch (e: Exception) {
          // do nothing
          Log.e("Media Player Error", "Can not play muzik")
        }
      }



      // showSthg()
      showDialog("ABC")
    } else if (type == "Student") {
      totalVIP += 1
      var textViewContent = "<font color=#ffffff>Total: $totalWatchlist</font> | <font color=#008000>$totalVIP</font> | <font color=#b32d00>$totalBlacklist</font>"
      txtView!!.setText(Html.fromHtml(textViewContent))

      if (isSpeakerOn == true) {
        try {
          var mediaPlayer = MediaPlayer.create(context, R.raw.entering_sound)
          mediaPlayer.start()
        } catch (e : Exception) {
          // do nothing
          Log.e("Media Player Error", "Can not play muzik")
        }
      }
    }

    if (student_id != "Unknown") {
      arrayWatchlist[arrayWatchlist.size - pos - 1].watchlistID = student_id.toInt()
    }

    // add(0, WatchList(index, croppedImage, face.trackingId.toString(), currentDate))

    adapter?.notifyDataSetChanged()
  }

  // Pop up Notification dialog
  private fun showDialog(title: String) {
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.layout_dialog)
    val warningContext = dialog.findViewById(R.id.notfContext) as TextView
    warningContext.text = "Hey! I found someone that might be in your blacklist. Take a look!"

    val yesBtn = dialog.findViewById(R.id.notfLearnMore) as Button
    val noBtn = dialog.findViewById(R.id.notfDismiss) as Button
    val warningImage = dialog.findViewById(R.id.notfWarning) as ImageView
    warningImage.setImageResource(R.drawable.shiba_inu)


    Handler().postDelayed({
      isShowDialog = false
      dialog.dismiss()
    }, 5000)

    /*yesBtn.setOnClickListener {
      isShowDialog = false
      dialog.dismiss()
    }
    noBtn.setOnClickListener {
      isShowDialog = false
      dialog.dismiss() }
     */

    dialog.show()
  }

  // Request Image
  private fun sendData(imageCode : String, pos: Int){
    // Init data: field value
    val mydata = data(imageCode, "0", "0", "")

    // Init transmit frame
    var faceData = FaceData(Constants.token, mydata)
    var data = imageData.postData(faceData)

    //return values
    var name = "Unknown"

    // broadcastArrayWatchlistChanged(pos, name, "Other", "Unknown")
    try {
      data!!.enqueue(object : Callback<ServerData?>{
        override fun onFailure(call: Call<ServerData?>, t: Throwable) {

          Log.v("Retrofit sendD Fail Res", t.toString())
          Toast.makeText(context, "Error =(((",
                  Toast.LENGTH_SHORT).show()
        }

        override fun onResponse(call: Call<ServerData?>, response: Response<ServerData?>) {
          if (response.isSuccessful) {
            var receiveData : ServerData = response.body()!!

            var serverData = receiveData.getData()
            var serverCode = receiveData.getCode()
            var serverStatus = receiveData.getStatus()

            var display = receiveData.toString()
            // serverResponse = serverData.data.student_id

            when {
                serverCode.toInt() == 1000 -> {

                  Log.v("MMLab response: ", "Code $serverCode\nStatus $serverStatus" +
                          "\nStudent: ${serverData.student_name}")
                  name = serverData.student_name
                  Toast.makeText(context, "Name: " + serverData.student_name,
                          Toast.LENGTH_SHORT).show()

                  // Add name to
                  var type = when{
                    serverData.student_id.length == 5 -> "Teacher"
                    serverData.student_id.length == 8 -> "Student"
                    serverData.student_id.length == 9 -> "Student"
                    else -> "Other"
                  }

                  if (serverData.student_name == "Lê Tuấn Anh" && serverData.student_id !in repeatFaces) repeatFaces.add(serverData.student_id)

                  if (serverData.student_id == "17520237") type = "Teacher" // Teacher will be marked as red


                  // Check if that's person is on blacklist
                  /*
                  var foundMember = false
                  for (i in 0 until watchlist.size) {
                    if (watchlist[i].getId() == serverData.student_id) {
                      foundMember = true
                      break
                    }
                  }
                  if (!foundMember) {
                    watchlist.add(Watchlist(serverData.student_id, serverData.student_name, serverData.image_id, type))
                  }
                  */

                  // Re-assign Name
                  broadcastArrayWatchlistChanged(pos, name, type, serverData.student_id)
                }
                serverCode.toInt() == 704 -> {
                  Log.v("MMLab response: ", "Code $serverCode\nStatus $serverStatus\nStudent: Unknown")
                    Toast.makeText(context, "Name: Unknown",
                            Toast.LENGTH_SHORT).show()

                  // Re-assign Name
                  broadcastArrayWatchlistChanged(pos, name, "Other", "Unknown")

                }
                else -> {
                  Log.v("MMLab response: ", "Code $serverCode\nStatus $serverStatus")
                  name = "Error!"
                  Toast.makeText(context, "Code $serverCode",
                          Toast.LENGTH_SHORT).show()

                  broadcastArrayWatchlistChanged(pos, name, "Other", "Unknown")

                }
            }

            /*
            // Log.v("Watchlist GSON", memberGson.toString())
            if (index == 5) {
              var json = gson.toJson(watchlist)
              Log.v("Watchlist JSON", json)

            }

            // var json = "[{\"id\":\"17520237\",\"imageId\":\"1618428463.6149511\",\"name\":\"Lê Tuấn Anh\",\"type\":\"Student\"},{\"id\":\"17520237\",\"imageId\":\"1618428467.9385846\",\"name\":\"Lê Tuấn Anh\",\"type\":\"Student\"},{\"id\":\"18520833\",\"imageId\":\"1618428471.503557\",\"name\":\"Lê Bảo Huy\",\"type\":\"Student\"},{\"id\":\"80197\",\"imageId\":\"1618428474.5133421\",\"name\":\"Thầy Đỗ Văn Tiến\",\"type\":\"Teacher\"},{\"id\":\"80036\",\"imageId\":\"1618428480.1334958\",\"name\":\"Thầy Lê Đình Duy\",\"type\":\"Teacher\"}]"

            // var memberGson = gson.fromJson(json, Array<Watchlist>::class.java).toList()
            // Log.v("Watchlist GSON", memberGson.toString())

            var fileName = "watchlist.json"
            var fileInString = getAssetJsonData(context, fileName)
            Log.v("Read Json file", fileInString)

            var memberGson = gson.fromJson(fileInString, Array<Watchlist>::class.java).toList()
            Log.v("Watchlist GSON", memberGson.toString())

            val gsonPretty = GsonBuilder().setPrettyPrinting().create()
            var json = gsonPretty.toJson(memberGson)

            context.openFileOutput("out.json", Context.MODE_PRIVATE).use {
              it.write(json.toByteArray())
            }
             */

          }
          else {
            name = "Error!"
            Log.v("Retrofit sendD Res else", "Fail")
            Toast.makeText(context, "Can not Search =((",
                    Toast.LENGTH_SHORT).show()

            // Re-assign Name
            broadcastArrayWatchlistChanged(pos, name, "Other", "Unknown")
          }
        }
      })
    }
    catch (e : Exception)
    {
      name = "Error!"
      Log.v("Retrofit catch", "Fail")
      Toast.makeText(context, "Retrofit Catch Exception",
              Toast.LENGTH_SHORT).show()

      broadcastArrayWatchlistChanged(pos, name,"Other", "Unknown")
    }



  }

  // Load json file from disk
  @Throws(FileNotFoundException::class)
  open fun <T> getFile(fileName: String?, type: Class<T>?): T? {
    val gson: Gson = GsonBuilder()
            .create()
    val json = FileReader(fileName)
    return gson.fromJson(json, type)
  }

  // Create a crop method that takes a bitmap and Rect
  // to focus only on the face
  private fun cropBitmap (bitmap: Bitmap, leftcoord: Int,
                          topcoord: Int, w: Int, h: Int): Bitmap? {
    var croppedBitmap = bitmap

    try {
      croppedBitmap = createBitmap(bitmap!!, leftcoord, topcoord, w, h)

    }
    catch (e : Exception){
      return croppedBitmap
    }

    return croppedBitmap
  }

  companion object {
    private const val TAG = "FaceDetectorProcessor"
    private fun logExtrasForTesting(face: Face?) {
      if (face != null) {
        Log.v(
          MANUAL_TESTING_LOG,
          "face bounding box: " + face.boundingBox.flattenToString()
        )
        Log.v(
          MANUAL_TESTING_LOG,
          "face Euler Angle X: " + face.headEulerAngleX
        )
        Log.v(
          MANUAL_TESTING_LOG,
          "face Euler Angle Y: " + face.headEulerAngleY
        )
        Log.v(
          MANUAL_TESTING_LOG,
          "face Euler Angle Z: " + face.headEulerAngleZ
        )
        // All landmarks
        val landMarkTypes = intArrayOf(
          FaceLandmark.MOUTH_BOTTOM,
          FaceLandmark.MOUTH_RIGHT,
          FaceLandmark.MOUTH_LEFT,
          FaceLandmark.RIGHT_EYE,
          FaceLandmark.LEFT_EYE,
          FaceLandmark.RIGHT_EAR,
          FaceLandmark.LEFT_EAR,
          FaceLandmark.RIGHT_CHEEK,
          FaceLandmark.LEFT_CHEEK,
          FaceLandmark.NOSE_BASE
        )
        val landMarkTypesStrings = arrayOf(
          "MOUTH_BOTTOM",
          "MOUTH_RIGHT",
          "MOUTH_LEFT",
          "RIGHT_EYE",
          "LEFT_EYE",
          "RIGHT_EAR",
          "LEFT_EAR",
          "RIGHT_CHEEK",
          "LEFT_CHEEK",
          "NOSE_BASE"
        )
        for (i in landMarkTypes.indices) {
          val landmark = face.getLandmark(landMarkTypes[i])
          if (landmark == null) {
            Log.v(
              MANUAL_TESTING_LOG,
              "No landmark of type: " + landMarkTypesStrings[i] + " has been detected"
            )
          } else {
            val landmarkPosition = landmark.position
            val landmarkPositionStr =
              String.format(Locale.US, "x: %f , y: %f", landmarkPosition.x, landmarkPosition.y)
            Log.v(
              MANUAL_TESTING_LOG,
              "Position for face landmark: " +
                landMarkTypesStrings[i] +
                " is :" +
                landmarkPositionStr
            )
          }
        }
        Log.v(
          MANUAL_TESTING_LOG,
          "face left eye open probability: " + face.leftEyeOpenProbability
        )
        Log.v(
          MANUAL_TESTING_LOG,
          "face right eye open probability: " + face.rightEyeOpenProbability
        )
        Log.v(
          MANUAL_TESTING_LOG,
          "face smiling probability: " + face.smilingProbability
        )
        Log.v(
          MANUAL_TESTING_LOG,
          "face tracking id: " + face.trackingId
        )
      }
    }
  }
}
