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
import android.graphics.Rect
import android.util.Log
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.demo.GraphicOverlay
import com.google.mlkit.vision.demo.R
import com.google.mlkit.vision.demo.kotlin.VisionProcessorBase
import com.google.mlkit.vision.demo.kotlin.detectedImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import java.util.Locale


/** Face Detector Demo.  */
class FaceDetectorProcessor(context: Context, detectorOptions: FaceDetectorOptions?) :
  VisionProcessorBase<List<Face>>(context) {

  lateinit var imageBitmap: Bitmap
  private val detector: FaceDetector
  private var check = false

  init {
    val options = detectorOptions
      ?: FaceDetectorOptions.Builder()
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .enableTracking()
        .build()

    detector = FaceDetection.getClient(options)

    Log.v(MANUAL_TESTING_LOG, "Face detector options: $options")
  }

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

    var croppedImage : Bitmap? = originalCameraImage
    for (face in results) {
      // Log.v (MANUAL_TESTING_LOG, "face: " + face.toString())

      graphicOverlay.add(FaceGraphic(graphicOverlay, face))
      logExtrasForTesting(face)

      croppedImage = cropBitmap(originalCameraImage, face.boundingBox)
      Log.v("\n\nCropped Image", "croppedImage: " + croppedImage?.toString())
      }
      Glide.with(detectedImage)
              .asBitmap()
              .load(croppedImage)
              .into(detectedImage)
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
  private fun cropBitmap (bitmap: Bitmap?, rect: Rect): Bitmap? {
    val w = rect.right - rect.left
    val h = rect.bottom - rect.top
    // val w = 100
    // val h = 100
    /*
    var canvas = Canvas(ret)
    canvas.drawBitmap(bitmap, -rect.left, -rect.top, null)
    */
    return Bitmap.createBitmap(bitmap!!, rect.left, rect.top, w, h)

    // return Bitmap.createBitmap(w, h, bitmap?.config!!)
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
