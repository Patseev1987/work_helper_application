package ru.bogdan.patseev_diploma.presenter.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.databinding.FragmentCameraBinding
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType
import ru.bogdan.patseev_diploma.presenter.states.CameraFragmentState
import ru.bogdan.patseev_diploma.presenter.viewModels.CameraFragmentViewModel
import ru.bogdan.patseev_diploma.presenter.viewModels.ViewModelFactory
import java.lang.RuntimeException
import java.util.concurrent.Executor
import javax.inject.Inject

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private val launcher by lazy {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.values.all { it }) {
                startCamera(binding, executor)
            } else {
                this@CameraFragment.requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var imageCapture: ImageCapture? = null

    private lateinit var executor: Executor

    private lateinit var imageAnalyzer: ImageAnalysis

    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[CameraFragmentViewModel::class.java]
    }

    private val component by lazy {
        (this.activity?.application as MyApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        executor = this.requireContext().mainExecutor
        component.inject(this)
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
        setOnclickListeners(binding,viewModel)
        observeState(binding,viewModel)

    }

    private fun checkPermissions() {
        val isAlLGranted = REQUEST_PERMISSIONS.all { permission ->
            (ContextCompat.checkSelfPermission(this.requireContext(), permission)
                    == PackageManager.PERMISSION_GRANTED)
        }
        if (isAlLGranted) {
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

                val inputImage = InputImage.fromMediaImage(
                    imageProxy.image!!,
                    imageProxy.imageInfo.rotationDegrees
                )
                barcodeScanner.process(inputImage).addOnSuccessListener { barcodes ->
                    onSuccessQRCode(barcodes, viewModel)
                }.addOnCompleteListener {
                    imageProxy.close()
                }
            }
            imageAnalyzer.setAnalyzer(executor, imageAnalysis)

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

    private fun onSuccessQRCode(
        barcodes: List<Barcode>,
        viewModel: CameraFragmentViewModel,
    ) {
        barcodes.firstOrNull()?.let {
            it.rawValue?.let { inputString ->
                    viewModel.getTool(inputString,
                        (requireActivity().application as MyApplication).worker)
            }
        }
    }

    private fun showButtons(binding: FragmentCameraBinding){
            binding.bGiveTool.visibility = View.VISIBLE
            binding.bTakeTool.visibility = View.VISIBLE
    }

    private fun goneButtons(binding: FragmentCameraBinding){
        binding.bGiveTool.visibility = View.GONE
        binding.bTakeTool.visibility = View.GONE
    }

    private fun setOnclickListeners(
        binding: FragmentCameraBinding,
        viewModel: CameraFragmentViewModel
    ){
        binding.bGiveTool.setOnClickListener{
            try {
                val action = CameraFragmentDirections.actionCameraFragmentToTransactionFragment(
                    tool = viewModel.tool,
                    sender = (this@CameraFragment.requireActivity().application as MyApplication).worker
                )
                findNavController().navigate(action)
            }catch (e:RuntimeException){
                Toast.makeText(this@CameraFragment.context,
                    e.message,Toast.LENGTH_SHORT).show()
                goneButtons(binding)
            }
        }
        binding.bTakeTool.setOnClickListener{
            try {
                val action = CameraFragmentDirections.actionCameraFragmentToTransactionFragment(
                    tool = viewModel.tool,
                    receiver = (this@CameraFragment.requireActivity().application as MyApplication).worker
                )
                findNavController().navigate(action)
            }catch (e:RuntimeException){
                Toast.makeText(this@CameraFragment.context,
                    e.message,Toast.LENGTH_SHORT).show()
                goneButtons(binding)
            }
        }
    }


    private fun observeState(binding: FragmentCameraBinding,viewModel: CameraFragmentViewModel){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.state.collectLatest { state ->
                    when (state){
                        is CameraFragmentState.Waiting -> {
                            goneButtons(binding)
                        }

                        is CameraFragmentState.Error -> {
                            Toast.makeText(this@CameraFragment.context,
                                state.msg,Toast.LENGTH_SHORT).show()
                            goneButtons(binding)
                            binding.twInformation.text = ""
                        }

                        is CameraFragmentState.Result -> {
                            if (state.isShowButtons){
                                showButtons(binding)
                            }
                            binding.twInformation.text = String.format(
                                getString(R.string.search_string_template),
                                state.tool.code,
                                state.tool.name,
                                state.tool.place.shelf,
                                state.tool.place.column,
                                state.tool.place.row
                            )
                        }
                    }
                }
            }
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