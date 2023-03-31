package com.example.bluetooth_kotlin.game_process

interface Animation {
    fun update()
    fun addNewData(msg: String): List<Int>
    fun stopGame()
    fun clearAnimation()
}