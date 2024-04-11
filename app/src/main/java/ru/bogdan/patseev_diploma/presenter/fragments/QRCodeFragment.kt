package ru.bogdan.patseev_diploma.presenter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.google.mlkit.vision.common.InputImage
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.databinding.FragmentCameraBinding
import ru.bogdan.patseev_diploma.databinding.FragmentQRCodeBinding
import java.util.concurrent.Executor

class QRCodeFragment : Fragment() {

    private var _binding:FragmentQRCodeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentQRCodeBinding.inflate(inflater,container,false)
//        getScanner().startScan().addOnSuccessListener {
//
//       }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.fullscreenContent.setOnClickListener {

            val options = GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_QR_CODE,
                    Barcode.FORMAT_AZTEC
                )
                .build();

            //GmsBarcodeScanner scanner = GmsBarcodeScanning.getClient(this, options);
            val scanner = GmsBarcodeScanning.getClient(this.requireContext());
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    println("hello")
                }
        }
    }


    private fun getScanner(): GmsBarcodeScanner {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
       return GmsBarcodeScanning.getClient(this.requireContext(),options)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}