package com.example.bluetooth_kotlin.game_process

import android.content.Context
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout


class BossAnimation(
    val context: Context,
    val constraint: ConstraintLayout,
    val listOfTvHits: List<TextView>,
    val tvResult: TextView,
    numberOfPlayers: Int = 1,
    isRobot: Boolean = true,
    robotPower: Int = 3
) : GameProcess(numberOfPlayers, isRobot, robotPower), Animation {

    override fun update() {
        TODO("Not yet implemented")
    }

    override fun addNewData(msg: String): List<Int> {
        return addData(msg)
    }

    override fun stopGame() {
        TODO("Not yet implemented")
    }

    override fun clearAnimation() {
        TODO("Not yet implemented")
    }
}