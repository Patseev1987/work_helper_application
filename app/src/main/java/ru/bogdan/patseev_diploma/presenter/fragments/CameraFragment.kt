package ru.bogdan.patseev_diploma.presenter.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat

import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import ru.bogdan.patseev_diploma.databinding.FragmentCameraBinding
import java.util.concurrent.Executor

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private val launcher by lazy {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.values.all { it }) {
                startCamera(binding, executor)
            } else {
                Toast.makeText(
                    this.requireContext(),
                    "Permissions is not granted",
                    Toast.LENGTH_SHORT
                ).show()
                this@CameraFragment.requireActivity().onBackPressed()
            }
        }
    }

    private var imageCapture: ImageCapture? = null

    private lateinit var executor: Executor

    private lateinit var imageAnalyzer: ImageAnalysis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        executor = this.requireContext().mainExecutor
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        checkPermissions()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnTouchListener(binding)
    }

    private fun checkPermissions() {
        val isAlLGranted = REQUEST_PERMISSIONS.all { permission ->
            (ContextCompat.checkSelfPermission(this.requireContext(), permission)
                    == PackageManager.PERMISSION_GRANTED)
        }
        if (isAlLGranted) {
            Toast.makeText(
                this.requireContext(),
                "Permission is granted",
                Toast.LENGTH_SHORT
            ).show()
            startCamera(binding, executor)
        } else {
            launcher.launch(REQUEST_PERMISSIONS)
        }
    }

    @OptIn(ExperimentalGetImage::class)
    private fun startCamera(binding: FragmentCameraBinding, executor: Executor) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.cameraViewFinder.surfaceProvider)
            imageCapture = ImageCapture.Builder().build()

            val barcodeScanner = BarcodeScanning.getClient(
                BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                    .build()
            )

            imageAnalyzer = ImageAnalysis.Builder()
                .setTargetRotation(binding.cameraViewFinder.display.rotation)
                .build()

            var imageAnalysis = ImageAnalysis.Analyzer { imageProxy ->
                val image = imageProxy.image ?: return@Analyzer
                val inputImage = InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
                barcodeScanner.process(inputImage).addOnSuccessListener { barcodes ->
                    barcodes.firstOrNull()?.let {
                        binding.twInformation.text = it.rawValue
                    }
                }.addOnCompleteListener {
                    imageProxy.close()
                }
            }
            imageAnalyzer.setAnalyzer(executor,imageAnalysis)

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture,
                imageAnalyzer
            )
        }, executor)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchListener(binding: FragmentCameraBinding){
        binding.cardView.setOnTouchListener { v, event ->
            val action = event.action

            when(action){
                MotionEvent.ACTION_MOVE -> {
                        v.x += event.x -(v.width/2)
                        v.y += event.y -(v.height/2)
                }
                MotionEvent.ACTION_UP -> {
                    checkConditionsPosition(v,binding)
                }
                else -> {
                }
            }
            true
        }
    }

private fun checkConditionsPosition(view: View,binding: FragmentCameraBinding){
    if (view.x < 0 ){
        view.x = 0f;
    }
    if (view.y < 0){
        view.y = 0f;
    }
    if( view.x > binding.cameraViewFinder.width-view.width){
        view.x = (binding.cameraViewFinder.width-view.width).toFloat()
    }
    if( view.y > binding.cameraViewFinder.height-view.height){
        view.y = (binding.cameraViewFinder.height-view.height).toFloat()
    }
}


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        private val REQUEST_PERMISSIONS: Array<String> = buildList {
            add(Manifest.permission.CAMERA)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }

}