package com.example.bluetooth_kotlin.game_process

open class GameProcess(
    private val numberOfPlayers: Int,
    val isRobot: Boolean = false,
    val robotPower: Int = 1
) {

    companion object {
        const val AVERAGE = 0
        const val SUM = 1
    }

    protected val listOfDataPlayers =
        List<MutableList<Int>>(numberOfPlayers) { MutableList<Int>(0) { 0 } }

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
            for (playerData in listOfDataPlayers){
                playerData.add(160)
            }
            return listOf(160, 160)/////////
        }
        return listOfDataPlayers.map { it.last() }
    }

    open fun winner(mode: Int = AVERAGE): Int {
        var result = 0
        //Добавить реализацию нескольких режимов
        for (i in 1 until listOfDataPlayers.size) {
            if (listOfDataPlayers[result].average() < listOfDataPlayers[i].average())
                result = i
        }

        return result
    }
}