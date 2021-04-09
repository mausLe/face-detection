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

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.annotation.KeepName
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.demo.CameraSource
import com.google.mlkit.vision.demo.CameraSourcePreview
import com.google.mlkit.vision.demo.GraphicOverlay
import com.google.mlkit.vision.demo.R
import com.google.mlkit.vision.demo.kotlin.facedetector.FaceDetectorProcessor
import com.google.mlkit.vision.demo.kotlin.objectdetector.ObjectDetectorProcessor
import com.google.mlkit.vision.demo.preference.PreferenceUtils
import com.google.mlkit.vision.demo.preference.SettingsActivity
import com.google.mlkit.vision.demo.preference.SettingsActivity.LaunchSource
import kotlinx.android.synthetic.main.activity_vision_live_preview.*
import java.io.IOException
import java.util.ArrayList

var detectedImage: ImageView? = null
var arrayWatchlist : ArrayList<WatchList> = ArrayList()
var adapter : CustomAdapter? = null

/** Live preview demo for ML Kit APIs.  */
@KeepName
class LivePreviewActivity :
  AppCompatActivity(),
  ActivityCompat.OnRequestPermissionsResultCallback,
  OnItemSelectedListener,
  CompoundButton.OnCheckedChangeListener {

  //val detectedImage: ImageView = findViewById<ImageView>(R.id.detected_face)

  private var cameraSource: CameraSource? = null
  private var preview: CameraSourcePreview? = null
  private var graphicOverlay: GraphicOverlay? = null
  private var selectedModel = OBJECT_DETECTION

  //Listview


  // private var imageID = arrayOf<Int>(R.drawable.shiba_inu)
  // private var name = arrayOf<String>("shiba_inu")
  // private var id = arrayOf<Int>(23)


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.d(TAG, "onCreate")
    setContentView(R.layout.activity_vision_live_preview)

    preview = findViewById(R.id.preview_view)
    if (preview == null) {
      Log.d(TAG, "Preview is null")
    }

    graphicOverlay = findViewById(R.id.graphic_overlay)
    if (graphicOverlay == null) {
      Log.d(TAG, "graphicOverlay is null")
    }

    // ListView
    // var watchlistID: Int, var imageCode: Int,
    // var name: String, var time: Text
    // arrayWatchlist.add(WatchList(23, R.drawable.shiba_inu, "Shiba_inu", "April 7, 2021"))
    // arrayWatchlist.add(WatchList(27, R.drawable.shibaaa, "Shibaaaa", "Feb 14, 2021"))

    adapter = CustomAdapter(this@LivePreviewActivity, arrayWatchlist)
    listView.setAdapter(adapter)

    /*
    val myListAdapter = MyListAdapter(this, imageID, name, id)
    listView.adapter = myListAdapter

    imageID = imageID + R.drawable.shibaaa
    name += "Shiba"
    id += id

     */

    // adapter.

    // var list = findViewById<ListView>(R.id.listView)


    // add Detected Image

    //val detectedImage = findViewById<ImageView>(R.id.detected_face)
    detectedImage = findViewById<ImageView>(R.id.detected_face)
    /*
    Glide.with(this)
      .load(R.drawable.shiba_inu)
      .into(detectedImage)
    */

    val spinner = findViewById<Spinner>(R.id.spinner)
    val options: MutableList<String> = ArrayList()
    options.add(OBJECT_DETECTION)
    options.add(OBJECT_DETECTION_CUSTOM)
    options.add(CUSTOM_AUTOML_OBJECT_DETECTION)
    options.add(FACE_DETECTION)

    // Creating adapter for spinner
    val dataAdapter =
      ArrayAdapter(this, R.layout.spinner_style, options)

    // Drop down layout style - list view with radio button
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    // attaching data adapter to spinner
    spinner.adapter = dataAdapter
    spinner.onItemSelectedListener = this

    val facingSwitch = findViewById<ToggleButton>(R.id.facing_switch)
    facingSwitch.setOnCheckedChangeListener(this)

    val settingsButton = findViewById<ImageView>(R.id.settings_button)
    settingsButton.setOnClickListener {
      val intent = Intent(applicationContext, SettingsActivity::class.java)
      intent.putExtra(SettingsActivity.EXTRA_LAUNCH_SOURCE, LaunchSource.LIVE_PREVIEW)
      startActivity(intent)
    }

    if (allPermissionsGranted()) {
      createCameraSource(selectedModel)
    } else {
      runtimePermissions
    }
  }

  @Synchronized
  override fun onItemSelected(
    parent: AdapterView<*>?,
    view: View?,
    pos: Int,
    id: Long
  ) {
    // An item was selected. You can retrieve the selected item using
    // parent.getItemAtPosition(pos)
    selectedModel = parent?.getItemAtPosition(pos).toString()
    Log.d(TAG, "Selected model: $selectedModel")
    preview?.stop()
    if (allPermissionsGranted()) {
      createCameraSource(selectedModel)
      startCameraSource()
    } else {
      runtimePermissions
    }
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    // Do nothing.
  }

  override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
    Log.d(TAG, "Set facing")
    if (cameraSource != null) {
      if (isChecked) {
        cameraSource?.setFacing(CameraSource.CAMERA_FACING_FRONT)
      } else {
        cameraSource?.setFacing(CameraSource.CAMERA_FACING_BACK)
      }
    }
    preview?.stop()
    startCameraSource()
  }

  private fun createCameraSource(model: String) {
    // If there's no existing cameraSource, create one.
    if (cameraSource == null) {
      cameraSource = CameraSource(this, graphicOverlay)
    }
    try {
      when (model) {
        OBJECT_DETECTION -> {
          Log.i(TAG, "Using Object Detector Processor")
          val objectDetectorOptions =
            PreferenceUtils.getObjectDetectorOptionsForLivePreview(this)
          cameraSource!!.setMachineLearningFrameProcessor(
            ObjectDetectorProcessor(this, objectDetectorOptions)
          )
        }
        FACE_DETECTION -> {
          Log.i(TAG, "Using Face Detector Processor")
          val faceDetectorOptions =
                  PreferenceUtils.getFaceDetectorOptionsForLivePreview(this)
          cameraSource!!.setMachineLearningFrameProcessor(
                  FaceDetectorProcessor(this, faceDetectorOptions)
          )
        }
        OBJECT_DETECTION_CUSTOM -> {
          Log.i(
            TAG,
            "Using Custom Object Detector Processor"
          )
          val localModel = LocalModel.Builder()
            .setAssetFilePath("custom_models/bird_classifier.tflite")
            .build()
          val customObjectDetectorOptions =
            PreferenceUtils.getCustomObjectDetectorOptionsForLivePreview(this, localModel)
          cameraSource!!.setMachineLearningFrameProcessor(
            ObjectDetectorProcessor(this, customObjectDetectorOptions)
          )
        }
        CUSTOM_AUTOML_OBJECT_DETECTION -> {
          Log.i(
            TAG,
            "Using Custom AutoML Object Detector Processor"
          )
          val customAutoMLODTLocalModel = LocalModel.Builder()
            .setAssetManifestFilePath("automl/manifest.json")
            .build()
          val customAutoMLODTOptions = PreferenceUtils
            .getCustomObjectDetectorOptionsForLivePreview(this, customAutoMLODTLocalModel)
          cameraSource!!.setMachineLearningFrameProcessor(
            ObjectDetectorProcessor(this, customAutoMLODTOptions)
          )
        }

        else -> Log.e(TAG, "Unknown model: $model")
      }
    } catch (e: Exception) {
      Log.e(TAG, "Can not create image processor: $model", e)
      Toast.makeText(
        applicationContext, "Can not create image processor: " + e.message,
        Toast.LENGTH_LONG
      ).show()
    }
  }

  /**
   * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
   * (e.g., because onResume was called before the camera source was created), this will be called
   * again when the camera source is created.
   */
  private fun startCameraSource() {
    if (cameraSource != null) {
      try {
        if (preview == null) {
          Log.d(TAG, "resume: Preview is null")
        }
        if (graphicOverlay == null) {
          Log.d(TAG, "resume: graphOverlay is null")
        }
        preview!!.start(cameraSource, graphicOverlay)
      } catch (e: IOException) {
        Log.e(TAG, "Unable to start camera source.", e)
        cameraSource!!.release()
        cameraSource = null
      }
    }
  }

  public override fun onResume() {
    super.onResume()
    Log.d(TAG, "onResume")
    createCameraSource(selectedModel)
    startCameraSource()
  }

  /** Stops the camera.  */
  override fun onPause() {
    super.onPause()
    preview?.stop()
  }

  public override fun onDestroy() {
    super.onDestroy()
    if (cameraSource != null) {
      cameraSource?.release()
    }
  }

  private val requiredPermissions: Array<String?>
    get() = try {
      val info = this.packageManager
        .getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
      val ps = info.requestedPermissions
      if (ps != null && ps.isNotEmpty()) {
        ps
      } else {
        arrayOfNulls(0)
      }
    } catch (e: Exception) {
      arrayOfNulls(0)
    }

  private fun allPermissionsGranted(): Boolean {
    for (permission in requiredPermissions) {
      if (!isPermissionGranted(this, permission)) {
        return false
      }
    }
    return true
  }

  private val runtimePermissions: Unit
    get() {
      val allNeededPermissions: MutableList<String?> = ArrayList()
      for (permission in requiredPermissions) {
        if (!isPermissionGranted(this, permission)) {
          allNeededPermissions.add(permission)
        }
      }
      if (allNeededPermissions.isNotEmpty()) {
        ActivityCompat.requestPermissions(
          this,
          allNeededPermissions.toTypedArray(),
          PERMISSION_REQUESTS
        )
      }
    }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    Log.i(TAG, "Permission granted!")
    if (allPermissionsGranted()) {
      createCameraSource(selectedModel)
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }



  companion object {
    private const val OBJECT_DETECTION = "Object Detection"
    private const val OBJECT_DETECTION_CUSTOM = "Custom Object Detection (Birds)"
    private const val CUSTOM_AUTOML_OBJECT_DETECTION = "Custom AutoML Object Detection (Flower)"
    private const val FACE_DETECTION = "Face Detection"
    private const val TEXT_RECOGNITION = "Text Recognition"
    private const val BARCODE_SCANNING = "Barcode Scanning"
    private const val IMAGE_LABELING = "Image Labeling"
    private const val IMAGE_LABELING_CUSTOM = "Custom Image Labeling (Birds)"
    private const val CUSTOM_AUTOML_LABELING = "Custom AutoML Image Labeling (Flower)"
    private const val POSE_DETECTION = "Pose Detection"
    private const val SELFIE_SEGMENTATION = "Selfie Segmentation"

    private const val TAG = "LivePreviewActivity"
    private const val PERMISSION_REQUESTS = 1
    private fun isPermissionGranted(
      context: Context,
      permission: String?
    ): Boolean {
      if (ContextCompat.checkSelfPermission(context, permission!!)
        == PackageManager.PERMISSION_GRANTED
      ) {
        Log.i(TAG, "Permission granted: $permission")
        return true
      }
      Log.i(TAG, "Permission NOT granted: $permission")
      return false
    }
  }
}
