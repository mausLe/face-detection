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

package com.google.mlkit.vision.demo.kotlin

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build.VERSION_CODES
import android.os.SystemClock
import android.util.Base64
import androidx.annotation.GuardedBy
import androidx.annotation.RequiresApi
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import api.ServerData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.demo.BitmapUtils
import com.google.mlkit.vision.demo.CameraImageGraphic
import com.google.mlkit.vision.demo.FrameMetadata
import com.google.mlkit.vision.demo.GraphicOverlay
import com.google.mlkit.vision.demo.InferenceInfoGraphic
import com.google.mlkit.vision.demo.ScopedExecutor
import com.google.mlkit.vision.demo.VisionImageProcessor
import com.google.mlkit.vision.demo.kotlin.api.Constants
import com.google.mlkit.vision.demo.kotlin.api.service.FrameData
import com.google.mlkit.vision.demo.kotlin.api.service.ImageData
import com.google.mlkit.vision.demo.kotlin.api.service.RTADClientData
import com.google.mlkit.vision.demo.kotlin.api.service.RTADServerData
import com.google.mlkit.vision.demo.preference.PreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.util.Timer
import java.util.TimerTask

/**
 * Abstract base class for ML Kit frame processors. Subclasses need to implement {@link
 * #onSuccess(T, FrameMetadata, GraphicOverlay)} to define what they want to with the detection
 * results and {@link #detectInImage(VisionImage)} to specify the detector object.
 *
 * @param <T> The type of the detected feature.
 */
abstract class StreamRTADBase<T>(context: Context) : VisionImageProcessor {

  companion object {
    const val MANUAL_TESTING_LOG = "LogTagForTest"
    private const val TAG = "VisionProcessorBase"
  }

  private var activityManager: ActivityManager =
    context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
  private val fpsTimer = Timer()
  private val executor = ScopedExecutor(TaskExecutors.MAIN_THREAD)

  // Whether this processor is already shut down
  private var isShutdown = false

  // Used to calculate latency, running in the same thread, no sync needed.
  private var numRuns = 0
  private var totalFrameMs = 0L
  private var maxFrameMs = 0L
  private var minFrameMs = Long.MAX_VALUE
  private var totalDetectorMs = 0L
  private var maxDetectorMs = 0L
  private var minDetectorMs = Long.MAX_VALUE

  // Frame count that have been processed so far in an one second interval to calculate FPS.
  private var frameProcessedInOneSecondInterval = 0
  private var framesPerSecond = 0

  // To keep the latest images and its metadata.
  @GuardedBy("this")
  private var latestImage: ByteBuffer? = null
  @GuardedBy("this")
  private var latestImageMetaData: FrameMetadata? = null
  // To keep the images and metadata in process.
  @GuardedBy("this")
  private var processingImage: ByteBuffer? = null
  @GuardedBy("this")
  private var processingMetaData: FrameMetadata? = null

  lateinit var bytebuffer2Bitmap : Bitmap

  lateinit var frameData: FrameData

  init {
    fpsTimer.scheduleAtFixedRate(
      object : TimerTask() {
        override fun run() {
          framesPerSecond = frameProcessedInOneSecondInterval
          frameProcessedInOneSecondInterval = 0
        }
      },
      0,
      1000
    )


    var builder = Retrofit.Builder()
            .baseUrl(Constants.RTAD_URL) // change this IP for testing by your actual machine IP
            .addConverterFactory(GsonConverterFactory.create())

    var retrofit = builder.build()
    // var userClient = retrofit.create(UserClient::class.java)

    frameData = retrofit.create(FrameData::class.java)
  }

  // -----------------Code for processing single still image----------------------------------------
  override fun processBitmap(bitmap: Bitmap?, graphicOverlay: GraphicOverlay) {
    val frameStartMs = SystemClock.elapsedRealtime()

    // 1st parameter
    /*
    requestDetectInImage(
      InputImage.fromBitmap(bitmap!!, 0),
      graphicOverlay, /* originalCameraImage= */
      bitmap, /* shouldShowFps= */
      false,
      frameStartMs
    )

     */
  }

  // -----------------Code for processing live preview frame from Camera1 API-----------------------

  fun encodeImage(bm: Bitmap): String? {
    val baos = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
  }



  private var fr = 0
  private var detectorStartMs = SystemClock.elapsedRealtime()

  @Synchronized
  override fun processByteBuffer(
    data: ByteBuffer?,
    frameMetadata: FrameMetadata?,
    graphicOverlay: GraphicOverlay
  ) {

    val frameStartMs = SystemClock.elapsedRealtime()
    val bitmap = BitmapUtils.getBitmap(data, frameMetadata)

    var decodedImage = encodeImage(bitmap!!) // Encode Base64 for bitmap
    // Decode to UTF-8
    var utfImage = String(decodedImage!!.toByteArray(), Charset.forName("UTF-8"))


    var rtadClientData = RTADClientData("mausLe", utfImage)

    var rtadServerData = frameData.postData(rtadClientData)

    try {
        rtadServerData!!.enqueue(object : Callback<RTADServerData?> {
          override fun onFailure(call: Call<RTADServerData?>, t: Throwable) {
            Log.v("Retrofit sendD Fail Res", t.toString())
            Toast.makeText(graphicOverlay.context, "Error =(((",
                    Toast.LENGTH_SHORT).show()
          }

          override fun onResponse(call: Call<RTADServerData?>, response: Response<RTADServerData?>) {
            if (response.isSuccessful) {
              var receiveData: RTADServerData = response.body()!!

              var serverCode = receiveData.getCode()
              var serverStatus = receiveData.getStatus()

              // var display = receiveData.toString()
              // serverResponse = serverData.data.student_id
              Toast.makeText(graphicOverlay.context, "Status: $serverStatus",
                      Toast.LENGTH_SHORT).show()

            }
          }

        }
        )
    }
    catch (e : Exception)
    {
      Log.v("Retrofit catch", "Fail")

    }


    fr += 1
    if (fr % 30 == 0) {
      Log.v("Frame", "\n\n\n" + fr.toString())
    }

    val endMs = SystemClock.elapsedRealtime()
    val currentFrameLatencyMs = endMs - frameStartMs
    val currentDetectorLatencyMs = endMs - detectorStartMs
    if (numRuns >= 500) {
      resetLatencyStats()
    }
    numRuns++
    frameProcessedInOneSecondInterval++
    totalFrameMs += currentFrameLatencyMs
    maxFrameMs = Math.max(currentFrameLatencyMs, maxFrameMs)
    minFrameMs = Math.min(currentFrameLatencyMs, minFrameMs)
    totalDetectorMs += currentDetectorLatencyMs
    maxDetectorMs = Math.max(currentDetectorLatencyMs, maxDetectorMs)
    minDetectorMs = Math.min(currentDetectorLatencyMs, minDetectorMs)

    // Only log inference info once per second. When frameProcessedInOneSecondInterval is
    // equal to 1, it means this is the first frame processed during the current second.
    if (frameProcessedInOneSecondInterval == 1) {
      Log.d(TAG, "Num of Runs: $numRuns")
      Log.d(
              TAG,
              "Frame latency: max=$maxFrameMs, min=$minFrameMs, avg=" +
                      (totalFrameMs / numRuns)
      )
      Log.d(
              TAG,
              "Detector latency: max=$maxDetectorMs, min=$minDetectorMs, avg=" +
                      (totalDetectorMs / numRuns)
      )
      val mi = ActivityManager.MemoryInfo()
      activityManager.getMemoryInfo(mi)
      val availableMegs = mi.availMem / 0x100000L
      Log.d(TAG, "Memory available in system: $availableMegs MB")
    }
    graphicOverlay.clear()
    if (bitmap != null) {
      graphicOverlay.add(
              CameraImageGraphic(
                      graphicOverlay,
                      bitmap
              )
      )
    }
    graphicOverlay.add(
            InferenceInfoGraphic(
                    graphicOverlay,
                    currentFrameLatencyMs,
                    currentDetectorLatencyMs,
                    framesPerSecond
            )
    )
    // this@StreamRTADBase.onSuccess(bitmap, graphicOverlay)

  }

  // -----------------Code for processing live preview frame from CameraX API-----------------------
  @RequiresApi(VERSION_CODES.KITKAT)
  @ExperimentalGetImage
  override fun processImageProxy(image: ImageProxy, graphicOverlay: GraphicOverlay) {
    val frameStartMs = SystemClock.elapsedRealtime()
    if (isShutdown) {
      return
    }
    var bitmap: Bitmap? = null
    if (!PreferenceUtils.isCameraLiveViewportEnabled(graphicOverlay.context)) {
      bitmap = BitmapUtils.getBitmap(image)
    }

    /*
    requestDetectInImage(
      InputImage.fromMediaImage(image.image!!, image.imageInfo.rotationDegrees),
      graphicOverlay, /* originalCameraImage= */
      bitmap, /* shouldShowFps= */
      true,
      frameStartMs
    )
      // When the image is from CameraX analysis use case, must call image.close() on received
      // images when finished using them. Otherwise, new images may not be received or the camera
      // may stall.
      .addOnCompleteListener { image.close() }
     */
  }

  // -----------------Common processing logic-------------------------------------------------------
  /*
  private fun requestDetectInImage(
    image: InputImage,
    graphicOverlay: GraphicOverlay,
    originalCameraImage: Bitmap?,
    shouldShowFps: Boolean,
    frameStartMs: Long
  ): Task<T> {

    val detectorStartMs = SystemClock.elapsedRealtime()
    return detectInImage(image).addOnSuccessListener(executor) { results: T ->
      val endMs = SystemClock.elapsedRealtime()
      val currentFrameLatencyMs = endMs - frameStartMs
      val currentDetectorLatencyMs = endMs - detectorStartMs
      if (numRuns >= 500) {
        resetLatencyStats()
      }
      numRuns++
      frameProcessedInOneSecondInterval++
      totalFrameMs += currentFrameLatencyMs
      maxFrameMs = Math.max(currentFrameLatencyMs, maxFrameMs)
      minFrameMs = Math.min(currentFrameLatencyMs, minFrameMs)
      totalDetectorMs += currentDetectorLatencyMs
      maxDetectorMs = Math.max(currentDetectorLatencyMs, maxDetectorMs)
      minDetectorMs = Math.min(currentDetectorLatencyMs, minDetectorMs)

      // Only log inference info once per second. When frameProcessedInOneSecondInterval is
      // equal to 1, it means this is the first frame processed during the current second.
      if (frameProcessedInOneSecondInterval == 1) {
        Log.d(TAG, "Num of Runs: $numRuns")
        Log.d(
          TAG,
          "Frame latency: max=$maxFrameMs, min=$minFrameMs, avg=" +
            (totalFrameMs / numRuns)
        )
        Log.d(
          TAG,
          "Detector latency: max=$maxDetectorMs, min=$minDetectorMs, avg=" +
            (totalDetectorMs / numRuns)
        )
        val mi = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(mi)
        val availableMegs = mi.availMem / 0x100000L
        Log.d(TAG, "Memory available in system: $availableMegs MB")
      }

      graphicOverlay.clear()
      if (originalCameraImage != null) {
        graphicOverlay.add(
          CameraImageGraphic(
            graphicOverlay,
            originalCameraImage
          )
        )
      }
      graphicOverlay.add(
        InferenceInfoGraphic(
          graphicOverlay,
          currentFrameLatencyMs,
          currentDetectorLatencyMs,
          if (shouldShowFps) framesPerSecond else null
        )
      )
      // image is InputImage
      // this@VisionProcessorBase.onSuccess(image, results, graphicOverlay)

      // originalCameraImage is Bitmap
      // this@VisionProcessorBase.onSuccess(results, graphicOverlay)
      this@StreamRTADBase.onSuccess(originalCameraImage, results, graphicOverlay)
      graphicOverlay.postInvalidate()
    }
      .addOnFailureListener(executor) { e: Exception ->
        graphicOverlay.clear()
        graphicOverlay.postInvalidate()
        Toast.makeText(
          graphicOverlay.context,
          "Failed to process.\nError: " +
            e.localizedMessage +
            "\nCause: " +
            e.cause,
          Toast.LENGTH_LONG
        )
          .show()
        e.printStackTrace()
        this@StreamRTADBase.onFailure(e)
      }
  }
   */

  override fun stop() {
    executor.shutdown()
    isShutdown = true
    resetLatencyStats()
    fpsTimer.cancel()
  }

  private fun resetLatencyStats() {
    numRuns = 0
    totalFrameMs = 0
    maxFrameMs = 0
    minFrameMs = Long.MAX_VALUE
    totalDetectorMs = 0
    maxDetectorMs = 0
    minDetectorMs = Long.MAX_VALUE
  }

  protected abstract fun detectInImage(image: InputImage): Task<T>

  protected abstract fun onSuccess(originalCameraImage: Bitmap?, graphicOverlay: GraphicOverlay)
  // protected abstract fun onSuccess(results: T, graphicOverlay: GraphicOverlay)

  protected abstract fun onFailure(e: Exception)
}
