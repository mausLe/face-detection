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
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.annotation.KeepName
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.mlkit.vision.demo.CameraSource
import com.google.mlkit.vision.demo.CameraSourcePreview
import com.google.mlkit.vision.demo.GraphicOverlay
import com.google.mlkit.vision.demo.R
import com.google.mlkit.vision.demo.kotlin.faceregister.FaceAdderProcessor
import com.google.mlkit.vision.demo.kotlin.iojson.Watchlist
import com.google.mlkit.vision.demo.preference.PreferenceUtils
import com.google.mlkit.vision.demo.preference.SettingsActivity
import com.google.mlkit.vision.demo.preference.SettingsActivity.LaunchSource
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.ArrayList

var watchlist = ArrayList<Watchlist>()

/** Live preview demo for ML Kit APIs.  */
@KeepName
class AddFaceActivity:
        AppCompatActivity(),
        ActivityCompat.OnRequestPermissionsResultCallback,
        CompoundButton.OnCheckedChangeListener {

    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var selectedModel = FACE_DETECTION

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

        detectedImage = findViewById<ImageView>(R.id.detected_face)


        val facingSwitch = findViewById<ToggleButton>(R.id.facing_switch)
        facingSwitch.setOnCheckedChangeListener(this)

        // loadfile
        // gson
        var gson : Gson = Gson()

        val file = File(this.filesDir, "watchlist.json")
        var json = ""
        try {
            json = getJsonData(this, file.toString())
            if (json == "[]") {
                json = getAssetJsonData(this, "watchlist.json")
            }
        } catch (e : Exception) {
            json = getAssetJsonData(this, "watchlist.json")
        }
        watchlist = gson.fromJson(json, Array<Watchlist>::class.java).toList() as ArrayList<Watchlist>


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
                FACE_DETECTION -> {
                    Log.i(TAG, "Using Face Detector Processor")
                    val faceDetectorOptions =
                            PreferenceUtils.getFaceDetectorOptionsForLivePreview(this)
                    cameraSource!!.setMachineLearningFrameProcessor(
                            FaceAdderProcessor(this, faceDetectorOptions)
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


    //Load JSON file from Assets folder.
    private fun getAssetJsonData(context: Context, fileName : String): String {
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

    //Load JSON file from Assets folder.
    private fun getJsonData(context: Context, filePath : String): String {
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

    /** Stops the camera.  */
    override fun onPause() {
        super.onPause()
        preview?.stop()
    }

    public override fun onDestroy() {
        try {
            // Write the watchlist out
            // filePath: "/data/user/0/com.google.mlkit.vision.demo/files/out.json"

            val gsonPretty = GsonBuilder().setPrettyPrinting().create()
            var json = gsonPretty.toJson(watchlist)
            this.openFileOutput("watchlist.json", Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }

            Log.v("Watchlist", json)
            /*
            Toast.makeText(this, "Wlist" +
                    watchlist[watchlist.size - 1].getName(), Toast.LENGTH_SHORT).show()
             */
        } catch (e : Exception)
        {
            // do nothing
        }

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
