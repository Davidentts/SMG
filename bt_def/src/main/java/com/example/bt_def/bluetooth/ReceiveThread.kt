package com.example.bt_def.bluetooth

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.example.bt_def.BluetoothConstants
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ReceiveThread(bSocket: BluetoothSocket, val listener: ListenerData) : Thread(){
    var inStream: InputStream? = null
    var outStream: OutputStream? = null

    init {
        try {
            inStream = bSocket.inputStream
        } catch (i: IOException){

        }
        try {
            outStream = bSocket.outputStream
        } catch (i: IOException){

        }
    }

    override fun run() {
        val buf = ByteArray(100)
        while (true){
            try{
                val size = inStream?.read(buf)
                val message = String(buf, 0, size!!)
                Log.d("Debugging", "Message: $message")
                listener.onReceive(message)
            } catch (i: IOException){
                listener.onReceive(BluetoothConstants.CONNECTION_LOST)
                break
            }
        }
    }
    fun sendMessage(byteArray: ByteArray){
        try{
            outStream?.write(byteArray)
        } catch (i: IOException){

        }
    }
    interface ListenerData{
        fun onReceive(message: String)
    }
}