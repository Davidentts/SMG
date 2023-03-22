package com.example.bt_def

import android.bluetooth.BluetoothAdapter
import com.example.bt_def.bluetooth.ConnectThread
import com.example.bt_def.bluetooth.ReceiveThread

class BtConnection(private var adapter: BluetoothAdapter?, private val listener: ReceiveThread.ListenerData) {
    lateinit var cThread: ConnectThread

    fun connect (mac: String){
        if(adapter?.isEnabled == true && mac.isNotEmpty()){
            val device = adapter?.getRemoteDevice(mac)
            device?.let {
                cThread = ConnectThread(it, listener)
                cThread.start()
            }
        }
    }

    fun sendMessage(message: String){
        cThread.rThread.sendMessage(message.toByteArray())
    }
}