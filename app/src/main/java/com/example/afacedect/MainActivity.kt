package com.example.afacedect

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
//import androidx.appcompat.app.AlertDialog
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.afacedect.Helper.ReactOverlay
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.wonderkiln.camerakit.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var waitingDialog: AlertDialog
    override fun onResume() {
        super.onResume()
        // your code here
        camera_view.start()
    }

    override fun onPause() {
        super.onPause()
        camera_view.stop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        waitingDialog= SpotsDialog.Builder().setContext(this)
            .setMessage("please wait ..")
            .setCancelable(false)
            .build()



        btn_detect.setOnClickListener {
            camera_view.start()
            camera_view.captureImage()
            graphic_overlay.clear()
        }
camera_view.addCameraKitListener(object :CameraKitEventListener{
    override fun onVideo(p0: CameraKitVideo?) {

    }

    override fun onEvent(p0: CameraKitEvent?) {

    }

    override fun onImage(p0: CameraKitImage?) {
       waitingDialog.show()
        var bitmap=p0!!.bitmap
        bitmap= Bitmap.createScaledBitmap(bitmap,camera_view.width,camera_view.height,false)
        camera_view.stop()
        runFaceDetector(bitmap)
    }

    override fun onError(p0: CameraKitError?) {

    }


})

    }

    private fun runFaceDetector(bitmap: Bitmap?){
        val image=FirebaseVisionImage.fromBitmap(bitmap!!)
        val options=FirebaseVisionFaceDetectorOptions.Builder().build()
        val detector=FirebaseVision.getInstance().getVisionFaceDetector(options)
        detector.detectInImage(image)
            .addOnSuccessListener { result ->processFaceResult(result) }
            .addOnFailureListener{e-> Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()}
    }


    private fun processFaceResult(result: List<FirebaseVisionFace>) {
var count=0;
        for (face:FirebaseVisionFace in result){

            val bound=face.boundingBox
            val rectOverlay= ReactOverlay(graphic_overlay,bound)
            graphic_overlay.add(rectOverlay)
            count++
        }
        waitingDialog.dismiss()
        Toast.makeText(this,String.format("Detected %d faces in picture",count),Toast.LENGTH_SHORT).show()

    }

}