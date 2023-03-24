package com.example.bluetooth_kotlin

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.bt_def.BluetoothConstants
import com.example.bt_def.BtConnection
import com.example.bt_def.bluetooth.ReceiveThread

class LocalGameActivity : AppCompatActivity(), ReceiveThread.ListenerData {
    private var preferences: SharedPreferences? = null
    private var bAdapter: BluetoothAdapter? = null
    lateinit var btConnection: BtConnection
    private var gameFlag: Boolean = false
    private var gameListPl1 = mutableListOf<Int>()
    private var gameListPl2 = mutableListOf<Int>()
    private lateinit var btStart: Button
    private lateinit var tvPl1: TextView
    private lateinit var tvPl2: TextView

    private fun initView(){
        tvPl1 = findViewById(R.id.tvPl1)
        tvPl2 = findViewById(R.id.tvPl2)
        btStart = findViewById(R.id.btStart)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_game)

        initView()
        initBtAdapter()
        preferences =
            this.getSharedPreferences(BluetoothConstants.PREFERENCES, Context.MODE_PRIVATE)

        btStart.setOnClickListener { startGame() }

        connectToDevice()
    }

    private fun connectToDevice() {
        var mac = preferences?.getString(BluetoothConstants.MAC, "шш")
        Log.d("Debugging", "Test Preferences = $mac")
        if (mac != null) {
            btConnection.connect(mac)
        } else {
            Toast.makeText(this, "Устройство не выбрано!", Toast.LENGTH_LONG).show()
            btStart.isEnabled = false
        }
        val myToast = Toast.makeText(this, "MAC: $mac", Toast.LENGTH_SHORT)
        myToast.show()
    }

    private fun startGame() {
        btConnection.sendMessage("A")
    }

    private fun initBtAdapter() {
        val bManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bAdapter = bManager.adapter
        btConnection = BtConnection(bAdapter, this)
    }

    override fun onReceive(message: String) {
        runOnUiThread {
            tvPl1.text = message
            when (message) {
                "START!" -> gameFlag = true
                "FINISH!" -> {
                    gameFlag = false
                    tvPl1.text = "Your result is: ${this.gameListPl1.average().toInt()}"
                    tvPl2.text = "Your result is: ${this.gameListPl2.average().toInt()}"
//                    Log.d("Debugging", "Check sum: ${gameList.sumOf { it / 10 }}")
                    Log.d("Debugging", "Check sum player1: ${this.gameListPl1.sum()}")
                    Log.d("Debugging", "Check sum player2: ${this.gameListPl2.sum()}")
                    gameListPl1.clear()
                    gameListPl2.clear()
                }
            }
            if (gameFlag && message != "START!") {
                if (message.first() == '<' && message.last() == '>') {
                    var msg = message.removePrefix("<").removeSuffix(">")
                    gameListPl1.add(msg.substringBefore(',').toInt())
                    gameListPl2.add(msg.substringAfter(',').toInt())
                }
            }
        }
    }
}