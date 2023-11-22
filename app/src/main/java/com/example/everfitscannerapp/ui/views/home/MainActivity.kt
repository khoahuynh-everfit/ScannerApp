package com.example.everfitscannerapp.ui.views.home

import androidx.activity.viewModels
import com.example.everfitscannerapp.databinding.ActivityMainBinding
import com.example.everfitscannerapp.ui.BaseActivity
import com.example.everfitscannerapp.ui.views.result.Mode
import com.example.everfitscannerapp.ui.views.result.ResultActivity
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel : MainViewModel by viewModels()

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onReady() {
        viewModel.getServices()
    }

    override fun observeReady() {
        viewModel.listService.observe(this) { services ->
            binding.btnSearch.isEnabled = !(services.none { service -> service.isSearchService() })
            binding.btnScan.isEnabled = !(services.none { service -> service.isScanService() })
        }
    }

    override fun eventReady() {
        binding.btnSearch.setOnClickListener {
            binding.edtSearch.text.toString().trim().let{ input ->
                if (input.isNotBlank()) {
                    ResultActivity.startResultScreen(
                        activity = this,
                        services = viewModel.getSearchServices(),
                        mode = Mode.SEARCH,
                        input = input
                    )
                }
            }
        }

        binding.btnScan.setOnClickListener {
            startScan { scannedCode ->
                ResultActivity.startResultScreen(
                    activity = this,
                    services = viewModel.getScanServices(),
                    mode = Mode.SCAN,
                    input = scannedCode
                )
            }
        }
    }

    private fun startScan(onScanCompleted: (String) -> Unit) {
        val optionsBuilder = GmsBarcodeScannerOptions.Builder()
        val gmsBarcodeScanner = GmsBarcodeScanning.getClient(this, optionsBuilder.build())
        gmsBarcodeScanner
            .startScan()
            .addOnSuccessListener { barcode: Barcode ->
                val scannedBarCode = barcode.rawValue?:""
                onScanCompleted.invoke(scannedBarCode)
            }
            .addOnFailureListener { e: Exception ->

            }
            .addOnCanceledListener {

            }
    }
}