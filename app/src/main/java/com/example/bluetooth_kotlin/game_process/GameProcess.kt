package com.example.bluetooth_kotlin.game_process

import java.lang.Exception

open class GameProcess(
    private val numberOfPlayers: Int,
    private val isRobot: Boolean = false,
    private val robotPower: Int = 1
) {

    companion object {
        const val AVERAGE = 0
        const val SUM = 1
    }

    protected val listOfDataPlayers =
        List<MutableList<Int>>(numberOfPlayers) { MutableList<Int>(0) { 0 } }

    protected var healthOfBoss: Int = when (robotPower) {
        1 -> 18_000
        2 -> 19_000
        3 -> 20_000
        4 -> 21_500
        5 -> 23_500
        else -> 50_000
    }

    protected open fun addData(data: String): List<Int> {
        if (data.first() == '<' && data.last() == '>' &&
            data.length == (numberOfPlayers * 3 + 2 + numberOfPlayers - 1)
        ) {
            var filterData = data.removePrefix("<").removeSuffix(">")
            for ((index, playerData) in listOfDataPlayers.withIndex()) {
                if (index < numberOfPlayers - 1) {
                    val temp = filterData.substringBefore(',')
                    playerData.add(temp.toInt())
                    filterData = filterData.removePrefix("$temp,")
                } else playerData.add(filterData.toInt())
            }
        } else {
            for (playerData in listOfDataPlayers) {
                playerData.add(160)
            }
            return listOf(160, 160)/////////
        }
        if (isRobot){
            for (lastPlayerData in listOfDataPlayers){
                healthOfBoss -= lastPlayerData.last()
            }
        }
        return listOfDataPlayers.map { it.last() }
    }

    open fun winner(mode: Int = AVERAGE): Int {
        var result = 0
        if (isRobot) {
            result = if (healthOfBoss <= 500)
                1 //Player is winner
            else
                2 //Boss is winner
        } else {
            when (mode) {
                AVERAGE -> {
                    for (i in 1 until listOfDataPlayers.size) {
                        if (listOfDataPlayers[result].average() < listOfDataPlayers[i].average())
                            result = i
                    }
                }
                SUM -> {
                    for (i in 1 until listOfDataPlayers.size) {
                        if (listOfDataPlayers[result].sum() < listOfDataPlayers[i].sum())
                            result = i
                    }
                }
                else -> throw Exception("The game mode is specified incorrectly!")
            }
        }
        return result
    }

    open fun healBoss(){
        healthOfBoss = when (robotPower) {
            1 -> 46_000
            2 -> 48_000
            3 -> 50_000
            4 -> 54_000
            5 -> 57_000
            else -> 50_000
        }
    }
}