package com.example.bluetooth_kotlin.ble_connect

import android.content.Context
import android.widget.Toast

data class StatMsg(var status: Boolean = false, val context: Context){
    val isConnect = "Connected"
    val connecting = "Connecting..."
    val lost = "Connection lost"
    val isNotConnect = "Can not connect to device"
    val start = "START!"
    val finish = "FINISH!"
    val starting = "The game will start in 3..."

    fun connected (){
        Toast.makeText(context, isConnect, Toast.LENGTH_SHORT).show()
        status = true
    }

    fun notConnection(){
        Toast.makeText(context, isNotConnect, Toast.LENGTH_LONG).show()
        status = false
    }

    fun lostConnection(){
        Toast.makeText(context, lost, Toast.LENGTH_LONG).show()
        status = false
    }

}
