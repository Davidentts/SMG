package com.example.bt_def.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

class ConnectThread(private val device: BluetoothDevice, val listener: ReceiveThread.ListenerData) : Thread() {
    private val uuid = "00001101-0000-1000-8000-00805F9B34FB"
    private var mSocket: BluetoothSocket? = null
    lateinit var rThread: ReceiveThread

    init {
        try {
            mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        } catch (e: IOException) {

        } catch (se: SecurityException){

        }
    }

    override fun run() {
        try {
            Log.d("Debugging", "Connecting...")
            listener.onReceive( "Connecting...")
            mSocket?.connect()
            Log.d("Debugging", "Connected")
            listener.onReceive( "Connected")
            rThread = ReceiveThread(mSocket!!, listener)
            rThread.start()
        } catch (e: IOException) {
            Log.d("Debugging", "Can not connect to device")
            listener.onReceive( "Can not connect to device")
            closeConnection()
        } catch (se: SecurityException){

        }
    }

    fun closeConnection(){
        try {
            mSocket?.close()
        } catch (e: IOException) {

        }
    }
}