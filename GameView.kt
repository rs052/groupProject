package com.example.groupproject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View

class GameView : View {
    private lateinit var paint : Paint
    private lateinit var headRect : Rect
    private lateinit var head : Bitmap
    private lateinit var caterpillar : Caterpillar
    private var c1 : String = ""
    private var c2 : String = ""
    // size of the head of the caterpillar
    private var headSize : Int = 250
    // lvl = size of caterpillar
    private var lvl : Int = 20
    // caterpillar size variables
    private var rad : Float = 0f
    private var cx : Float = 0f
    private var cy : Float = 0f
    // arraylist to track the path of the caterpillar
    private var bx = ArrayList<Float>()
    private var by = ArrayList<Float>()
    
    constructor(context: Context, width: Int, height: Int) : super(context) {
        paint = Paint()
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        head = BitmapFactory.decodeResource(resources, R.drawable.caterpillarhead)
        // changed to height instead of 0 for top
        headRect = Rect(0,height,headSize,headSize)

        caterpillar = Caterpillar(headRect, width, height)
    }

    // used this to set the size of the caterpillar bitmap
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // get the radius for body so it can be similar size
        rad = headSize / 3.5f

        var left = (w - headSize) / 2
        var top = (h - headSize) / 2
        headRect.set(left, top, left + headSize, top + headSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // set color
        // used so the dark green circle is always at the last part
        if (lvl % 2 == 0) {
            c1 = DARK_GREEN
            c2 = LIGHT_GREEN
        }
        else {
            c1 = LIGHT_GREEN
            c2 = DARK_GREEN
        }

        // at lvl = 0 its the head and a single circle for the body
        while (bx.size < lvl + 1) {
            updateBody()
        }

        canvas.save()
        // rotate head to direction of where caterpillar moving
        if (caterpillar.getDirection() == "up") {
            canvas.rotate(0f, headRect.exactCenterX(), headRect.exactCenterY())
        }
        if (caterpillar.getDirection() == "down") {
            canvas.rotate(180f, headRect.exactCenterX(), headRect.exactCenterY())
        }
        if (caterpillar.getDirection() == "left") {
            canvas.rotate(270f, headRect.exactCenterX(), headRect.exactCenterY())
        }
        if (caterpillar.getDirection() == "right") {
            canvas.rotate(90f, headRect.exactCenterX(), headRect.exactCenterY())
        }
        // draw head
        canvas.drawBitmap(head, null, headRect, paint)
        canvas.restore()

        // build body
        for (i in 0..lvl) {
            if ( i % 2 == 0) {
                paint.color = Color.parseColor(c1)
            } else {
                paint.color = Color.parseColor(c2)
            }
            canvas.drawCircle(bx[i],by[i],rad, paint)
        }
    }

    // used to give a trailing affect on caterpillar body
    fun updateBody() {
        // space from head to start of body
        var space = headSize / 10f

        if (caterpillar.getDirection() == "up") {
            cx = headRect.centerX().toFloat()
            cy = headRect.centerY().toFloat() + space
        }
        if (caterpillar.getDirection() == "down") {
            cx = headRect.centerX().toFloat()
            cy = headRect.centerY().toFloat() - space
        }
        if (caterpillar.getDirection() == "left") {
            cx = headRect.centerX().toFloat() + space
            cy = headRect.centerY().toFloat()
        }
        if (caterpillar.getDirection() == "right") {
            cx = headRect.centerX().toFloat() - space
            cy = headRect.centerY().toFloat()
        }
        bx.add(0, cx)
        by.add(0, cy)

        // keep caterpillar length to lvl
        while (bx.size > lvl + 1) {
            bx.removeAt(bx.size - 1)
            by.removeAt(by.size - 1)
        }
    }
    fun getCaterpillar() : Caterpillar {
        return caterpillar
    }

    companion object {
        var LIGHT_GREEN : String = "#FF00ff00"
        var DARK_GREEN : String = "#FF006900"
        const val DELTA_TIME : Int = 100
    }
}
