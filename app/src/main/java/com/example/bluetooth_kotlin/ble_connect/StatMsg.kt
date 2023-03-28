package com.example.bluetooth_kotlin.ble_connect

import android.content.Context
import android.graphics.Color
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.bluetooth_kotlin.R

data class StatMsg(var status: Boolean = false, val context: Context, val tvStatus: TextView, val btStart: Button){
    val isConnect = "Connected"
    val connecting = "Connecting..."
    val lost = "Connection lost"
    val isNotConnect = "Can not connect to device"
    val start = "START!"
    val finish = "FINISH!"
    val starting = "The game will start in 3..."

    fun connected (){
        tvStatus.text = context.getString(R.string.status_connected)
        btStart.isEnabled = true
        tvStatus.setTextColor(Color.WHITE)
        Toast.makeText(context, isConnect, Toast.LENGTH_SHORT).show()
        status = true
    }

    fun notConnection(){
        tvStatus.text = context.getString(R.string.status_cannotConnect)
        tvStatus.setTextColor(Color.RED)
        btStart.isEnabled = false
        Toast.makeText(context, isNotConnect, Toast.LENGTH_LONG).show()
        status = false
    }

    fun lostConnection(){
        tvStatus.text = context.getString(R.string.status_connectionLost)
        tvStatus.setTextColor(Color.RED)
        btStart.isEnabled = false
        Toast.makeText(context, lost, Toast.LENGTH_LONG).show()
        status = false
    }

}
