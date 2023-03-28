package com.example.bluetooth_kotlin

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.bluetooth_kotlin.ble_connect.StatMsg
import com.example.bt_def.BaseActivity
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
    private lateinit var statusConnect: StatMsg
    private lateinit var btStart: Button
    private lateinit var tvPl1: TextView
    private lateinit var tvPl2: TextView
    private lateinit var tvStatus: TextView
    private lateinit var ibReconnection: ImageButton

    private fun initView() {
        tvPl1 = findViewById(R.id.tvPl1)
        tvPl2 = findViewById(R.id.tvPl2)
        btStart = findViewById(R.id.btStart)
        btStart.isEnabled = false
        tvStatus = findViewById(R.id.tvStatus)
        ibReconnection = findViewById(R.id.ibReconnection)
        statusConnect = StatMsg(context = this, tvStatus = tvStatus, btStart = btStart)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_game)

        initView()
        initBtAdapter()
        preferences =
            this.getSharedPreferences(BluetoothConstants.PREFERENCES, Context.MODE_PRIVATE)

        btStart.setOnClickListener { startGame() }
        tvStatus.setOnClickListener { startActivity(Intent(this, BaseActivity::class.java)) }
        ibReconnection.setOnClickListener { connectToDevice() }

        connectToDevice()
    }

    private fun connectToDevice() {
        ibReconnection.isEnabled = false
        var mac = preferences?.getString(BluetoothConstants.MAC, null)
        Log.d("Debugging", "Test Preferences = $mac")
        if (mac != null) {
            btConnection.connect(mac)
        } else {
            tvStatus.text = getString(R.string.status_noDevice)
        }
        val myToast = Toast.makeText(this, "MAC: $mac", Toast.LENGTH_SHORT)
        myToast.show()
        ibReconnection.isEnabled = true
    }

    private fun startGame() {
        btConnection.sendMessage("A")
    }

    private fun initBtAdapter() {
        val bManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bAdapter = bManager.adapter
        btConnection = BtConnection(bAdapter, this)
    }

    @SuppressLint("SetTextI18n")
    override fun onReceive(message: String) {
        runOnUiThread {
            with(statusConnect) {
                when (message) {
                    isConnect -> connected()
                    isNotConnect -> notConnection()
                    lost -> lostConnection()
                    connecting -> Toast.makeText(
                        this@LocalGameActivity,
                        connecting,
                        Toast.LENGTH_SHORT
                    ).show()
                    start -> {
                        gameFlag = true
                        Toast.makeText(this@LocalGameActivity, start, Toast.LENGTH_SHORT).show()
                    }
                    starting -> Toast.makeText(this@LocalGameActivity, starting, Toast.LENGTH_LONG)
                        .show()
                    finish -> {
                        gameFlag = false
                        tvPl1.text =
                            "${getString(R.string.result)} ${gameListPl1.average().toInt()}"
                        tvPl2.text =
                            "${getString(R.string.result)} ${gameListPl2.average().toInt()}"
                        Log.d("Debugging", "Check sum player1: ${gameListPl1.sum()}")
                        Log.d("Debugging", "Check sum player2: ${gameListPl2.sum()}")
                        gameListPl1.clear()
                        gameListPl2.clear()
                    }
                }
            }
            if (gameFlag && message != statusConnect.start) {
                if (message.first() == '<' && message.last() == '>' && message.length < 10) {
                    var msg = message.removePrefix("<").removeSuffix(">")
                    gameListPl1.add(msg.substringBefore(',').toInt())
                    gameListPl2.add(msg.substringAfter(',').toInt())
                    tvPl1.text = gameListPl1.last().toString()
                    tvPl2.text = gameListPl2.last().toString()
                }
            }
        }
    }
}