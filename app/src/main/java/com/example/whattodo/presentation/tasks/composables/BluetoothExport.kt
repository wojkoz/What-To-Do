package com.example.whattodo.presentation.tasks.composables

import android.Manifest
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.whattodo.R
import com.example.whattodo.utils.bluetooth.BluetoothManagerDelegate
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import timber.log.Timber

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BluetoothExport(
    onDismiss: () -> Unit,
    bluetoothManagerDelegate: BluetoothManagerDelegate,
) {
    var isBluetoothEnabled by remember {
        mutableStateOf(bluetoothManagerDelegate.isBluetoothEnabled())
    }
    val launchBluetooth = rememberLauncherForActivityResult(contract = StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            Timber.d("Bluetooth is turned on")
        } else {
            Timber.d("Can't turn on bluetooth")
        }

        isBluetoothEnabled = bluetoothManagerDelegate.isBluetoothEnabled()
    }

    fun onPermissionResult(result: Map<String, Boolean>) {
        val arePermissionsGranted = result.values.all { it }
        if (arePermissionsGranted) {
            val blIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            launchBluetooth.launch(blIntent)
        }
    }

    val permissionsStateSdkOver31 = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
        ),
        onPermissionsResult = ::onPermissionResult
    )
    val permissionsStateSdkBelow31 = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.BLUETOOTH,
        ),
        onPermissionsResult = ::onPermissionResult
    )

    LaunchedEffect(key1 = true) {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.S) {
            permissionsStateSdkOver31.launchMultiplePermissionRequest()
        } else {
            permissionsStateSdkBelow31.launchMultiplePermissionRequest()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.bluetooth_export),
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = if (isBluetoothEnabled) "enabled" else "disabled")
            Button(onClick = {
                val blIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                launchBluetooth.launch(blIntent)
            }) {
                Text(text = "Toggle Bluetooth")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewBluetoothExport() {
    BluetoothExport(
        onDismiss = {},
        bluetoothManagerDelegate = BluetoothManagerDelegate(LocalContext.current)
    )
}