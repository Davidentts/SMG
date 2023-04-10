package com.example.bluetooth_kotlin.game_process

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.bluetooth_kotlin.R

class LocalAnimation(
    private val context: Context,
    private val listOfSticksPl1: List<Button>,
    private val listOfSticksPl2: List<Button>,
    private val tvPl1: TextView,
    private val tvPl2: TextView,
    numberOfPlayers: Int = 2
) : GameProcess(numberOfPlayers), Animation {

    private var player1 = 0
    private var player2 = 0
    private val isOnStickPl1 = mutableListOf(false, false, false, false, false)
    private val isOnStickPl2 = mutableListOf(false, false, false, false, false)

    override fun update() {
        val tempPl1 = listOfDataPlayers[0].last()
        val tempPl2 = listOfDataPlayers[1].last()

        if (player1 < tempPl1) {
            when (tempPl1) {
                in 30 until 101 -> updateColorSticksPl1Up(1)
                in 101 until 150 -> updateColorSticksPl1Up(2)
                in 150 until 200 -> updateColorSticksPl1Up(3)
                in 200 until 350 -> updateColorSticksPl1Up(4)
                in 350 until 1000 -> updateColorSticksPl1Up(5)
            }
        } else {
            when (tempPl1) {
                in 30 until 101 -> updateColorSticksPl1Down(0)
                in 101 until 150 -> updateColorSticksPl1Down(1)
                in 150 until 200 -> updateColorSticksPl1Down(2)
                in 200 until 350 -> updateColorSticksPl1Down(3)
                in 350 until 1000 -> updateColorSticksPl1Down(4)
            }
        }
        player1 = tempPl1

        if (player2 < tempPl2) {
            when (tempPl2) {
                in 30 until 101 -> updateColorSticksPl2Up(1)
                in 101 until 150 -> updateColorSticksPl2Up(2)
                in 150 until 200 -> updateColorSticksPl2Up(3)
                in 200 until 350 -> updateColorSticksPl2Up(4)
                in 350 until 1000 -> updateColorSticksPl2Up(5)
            }
        } else {
            when (tempPl2) {
                in 30 until 101 -> updateColorSticksPl2Down(0)
                in 101 until 150 -> updateColorSticksPl2Down(1)
                in 150 until 200 -> updateColorSticksPl2Down(2)
                in 200 until 350 -> updateColorSticksPl2Down(3)
                in 350 until 1000 -> updateColorSticksPl2Down(4)
            }
        }
        player2 = tempPl2
    }

    private fun updateColorSticksPl1Up(num: Int) {
        for (i in 0 until num) {
            if (!isOnStickPl1[i]) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    listOfSticksPl1[i].setBackgroundColor(
                        context.resources.getColor(R.color.active_stick, null))
                }
                isOnStickPl1[i] = true
                break
            }
        }
    }

    private fun updateColorSticksPl2Up(num: Int) {
        for (i in 0 until num) {
            if (!isOnStickPl2[i]) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    listOfSticksPl2[i].setBackgroundColor(
                        context.resources.getColor(R.color.active_stick, null)
                    )
                }
                isOnStickPl2[i] = true
                break
            }
        }
    }

    private fun updateColorSticksPl1Down(num: Int) {
        for (i in 4 downTo num) {
            if (isOnStickPl1[i]) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    listOfSticksPl1[i].setBackgroundColor(
                        context.resources.getColor(R.color.inactive_stick,null))
                }
                isOnStickPl1[i] = false
                break
            }
        }
    }

    private fun updateColorSticksPl2Down(num: Int) {
        for (i in 4 downTo num) {
            if (isOnStickPl2[i]) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    listOfSticksPl2[i].setBackgroundColor(
                        context.resources.getColor(R.color.inactive_stick, null))
                }
                isOnStickPl2[i] = false
                break
            }
        }
    }

    override fun addNewData(msg: String): List<Int> {
        return addData(msg)
    }

    @SuppressLint("SetTextI18n")
    override fun stopGame() {
        tvPl1.text =
            "${context.getString(R.string.result)} ${listOfDataPlayers[0].average().toInt()}"
        tvPl2.text =
            "${context.getString(R.string.result)} ${listOfDataPlayers[1].average().toInt()}"
        if (winner(AVERAGE) == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvPl1.setTextColor(context.getColor(R.color.result_winner))
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvPl2.setTextColor(context.getColor(R.color.result_winner))
            }
        }
        Log.d("Debugging", "Check sum player1: ${listOfDataPlayers[0].sum()}")
        Log.d("Debugging", "Check sum player2: ${listOfDataPlayers[1].sum()}")
        for (playerData in listOfDataPlayers) {
            playerData.clear()
        }
    }

    override fun clearAnimation() {
        tvPl1.text = context.getString(R.string.result)
        tvPl2.text = context.getString(R.string.result)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tvPl1.setTextColor(context.getColor(R.color.white))
            tvPl2.setTextColor(context.getColor(R.color.white))
        }
        for (index in listOfSticksPl1.indices) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                listOfSticksPl1[index].setBackgroundColor(
                    context.resources.getColor(R.color.inactive_stick, null))
            }
            isOnStickPl1[index] = false
        }
        for (index in listOfSticksPl2.indices) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                listOfSticksPl2[index].setBackgroundColor(
                    context.resources.getColor(R.color.inactive_stick, null))
            }
            isOnStickPl2[index] = false
        }
    }
}