package com.example.groupproject

import android.content.Context

class Game {
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var headX : Float = 300f
    private var headY : Float = 400f
    private var currScore : Int = 0
    private var headSize : Int = 250

    constructor (context: Context, width : Int, height: Int) {
        this.screenWidth = width
        this.screenHeight = height
        headX = (screenWidth - headSize) / 2f
        headY = (screenHeight - headSize) / 2f

    }

    fun getHeadX(): Float = headX
    fun getHeadY(): Float = headY
    fun getHeadSize(): Int = headSize

    fun move(dx: Float, dy: Float) {
        headX += dx
        headY += dy

        // Optional: prevent going off-screen
//        headX = headX.coerceIn(0f, screenWidth - headSize)
//        headY = headY.coerceIn(0f, screenHeight - headSize)
    }

}