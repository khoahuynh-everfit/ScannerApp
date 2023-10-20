package com.example.everfitscannerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.everfitscannerapp.ui.theme.EverfitScannerAppTheme
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EverfitScannerAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScanView()
                }
            }
        }
        viewModel.getServices()
    }

    @Composable
    fun ScanView(
        viewModel: MainViewModel = hiltViewModel()
    ) {
        val selectedValue = remember { mutableStateOf("") }
        val isSelectedItem: (String) -> Boolean = { selectedValue.value == it }
        val onChangeState: (String) -> Unit = {
            selectedValue.value = it
            viewModel.selectService(it)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                viewModel.scanServices.forEach { service ->
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.selectable(
                            selected = isSelectedItem(service.id),
                            onClick = { onChangeState(service.id) },
                            role = Role.RadioButton
                        ), horizontalArrangement = Arrangement.Center) {
                        RadioButton(
                            selected = isSelectedItem(service.id), onClick = null)
                        Text(modifier = Modifier.fillMaxWidth(), text = service.name)
                    }
                }

                Button(
                    onClick = {
                        startScan()
                    }, enabled = selectedValue.value.isNotEmpty()
                ) {
                    Text(text = if(viewModel.callingAPI) "Scaning..." else "Scan")
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    text = viewModel.scannedInfoJson
                )
            }
        }
    }

    private fun startScan() {
        val optionsBuilder = GmsBarcodeScannerOptions.Builder()
        val gmsBarcodeScanner = GmsBarcodeScanning.getClient(this, optionsBuilder.build())
        gmsBarcodeScanner
            .startScan()
            .addOnSuccessListener { barcode: Barcode ->
                viewModel.getDataInfoJson(barcode)
            }
            .addOnFailureListener { e: Exception ->
                viewModel.scanFail(e)
            }
            .addOnCanceledListener {
                viewModel.scanCancel()
            }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        EverfitScannerAppTheme {
            ScanView()
        }
    }
}