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

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.demo.GraphicOverlay
import com.google.mlkit.vision.demo.kotlin.*
import com.google.mlkit.vision.demo.kotlin.api.Constants
import com.google.mlkit.vision.demo.kotlin.api.model.Login
import com.google.mlkit.vision.demo.kotlin.api.model.User
import com.google.mlkit.vision.demo.kotlin.api.service.UserClient
import com.google.mlkit.vision.face.*
import okhttp3.ResponseBody
import java.text.SimpleDateFormat
import java.util.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

var index = 0



/** Face Detector Demo.  */
class FaceDetectorProcessor(context: Context, detectorOptions: FaceDetectorOptions?) :
  VisionProcessorBase<List<Face>>(context) {

  lateinit var imageBitmap: Bitmap
  private val detector: FaceDetector
  private var check = false
  var context = context

  private var TAG = "FaceDetectorProcessor"

  // Using trackingId to determine if someone continue to appear on the screen view
  // If he/she is out, then
  private var currentID : ArrayList<Int> = arrayListOf()

  init {
    val options = detectorOptions
      ?: FaceDetectorOptions.Builder()
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .enableTracking()
        .build()

    detector = FaceDetection.getClient(options)

    Log.v(MANUAL_TESTING_LOG, "Face detector options: $options")
  }

  var builder = Retrofit.Builder()
          .baseUrl(Constants.BASE_URL) // change this IP for testing by your actual machine IP
          .addConverterFactory(GsonConverterFactory.create())

  var retrofit = builder.build()
  var userClient = retrofit.create(UserClient::class.java)

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

  // override fun onSuccess(results: List<Face>, graphicOverlay: GraphicOverlay) {
  override fun onSuccess(originalCameraImage: Bitmap?, results: List<Face>, graphicOverlay: GraphicOverlay) {
    // currentBitmap is our originalCameraImage
    /*
    if (originalCameraImage == null) {
      Log.v("Bitmap Error", "originalCameraImage is Null")
    }
    */
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

    /*
    if (check == false) {
      check = true
      arrayWatchlist.add(WatchList(29, R.drawable.shiba_inu, "Shiba_inu", "March 14, 2021"))
      arrayWatchlist.add(WatchList(29, R.drawable.shiba_inu, "Shiba_inu", "March 14, 2021"))
      arrayWatchlist.add(WatchList(29, R.drawable.shiba_inu, "Shiba_inu", "March 14, 2021"))

      adapter?.notifyDataSetChanged()

    }*/
    var croppedImage : Bitmap? = originalCameraImage


    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    val currentDate = sdf.format(Date())
    var faceChanged: Boolean = false


    for (face in results) {
      // Log.v (MANUAL_TESTING_LOG, "face: " + face.toString())

      graphicOverlay.add(FaceGraphic(graphicOverlay, face))
      logExtrasForTesting(face)

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
        if (w > 50 && h > 50 && h/w >= 0.5 && w/h >= 0.5) {
          croppedImage = cropBitmap(originalCameraImage!!, leftcoord, topcoord, w, h)
        }
      }
      catch (e : Exception) {
        // do nothing
      }


      if (face.trackingId !in currentID) {
        currentID.add(face.trackingId)

        // arrayWatchlist.add(0, WatchList(face.trackingId, croppedImage, "Maus", currentDate))
        arrayWatchlist.add(0, WatchList(index, croppedImage, face.trackingId.toString(), currentDate))
        faceChanged = true
        index += 1
      }

      }
    if (faceChanged) {
      adapter?.notifyDataSetChanged()

      // Volley HTTP REquest
      // Access the RequestQueue through your singleton class.
      // Add the request to the RequestQueue.
      // queue.add(stringRequest)


      login()
      getSecret()
      // fetchData()


    }

  }
  /*
  override fun onSuccess(faces: List<Face>, graphicOverlay: GraphicOverlay) {
    for (face in faces) {
      graphicOverlay.add(FaceGraphic(graphicOverlay, face))
      logExtrasForTesting(face)
    }
  }
   */

  /*
  private fun fetchData() {

    // Pass the token as parameter
    apiClient.getApiService().fetchPosts(token = "Bearer ${sessionManager.fetchAuthToken()}")
            .enqueue(object : Callback<PostsResponse> {
              override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
                // Error fetching posts
              }

              override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
                // Handle function to display posts
              }
            })
  }
}

   */

  /*
  private fun fetchData()
  {
    val api = Retrofit.Builder()
            .baseUrl("http://service.mmlab.uit.edu.vn/checkinService_demo/search_face/post/") // change this IP for testing by your actual machine IP
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NoteClient::class.java)

    GlobalScope.launch (Dispatchers.IO) {
      val response = api.getToken().awaitResponse()

      if (response.isSuccessful) {
        val data: ThuaNguyenJSON = response.body()!!
        Log.d(TAG, data.toString())
      }
    }
  }

   */

  override fun onFailure(e: Exception) {
    Log.e(TAG, "Face detection failed $e")
  }
  /*
  fun addDummyUser() {
    val apiService = RestApiManager()
    val userInfo = UserInfo(
            username = "tester1",
            password = "tester1")

    apiService.addUser(userInfo) {
      if (it?.username != null) {
        // it = newly added user parsed as response
        // it?.id = newly added  user ID
      } else {
        Log.v("Error","Error registering new user")
      }
    }
  }

   */

  private fun login() {
    var login = Login("tester1", "tester1")
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

          Toast.makeText(context, response.body().toString(),
                  Toast.LENGTH_SHORT).show()

          Log.v("Retrofit Res", response.toString())
        }
        else {
          Toast.makeText(context, "Login not correct =((",
                  Toast.LENGTH_SHORT).show()
        }
      }
    })


  }

  private var token : String = "Token is Null"

  private fun getSecret() {
    var call = userClient.getSecret(token)

    call!!.enqueue(object : Callback<ResponseBody?>{
      override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
        Log.v("Retrofit Res", t.toString())
        Toast.makeText(context, "Error =(((",
                Toast.LENGTH_SHORT).show()

      }

      override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
        if (response.isSuccessful) {
          try {
            Toast.makeText(context, response.body().toString(),
                    Toast.LENGTH_SHORT).show()
            Log.v("Retrofit Res", response.toString())
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
