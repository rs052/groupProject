package com.example.groupproject

import android.content.Context
import android.graphics.Rect

class Caterpillar {
    private lateinit var headRect : Rect
    private var speed : Int = 10
    private var direction : String = "up"

    constructor(headRect : Rect) {
        this.headRect = headRect
    }

    fun setDirection(s : String) {
        direction = s
    }

    fun getDirection() : String {
        return direction
    }
    fun moveCaterpillar() {
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
    }
}