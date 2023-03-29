package com.example.bluetooth_kotlin.ble_connect

import android.content.Context
import android.graphics.Color
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.bluetooth_kotlin.R

data class StatMsg(var status: Boolean = false, val context: Context, val tvStatus: TextView, val btStart: Button){
    companion object {
        const val IS_CONNECT = "Connected"
        const val CONNECTING = "Connecting..."
        const val LOST = "Connection lost"
        const val IS_NOT_CONNECT = "Can not connect to device"
        const val START = "START!"
        const val FINISH = "FINISH!"
        const val STARTING = "The game will start in 3..."
        const val COUNTDOWN_2 = "2..."
        const val COUNTDOWN_1 = "1..."
        const val START_GAME = "start game"
    }

    fun connected (){
        tvStatus.text = context.getString(R.string.status_connected)
        btStart.isEnabled = true
        tvStatus.setTextColor(Color.WHITE)
        Toast.makeText(context, IS_CONNECT, Toast.LENGTH_SHORT).show()
        status = true
    }

    fun notConnection(){
        tvStatus.text = context.getString(R.string.status_cannotConnect)
        tvStatus.setTextColor(Color.RED)
        btStart.isEnabled = false
        Toast.makeText(context, IS_NOT_CONNECT, Toast.LENGTH_LONG).show()
        status = false
    }

    fun lostConnection(){
        tvStatus.text = context.getString(R.string.status_connectionLost)
        tvStatus.setTextColor(Color.RED)
        btStart.isEnabled = false
        Toast.makeText(context, LOST, Toast.LENGTH_LONG).show()
        status = false
    }

}
