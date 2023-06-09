package com.example.bluetooth_kotlin

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.bluetooth_kotlin.ble_connect.StatMsg
import com.example.bluetooth_kotlin.ble_connect.StatMsg.Companion.CONNECTING
import com.example.bluetooth_kotlin.ble_connect.StatMsg.Companion.COUNTDOWN_1
import com.example.bluetooth_kotlin.ble_connect.StatMsg.Companion.COUNTDOWN_2
import com.example.bluetooth_kotlin.ble_connect.StatMsg.Companion.FINISH
import com.example.bluetooth_kotlin.ble_connect.StatMsg.Companion.IS_CONNECT
import com.example.bluetooth_kotlin.ble_connect.StatMsg.Companion.IS_NOT_CONNECT
import com.example.bluetooth_kotlin.ble_connect.StatMsg.Companion.LOST
import com.example.bluetooth_kotlin.ble_connect.StatMsg.Companion.START
import com.example.bluetooth_kotlin.ble_connect.StatMsg.Companion.STARTING
import com.example.bluetooth_kotlin.ble_connect.StatMsg.Companion.START_GAME
import com.example.bluetooth_kotlin.game_process.BossAnimation
import com.example.bluetooth_kotlin.game_process.LocalAnimation
import com.example.bt_def.BaseActivity
import com.example.bt_def.BluetoothConstants
import com.example.bt_def.BtConnection
import com.example.bt_def.bluetooth.ReceiveThread

class BossGameActivity : AppCompatActivity(), ReceiveThread.ListenerData {
    private var preferences: SharedPreferences? = null
    private var bAdapter: BluetoothAdapter? = null
    private lateinit var btConnection: BtConnection
    private lateinit var statusConnect: StatMsg
    private lateinit var gameProcess: BossAnimation
    private var gameFlag: Boolean = false
    private lateinit var tvStatus: TextView
    private lateinit var ibReconnection: ImageButton
    private lateinit var btStart: Button
    private lateinit var countdown: TextView
    private lateinit var tvResult: TextView

    private fun initView() {
        tvStatus = findViewById(R.id.tvStatus)
        ibReconnection = findViewById(R.id.ibReconnection)
        btStart = findViewById(R.id.btStart)
        countdown = findViewById(R.id.tvCountdown)
        tvResult = findViewById(R.id.tvResult)

        statusConnect = StatMsg(context = this, tvStatus = tvStatus, btStart = btStart)
        gameProcess = BossAnimation(
            this,
            findViewById(R.id.constraintBoss),
            findViewById(R.id.ibBoss),
            listOf(
                findViewById(R.id.tvHit1),
                findViewById(R.id.tvHit2),
                findViewById(R.id.tvHit3),
                findViewById(R.id.tvHit4),
                findViewById(R.id.tvHit5),
                findViewById(R.id.tvHit6),
                findViewById(R.id.tvHit7),
                findViewById(R.id.tvHit8)
            ),
            tvResult, countdown
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boss_game)

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
        val mac = preferences?.getString(BluetoothConstants.MAC, null)
        Log.d("Debugging", "Test Preferences = $mac")
        if (mac != null) {
            btConnection.connect(mac)
        } else {
            tvStatus.text = getString(R.string.status_noDevice)
            tvStatus.setTextColor(Color.RED)
        }
        val myToast = Toast.makeText(this, "MAC: $mac", Toast.LENGTH_SHORT)
        myToast.show()
        ibReconnection.isEnabled = true
    }

    private fun startGame() {
        btConnection.sendMessage("B")
        gameProcess.clearAnimation()
    }

    private fun initBtAdapter() {
        val bManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bAdapter = bManager.adapter
        btConnection = BtConnection(bAdapter, this)
    }

    override fun onReceive(message: String) {
        val context = this
        runOnUiThread {
            with(statusConnect) {
                when (message) {
                    IS_CONNECT -> connected()
                    IS_NOT_CONNECT -> notConnection()
                    LOST -> lostConnection()
                    CONNECTING -> Toast.makeText(
                        context, CONNECTING, Toast.LENGTH_SHORT
                    ).show()
                    STARTING -> {
                        countdown.text = context.getString(R.string.countdown_3)
                        countdown.isVisible = true
                    }
                    COUNTDOWN_2 -> countdown.text = context.getString(R.string.countdown_2)
                    COUNTDOWN_1 -> countdown.text = context.getString(R.string.countdown_1)
                    START -> {
                        gameFlag = true
                        countdown.text = context.getString(R.string.start_game)
                    }
                    START_GAME -> {
                        countdown.text = ""
                        countdown.isVisible = false
                    }
                    FINISH -> {
                        gameFlag = false
                        gameProcess.stopGame()
                    }
                    else -> if (gameFlag) {
                        gameProcess.addNewData(message)
                        gameProcess.update()
                    }
                }
            }
        }
    }
}