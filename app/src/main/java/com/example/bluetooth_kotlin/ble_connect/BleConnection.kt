package com.example.bluetooth_kotlin.ble_connect

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.example.bt_def.BaseActivity
import com.example.bt_def.BluetoothConstants
import com.example.bt_def.BtConnection
import com.example.bt_def.bluetooth.ReceiveThread

class BleConnection(var btConnection: BtConnection, private var mac: String?) :
    ReceiveThread.ListenerData {
    var isConnect = false

    fun connectToDevice(): Boolean {
        Log.d("Debugging", "Test Preferences = $mac")
        if (mac != null) {
            btConnection.connect(mac!!)
        } else {
//            Toast.makeText(this, "Устройство не выбрано!", Toast.LENGTH_LONG).show()
        }
//        val myToast = Toast.makeText(this, "MAC: $mac", Toast.LENGTH_SHORT)
//        myToast.show()
        return false
    }

    override fun onReceive(message: String) {
//        runOnUiThread {
            isConnect = when (message) {
                "Connection lost" -> false
                "Can not connect to device" -> false
                "Connected" -> true
                else -> false
            }
//        }
    }
}