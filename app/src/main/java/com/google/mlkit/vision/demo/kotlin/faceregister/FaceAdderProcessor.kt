package com.google.mlkit.vision.demo.kotlin.faceregister

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.demo.GraphicOverlay
import com.google.mlkit.vision.demo.kotlin.VisionProcessorBase
import com.google.mlkit.vision.demo.kotlin.api.Constants
import com.google.mlkit.vision.demo.kotlin.api.service.ImageData
import com.google.mlkit.vision.demo.kotlin.facedetector.FaceGraphic
import com.google.mlkit.vision.face.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class FaceAdderProcessor (context: Context, detectorOptions: FaceDetectorOptions?) :
        VisionProcessorBase<List<Face>>(context) {

    var frontFace: Bitmap? = null
    var leftFace: Bitmap? = null
    var rightFace: Bitmap? = null

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

    var gson : Gson = Gson()

    init {
        val options = detectorOptions
                ?: FaceDetectorOptions.Builder()
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .enableTracking()
                        .build()

        detector = FaceDetection.getClient(options)

        Log.v(MANUAL_TESTING_LOG, "Face detector options: $options")

        // filePath: "/data/user/0/com.google.mlkit.vision.demo/files/out.json"
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

    }

    var builder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL) // change this IP for testing by your actual machine IP
            .addConverterFactory(GsonConverterFactory.create())

    var retrofit = builder.build()

    var imageData = retrofit.create(ImageData::class.java)

    override fun stop() {
        super.stop()
        detector.close()
    }

    override fun detectInImage(image: InputImage): Task<List<Face>> {
        return detector.process(image)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onSuccess(originalCameraImage: Bitmap?, results: List<Face>, graphicOverlay: GraphicOverlay) {

        var croppedImage : Bitmap? = originalCameraImage


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

            if (face.headEulerAngleY > 36 || face.headEulerAngleY < -36) continue

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
                if (w > 250 && h > 250 && h/w >= 0.7 && w/h >= 0.7) {
                    croppedImage = cropBitmap(originalCameraImage!!, leftcoord, topcoord, w, h)
                }
                else continue
            }
            catch (e : Exception) {
                // do nothing
                continue
            }

            if (frontFace == null
                    && -5 < face.headEulerAngleY && face.headEulerAngleY  < 5
                    && -5 < face.headEulerAngleX && face.headEulerAngleX  < 10) {
                frontFace = croppedImage
                Toast.makeText(context, "Add FrontalFace", Toast.LENGTH_LONG).show()

            } else if (leftFace == null
                    && -40 < face.headEulerAngleY && face.headEulerAngleY  < -7) {
                leftFace = croppedImage
                Toast.makeText(context, "Add LeftFace", Toast.LENGTH_LONG).show()

            } else if (rightFace == null
                    && 7 < face.headEulerAngleY && face.headEulerAngleY  < 40) {
                rightFace = croppedImage
                Toast.makeText(context, "Add RightFace", Toast.LENGTH_LONG).show()
            }
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

            // if record 3 face, then display these
            if (frontFace != null
                    && leftFace != null
                    && rightFace != null) {



                // registerFace(encodedFace)

                val intent = Intent(context, FormRegister::class.java)

                var frontalByteArray = convertBitmap2ByteArray(frontFace!!)
                intent.putExtra("frontal face", frontalByteArray)

                var leftByteArray = convertBitmap2ByteArray(leftFace!!)
                intent.putExtra("left face", leftByteArray)

                var rightByteArray = convertBitmap2ByteArray(rightFace!!)
                intent.putExtra("right face", rightByteArray)

                context.startActivity(intent)

            }
            /*

            if (face.trackingId !in currentID) {
                if (currentID.size > 30)  currentID.drop(29)
                currentID.add(0, face.trackingId)

                faceChanged = true


                var decodedImage = encodeImage(croppedImage!!) // Encode Base64 for bitmap
                // Decode to UTF-8
                var utf = String(decodedImage!!.toByteArray(), Charset.forName("UTF-8"))
                sendData(utf, index)

                index += 1
            }

             */

        }
        // frameNum += 1
    }

    private fun convertBitmap2ByteArray(face : Bitmap): ByteArray {
        var stream = ByteArrayOutputStream()
        face.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }




    override fun onFailure(e: Exception) {
        Log.e(TAG, "Face detection failed $e")
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

    // Request Image
    /*
    private fun registerFace(encodedFace : ImageEncoder){

        // Init data: field value
        val myFaces = RegisterData()

        // Init transmit frame
        var faceData = FaceRegisterData(Constants.token, myFaces)
        // var data = imageData.postData(faceData)

        //return values
        var name = "Unknown"
        var id = "Unknown"

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
                                var type = when(serverData.student_id.length) {
                                    5 -> "Teacher"
                                    8 -> "Student"
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
                            }
                            serverCode.toInt() == 704 -> {
                                Log.v("MMLab response: ", "Code $serverCode\nStatus $serverStatus\nStudent: Unknown")
                                Toast.makeText(context, "Name: Unknown",
                                        Toast.LENGTH_SHORT).show()

                            }
                            else -> {
                                Log.v("MMLab response: ", "Code $serverCode\nStatus $serverStatus")
                                name = "Error!"
                                Toast.makeText(context, "Code $serverCode",
                                        Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                    else {
                        name = "Error!"
                        Log.v("Retrofit sendD Res else", "Fail")
                        Toast.makeText(context, "Can not Search =((",
                                Toast.LENGTH_SHORT).show()

                        // Re-assign Name
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

        }


    }

     */

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
            croppedBitmap = Bitmap.createBitmap(bitmap!!, leftcoord, topcoord, w, h)

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
