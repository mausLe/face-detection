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
import com.google.mlkit.vision.demo.CameraSource
import com.google.mlkit.vision.demo.CameraSourcePreview
import com.google.mlkit.vision.demo.GraphicOverlay
import com.google.mlkit.vision.demo.R
import com.google.mlkit.vision.demo.kotlin.facedetector.repeatFaces
// import com.google.mlkit.vision.demo.kotlin.facedetector.watchlist
import com.google.mlkit.vision.demo.kotlin.objectdetector.RTADObjectDetectorProcessor
import com.google.mlkit.vision.demo.preference.PreferenceUtils
import com.google.mlkit.vision.demo.preference.SettingsActivity
import com.google.mlkit.vision.demo.preference.SettingsActivity.LaunchSource
import kotlinx.android.synthetic.main.activity_vision_live_preview.*
import java.io.IOException
import java.util.ArrayList

var rtadDetectedImage: ImageView? = null
var rtadArrayWatchlist : ArrayList<WatchList> = ArrayList()
var rtadAdapter : CustomAdapter? = null
// var borderLayout : LinearLayout? = null
var totalAD = 0

@KeepName
class RTADActivity :
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
    setContentView(R.layout.realtime_anomaly_detection_preview)

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

    rtadAdapter = CustomAdapter(this@RTADActivity, rtadArrayWatchlist)
    listView.setAdapter(rtadAdapter)

    // Change LinearLayout border
    // borderLayout = findViewById<LinearLayout>(R.id.borderLayout)

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
    rtadDetectedImage = findViewById<ImageView>(R.id.detected_face)
    /*
    Glide.with(this)
      .load(R.drawable.shiba_inu)
      .into(detectedImage)
    */

    val options: MutableList<String> = ArrayList()
    options.add(OBJECT_DETECTION)

    txtView = findViewById<TextView>(R.id.txtTotal)
    txtView!!.setText("Total: 0" )

    val speakerButton = findViewById<ImageView>(R.id.speaker_button)
    speakerButton.setOnClickListener {
      if (isSpeakerOn == true) {
        isSpeakerOn = false
        speakerButton.setImageResource(R.drawable.speaker_off)

        Log.v("Speaker", "Turn speaker OFF")
        Toast.makeText( applicationContext, "Turn speaker OFF",
                Toast.LENGTH_SHORT ).show()
      } else {
        isSpeakerOn = true
        speakerButton.setImageResource(R.drawable.speaker_on)

        Log.v("Speaker", "Turn speaker ON")
        Toast.makeText( applicationContext, "Turn speaker ON",
                Toast.LENGTH_SHORT ).show()
      }
    }


    val facingSwitch = findViewById<ToggleButton>(R.id.facing_switch)
    facingSwitch.setOnCheckedChangeListener(this)

    val settingsButton = findViewById<ImageView>(R.id.settings_button)
    settingsButton.setOnClickListener {
      val intent = Intent(applicationContext, SettingsActivity::class.java)
      intent.putExtra(SettingsActivity.EXTRA_LAUNCH_SOURCE, LaunchSource.RTAD_PREVIEW)
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
                  RTADObjectDetectorProcessor(this, objectDetectorOptions)
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
    /*
    try {
      // Write the watchlist out
      // filePath: "/data/user/0/com.google.mlkit.vision.demo/files/out.json"

      val gsonPretty = GsonBuilder().setPrettyPrinting().create()
      var json = gsonPretty.toJson(watchlist)
      this.openFileOutput("watchlist.json", Context.MODE_PRIVATE).use {
        it.write(json.toByteArray())
      }

      Log.v("Repeat Faces", repeatFaces.toString())
      /*
      Toast.makeText(this, "Wlist" +
              watchlist[watchlist.size - 1].getName(), Toast.LENGTH_SHORT).show()
       */
    } catch (e : Exception)
    {
      // do nothing
    }
     */

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
