package com.example.groupproject

import android.content.Context
import android.graphics.Rect

class Caterpillar {
    private lateinit var headRect : Rect
    private lateinit var leafRect: Rect
    private var speed : Int = 10
    private var direction : String = "up"
    private var gameOver : Boolean = false
    private var screenWidth : Float = 0f
    private var screenHeight : Float = 0f
    private var leafX : Int = 0
    private var leafY : Int = 0
    private var lvl : Int = 6



    constructor(headRect : Rect, leafRect : Rect, width: Int, height: Int) {
        this.headRect = headRect
        this.leafRect = leafRect
        this.screenWidth = width.toFloat()
        this.screenHeight = height.toFloat()
        getNewLeafPos()
    }

    fun setDirection(s : String) {
        direction = s
    }

    fun getLevel () : Int {
        return lvl
    }

    fun increaseLevel () {
        lvl++
    }

    fun getLeafX () : Int {
        return leafX
    }

    fun getLeafY () : Int {
        return leafY
    }

    fun getNewLeafPos () {
        leafX = (20..screenWidth.toInt() - 150).random()
        leafY = (20..screenHeight.toInt() - 150).random()
        leafRect.set(leafX, leafY, leafX + leafRect.width(), leafY + leafRect.height())
    }

    fun getDirection() : String {
        return direction
    }

    fun isGameOver () : Boolean {
        return gameOver
    }

    fun doesInterset() : Boolean {
        val overlap = Rect()
        if (overlap.setIntersect(headRect, leafRect)) {
            return overlap.width() >= 50 && overlap.height() >= 50
        }
        return false
    }

    fun moveCaterpillar() {
        if (gameOver) {
            return
        }
        // if intersects with leaf

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

        if (doesInterset()) {
            getNewLeafPos()
            increaseLevel()
        }
    }
}