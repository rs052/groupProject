package com.example.groupproject

import android.content.Context
import android.graphics.Rect

class Caterpillar {
    private lateinit var headRect : Rect
    private var speed : Int = 10
    private var direction : String = "up"
    private var gameOver : Boolean = false
    private var screenWidth : Float = 0f
    private var screenHeight : Float = 0f


    constructor(headRect : Rect, width: Int, height: Int) {
        this.headRect = headRect
        this.screenWidth = width.toFloat()
        this.screenHeight = height.toFloat()
    }

    fun setDirection(s : String) {
        direction = s
    }

    fun getDirection() : String {
        return direction
    }

    fun isGameOver () : Boolean {
        return gameOver
    }

    fun moveCaterpillar() {
        if (gameOver) {
            return
        }
        if (direction == "up") {
            headRect.offset(0, -speed)
        }
        if (direction == "down") {
            headRect.offset(0, speed)
        }
        if (direction == "left") {
            headRect.offset(-speed, 0)
        }
        if (direction == "right") {
            headRect.offset(speed,0)
        }

        if (headRect.left < 0 || headRect.right > screenWidth || headRect.bottom > screenHeight || headRect.top < 0) {
            headRect.offset(0,0)
            gameOver = true
        }
    }
}
