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
    lateinit var bx : ArrayList<Float>
    lateinit var by : ArrayList<Float>
    private var rad : Float = 0f



    constructor(headRect : Rect, leafRect : Rect, width: Int, height: Int, rad : Float) {        this.headRect = headRect
        this.leafRect = leafRect
        this.screenWidth = width.toFloat()
        this.screenHeight = height.toFloat()
        getNewLeafPos()
        this.rad = rad
        bx = ArrayList<Float>()
        by = ArrayList<Float>()
    }

    fun setSpeed (newSpeed : Int ) {
        speed = newSpeed
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
        leafX = (0..screenWidth.toInt()).random()
        leafY = (0..screenHeight.toInt()).random()
        leafRect.set(leafX, leafY, leafX + leafRect.width(), leafY + leafRect.height())
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

    fun doesInterset() : Boolean {
        val overlap = Rect()
        if (overlap.setIntersect(headRect, leafRect)) {
            return overlap.width() >= 50 && overlap.height() >= 50
        }
        return false
    }

    fun moveCaterpillar() {
        if (gameOver) {
//            Log.w("MainActivity", "game over")
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

        var dx : Float
        var dy : Float
        for (i in 0..bx.size-1){
            if (direction == "down") {
                dx = headRect.centerX().toFloat() - bx[i]
                dy = headRect.bottom.toFloat() - by[i]
            }
            else if (direction == "left") {
                dx = headRect.left.toFloat() - bx[i]
                dy = headRect.centerY().toFloat() - by[i]
            }
            else if (direction == "right") {
                dx = headRect.right.toFloat() - bx[i]
                dy = headRect.centerY().toFloat() - by[i]
            } else {
                dx = headRect.centerX().toFloat() - bx[i]
                dy = headRect.top.toFloat() - by[i]
            }

            if ((dx * dx + dy * dy) <= rad * rad) {
                gameOver = true
            }
        }

        if (doesInterset()) {
            getNewLeafPos()
            increaseLevel()
        }
    }

    fun setBodyCoords(bx : ArrayList<Float>, by : ArrayList<Float>) {
        this.bx = bx
        this.by = by
    }

    fun reset() {
        lvl = 0
    }
}