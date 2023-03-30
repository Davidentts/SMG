package com.example.bluetooth_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bt_def.bluetooth.ReceiveThread

class BossGameActivity : AppCompatActivity(), ReceiveThread.ListenerData {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boss_game)
    }

    override fun onReceive(message: String) {
        runOnUiThread {

        }
    }
}