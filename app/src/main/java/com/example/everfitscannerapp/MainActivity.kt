package com.example.everfitscannerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.everfitscannerapp.ui.theme.EverfitScannerAppTheme
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScanView(
        viewModel: MainViewModel = hiltViewModel()
    ) {
        val searchBarCodeTextFieldValue = rememberSaveable { mutableStateOf("") }
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter,
            ) {
                Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .padding(12.dp)
                    .wrapContentHeight()
                    .background(Color.White, CircleShape)) {
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.White),
                        singleLine = true,
                        value = searchBarCodeTextFieldValue.value,
                        onValueChange = { searchText ->
                            searchBarCodeTextFieldValue.value = searchText
                        })
                    Button(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            if (!viewModel.callingAPI) {
                                searchBarcode(searchBarCodeTextFieldValue.value)
                            }
                        }, enabled = searchBarCodeTextFieldValue.value.isNotEmpty()
                    ) {
                        Text(text = "Search", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Image(
                        painterResource(id = R.drawable.ic_scan),
                        contentDescription = "",
                        modifier = Modifier
                            .width(70.dp)
                            .height(50.dp)
                            .clickable {
                                startScan { scannedCode ->
                                    searchBarCodeTextFieldValue.value = scannedCode
                                }
                            }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(12.dp)
                ) {
                    if (viewModel.callingAPI) {
                        Text(text = "Scanning...")
                    }
                    if (viewModel.scannedInfoJson.isNotEmpty()) {
                        Text(text = "Result", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            viewModel.scannedInfoJson.forEach { scanned ->
                                Text(text = "Service - ${scanned.key}", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Text(text = scanned.value)
                                Divider(color = Color.Black, thickness = 2.dp)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun searchBarcode(barcodeStr: String) {
        viewModel.searchDataInfoJson(barcodeStr)
    }

    private fun startScan(onScanCompleted: (String) -> Unit) {
        val optionsBuilder = GmsBarcodeScannerOptions.Builder()
        val gmsBarcodeScanner = GmsBarcodeScanning.getClient(this, optionsBuilder.build())
        gmsBarcodeScanner
            .startScan()
            .addOnSuccessListener { barcode: Barcode ->
                val scannedBarCode = barcode.rawValue?:""
                onScanCompleted.invoke(scannedBarCode)
                viewModel.searchDataInfoJson(scannedBarCode)
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