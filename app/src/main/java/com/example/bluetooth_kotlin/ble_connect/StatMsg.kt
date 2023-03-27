package com.example.bluetooth_kotlin.ble_connect

import android.content.Context
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

data class StatMsg(var status: Boolean = false, val context: Context, val tvStatus: TextView, val btStart: Button){
    val isConnect = "Connected"
    val connecting = "Connecting..."
    val lost = "Connection lost"
    val isNotConnect = "Can not connect"
    val start = "START!"
    val finish = "FINISH!"
    val starting = "The game will start in 3..."

    fun connected (){
        tvStatus.text = isConnect
        btStart.isEnabled = true
        Toast.makeText(context, isConnect, Toast.LENGTH_SHORT).show()
        status = true
    }

    fun notConnection(){
        tvStatus.text = isNotConnect
        btStart.isEnabled = false
        Toast.makeText(context, isNotConnect, Toast.LENGTH_LONG).show()
        status = false
    }

    fun lostConnection(){
        tvStatus.text = lost
        btStart.isEnabled = false
        Toast.makeText(context, lost, Toast.LENGTH_LONG).show()
        status = false
    }

}
