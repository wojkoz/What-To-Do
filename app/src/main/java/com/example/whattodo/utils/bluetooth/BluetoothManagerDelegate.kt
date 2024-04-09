package com.example.whattodo.utils.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import javax.inject.Inject

class BluetoothManagerDelegate @Inject constructor(val context: Context) {
    private var bluetoothManager: BluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private var bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    fun isBluetoothAvailable(): Boolean {
        return bluetoothAdapter != null
    }

    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled ?: false
    }
}
