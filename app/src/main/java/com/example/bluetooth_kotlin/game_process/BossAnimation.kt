package com.example.bluetooth_kotlin.game_process

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.bluetooth_kotlin.R


class BossAnimation(
    private val context: Context,
    private val constraint: ConstraintLayout,
    private val ibBoss: ImageButton,
    private val listOfTvHits: List<TextView>,
    private val tvResult: TextView,
    private val tvCountDown: TextView,
    numberOfPlayers: Int = 1,
    isRobot: Boolean = true,
    robotPower: Int = 3
) : GameProcess(numberOfPlayers, isRobot, robotPower), Animation {

    private var indexOfLastHit: Int = 0
    private var stage: Int = 0
    private val bossStartHealth: Int = healthOfBoss

    override fun update() {
        newHit((0..7).random())
        tvResult.text = (bossStartHealth - listOfDataPlayers[0].sum()).toString()
        when(getHealthAsPercentage()){
            in 74 downTo 50 -> changeStage(2)
            in 49 downTo 25 -> changeStage(3)
            in 24 downTo 1 -> changeStage(4)
        }
    }

    private fun newHit(indexOfNewHit: Int) {
        listOfTvHits[indexOfLastHit].text = ""
        listOfTvHits[indexOfLastHit].isVisible = false
        listOfTvHits[indexOfNewHit].isVisible = true
        listOfTvHits[indexOfNewHit].text = listOfDataPlayers[0].last().toString()
        indexOfLastHit = indexOfNewHit
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun changeStage(num: Int){
        if (stage != num){
            stage = num
//            constraint.background = context.resources.getColor(R.color.defeat)
            when(stage){
                2 -> ibBoss.setImageDrawable(context.getDrawable(R.drawable.ic_boss_second_stage))
                3 -> ibBoss.setImageDrawable(context.getDrawable(R.drawable.ic_boss_third_stage))
                4 -> ibBoss.setImageDrawable(context.getDrawable(R.drawable.ic_boss_fourth_stage))
            }
        }
    }

    private fun getHealthAsPercentage(): Int = healthOfBoss * 100 / bossStartHealth

    override fun addNewData(msg: String): List<Int> {
        return addData(msg)
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun stopGame() {
        if (winner() == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvResult.setTextColor(context.getColor(R.color.result_winner))
                tvCountDown.setTextColor(context.getColor(R.color.victory))
            }
            tvCountDown.text = context.getString(R.string.victory)
            tvCountDown.background = context.getDrawable(R.drawable.rounded_victory)
            tvCountDown.isVisible = true
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvResult.setTextColor(context.getColor(R.color.red))
                tvCountDown.setTextColor(context.getColor(R.color.defeat))
            }
            tvCountDown.text = context.getString(R.string.defeat)
            tvCountDown.background = context.getDrawable(R.drawable.rounded)
            tvCountDown.isVisible = true
        }
        tvResult.text =
            "${context.getString(R.string.result)} ${listOfDataPlayers[0].sum().toString()}"
        Log.d("Debugging", "Check sum player1: ${listOfDataPlayers[0].sum()}")
        listOfTvHits[indexOfLastHit].isVisible = false
        for (playerData in listOfDataPlayers) {
            playerData.clear()
        }
        healBoss()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun clearAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tvResult.setTextColor(context.getColor(R.color.white))
            tvCountDown.setTextColor(context.getColor(R.color.countdown))
        }
        ibBoss.setImageDrawable(context.getDrawable(R.drawable.ic_boss_main))
//        tvResult.text = context.getString(R.string.result)
        tvCountDown.text = ""
        tvCountDown.isVisible = false
        tvCountDown.background = context.getDrawable(R.drawable.rounded_countdown)

        tvResult.text = bossStartHealth.toString()
    }
}