package com.example.whattodo.utils.bluetooth

import android.Manifest.permission
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import javax.inject.Inject

class BluetoothManagerDelegate @Inject constructor(val context: Context) {
    private var bluetoothManager: BluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private var bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private var pairedDevices: Set<BluetoothDevice> = emptySet()

    fun isBluetoothAvailable(): Boolean {
        return bluetoothAdapter != null
    }

    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled ?: false
    }

    fun getPairedDevices(): List<BtDevice> {
        if (ActivityCompat.checkSelfPermission(
                context, permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return emptyList()
        }
        pairedDevices = bluetoothAdapter?.bondedDevices ?: emptySet()

        return pairedDevices.map { BtDevice(it.name, it.address) }
    }
}
