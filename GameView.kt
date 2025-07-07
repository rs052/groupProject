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

    // size of the head of the caterpillar
    private var headSize : Int = 250

    // lvl = size of caterpillar
    private var lvl : Int = 0

    // caterpillar size variables
    private var rad : Float = 0f
    private var cx : Float = 0f
    private var cy : Float = 0f

    private var c1 : String = ""
    private var c2 : String = ""


    constructor(context: Context) : super(context) {
        paint = Paint()
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        head = BitmapFactory.decodeResource(resources, R.drawable.caterpillarhead)
        headRect = Rect(0,0,headSize,headSize)
    }

    // used this to set the size of the caterpillar bitmap
    // might need to change depending on how caterpillar moves
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // get the radius and x position for body so it can be similar size
        // might need to change depending on how caterpillar moves
        rad = headSize / 3.5f
        cx = w / 2f

        var left = (w - headSize) / 2
        var top = (h - headSize) / 2
        headRect.set(left, top, left + headSize, top + headSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // draw head
        canvas.drawBitmap(head, null, headRect, paint)

        // draw body
        var space = headSize / 10f
        // space from head to start of body
        cy = headRect.bottom - 3f * space

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

        // build body
        // at lvl = 0 its the head and a single circle for the body
        for (i in 0..lvl) {
            if ( i % 2 == 0) {
                paint.color = Color.parseColor(c1)
            } else {
                paint.color = Color.parseColor(c2)
            }
            canvas.drawCircle(cx,cy,rad,paint)
            cy += space
        }
    }

    companion object {
        var LIGHT_GREEN : String = "#FF00ff00"
        var DARK_GREEN : String = "#FF006900"
    }
}
