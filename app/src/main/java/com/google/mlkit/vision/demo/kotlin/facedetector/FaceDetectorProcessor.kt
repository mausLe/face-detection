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
import android.graphics.Bitmap.*
import android.graphics.Rect
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.demo.GraphicOverlay
import com.google.mlkit.vision.demo.kotlin.*
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

var index = 0

/** Face Detector Demo.  */
class FaceDetectorProcessor(context: Context, detectorOptions: FaceDetectorOptions?) :
  VisionProcessorBase<List<Face>>(context) {

  lateinit var imageBitmap: Bitmap
  private val detector: FaceDetector
  private var check = false
  var context = context

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

  // Volley HTTP Request
  // Instantiate the RequestQueue.
  val queue = Volley.newRequestQueue(context)
  val url = "https://www.google.com"
  var res = "The Response"

  // Request a string response from the provided URL.
  val stringRequest = StringRequest(Request.Method.GET, url,
          Response.Listener<String> { response ->
            // Display the first 500 characters of the response string.
            res =  "Response is: ${response.substring(0, 500)}"
            Log.v("HTTP Response", res)
          },
          Response.ErrorListener { error ->
            res = "That didn't work!"
            Log.v("HTTP Response", res) })

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
      queue.add(stringRequest)

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
  override fun onFailure(e: Exception) {
    Log.e(TAG, "Face detection failed $e")
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
