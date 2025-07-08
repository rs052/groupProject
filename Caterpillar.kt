package com.example.groupproject

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Rect
import android.util.Log

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
    private var lvl : Int = 0
    lateinit var bx : ArrayList<Float>
    lateinit var by : ArrayList<Float>
    private var rad : Float = 0f
    lateinit var pref : SharedPreferences
    private var bestLevel : Int = 0



    constructor(context: Context, headRect : Rect, leafRect : Rect, width: Int, height: Int, rad : Float) {
        pref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        this.headRect = headRect
        this.leafRect = leafRect
        this.screenWidth = width.toFloat()
        this.screenHeight = height.toFloat()
        getNewLeafPos()
        this.rad = rad
        bx = ArrayList<Float>()
        by = ArrayList<Float>()
        bestLevel = pref.getInt(PREFERENCE_LEVEL,0)
    }

    fun updateHeadRect(newRect: Rect) {
        headRect = Rect(newRect)
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
        leafX = (20..screenWidth.toInt()).random()
        leafY = (-150..screenHeight.toInt()).random()
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
            Log.w("MainActivity", "game over")
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
            saveBestLevel()
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
                saveBestLevel()
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
        gameOver = false
        getNewLeafPos()
        setDirection("up")
        Log.w("MainActivity", "reseting")
    }

    fun saveBestLevel() {
        Log.w("MainActivity", "lvl = " + lvl)
        Log.w("MainActivity", "bestLevel = " + bestLevel)
        if (lvl > bestLevel) {
            Log.w("MainActivity", "new highscore!!")
            var editor = pref.edit()
            editor.putInt(PREFERENCE_LEVEL, lvl)
            editor.commit()
        }
    }

    companion object {
        private const val PREFERENCE_LEVEL = "bestLevel"
    }
}