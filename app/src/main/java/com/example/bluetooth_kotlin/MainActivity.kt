package com.example.bluetooth_kotlin

import android.bluetooth.BluetoothAdapter
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
import com.example.bt_def.BaseActivity
import com.example.bt_def.BluetoothConstants
import com.example.bt_def.BtConnection
import com.example.bt_def.bluetooth.ReceiveThread

class MainActivity : AppCompatActivity(), ReceiveThread.ListenerData {
    private var preferences: SharedPreferences? = null
    private var bAdapter: BluetoothAdapter? = null
    lateinit var btConnection: BtConnection
    private var gameFlag: Boolean = false
    private val gameListPl1 = mutableListOf<Int>()
    private val gameListPl2 = mutableListOf<Int>()
    private lateinit var ibLocal: ImageButton

    private var backPressed: Long = 0
    override fun onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()){
            super.getOnBackPressedDispatcher().onBackPressed()
        } else {
            Toast.makeText(baseContext, "Press once again to exit!", Toast.LENGTH_SHORT).show()
        }
        backPressed = System.currentTimeMillis()
    }

    private fun initView(){
        ibLocal = findViewById(R.id.ibLocal)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.btTest)
        button.setOnClickListener { testPreferences() }

        val buttonSendA: Button = findViewById(R.id.buttonSendA)
        val buttonSendB: Button = findViewById(R.id.buttonSendB)
        buttonSendA.setOnClickListener { btConnection.sendMessage("A") }
        buttonSendB.setOnClickListener { btConnection.sendMessage("B") }

        initView()
        ibLocal.setOnClickListener { startActivity(Intent(this ,LocalGameActivity::class.java)) }

        initBtAdapter()
        preferences =
            this.getSharedPreferences(BluetoothConstants.PREFERENCES, Context.MODE_PRIVATE)

    }

    private fun testPreferences() {
        var mac = preferences?.getString(BluetoothConstants.MAC, null)
        Log.d("Debugging", "Test Preferences = $mac")
        if (mac != null) {
            btConnection.connect(mac)
        } else {
            Toast.makeText(this, "Устройство не выбрано!", Toast.LENGTH_LONG).show()
        }
        val myToast = Toast.makeText(this, "MAC: $mac", Toast.LENGTH_SHORT)
        myToast.show()
    }

    private fun initBtAdapter() {
        val bManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bAdapter = bManager.adapter
        btConnection = BtConnection(bAdapter, this)
    }

    override fun onReceive(message: String) {
        runOnUiThread {
            val tView: TextView = findViewById(R.id.textView)
            tView.text = message
            when (message) {
                "START!" -> gameFlag = true
                "FINISH!" -> {
                    gameFlag = false
                    tView.text = "Your result is: ${this.gameListPl1.average().toInt()}"
//                    Log.d("Debugging", "Check sum: ${gameList.sumOf { it / 10 }}")
                    Log.d("Debugging", "Check sum player1: ${this.gameListPl1.sum()}")
                    Log.d("Debugging", "Check sum player2: ${this.gameListPl2.sum()}")
                    gameListPl1.clear()
                    gameListPl2.clear()
                }
            }
            if (gameFlag && message != "START!") {
                if(message.first() == '<' && message.last() == '>') {
                    var msg = message.removePrefix("<").removeSuffix(">")
                    gameListPl1.add(msg.substringBefore(',').toInt())
                    gameListPl2.add(msg.substringAfter(',').toInt())
                }
            }
        }
    }
}