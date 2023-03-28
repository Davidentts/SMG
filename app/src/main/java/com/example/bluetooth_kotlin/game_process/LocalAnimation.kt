package com.example.bluetooth_kotlin.game_process

import android.widget.Button

class LocalAnimation(
    val listOfSticksPl1: List<Button>,
    val listOfSticksPl2: List<Button>,
    numberOfPlayers: Int = 2
) : GameProcess(numberOfPlayers) {

}