package com.example.platformer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint

class StaticEntity(sprite:String,x:Float,y:Float):Entity(){
    lateinit var bitmap: Bitmap
    init {
        this.x=x
        this.y=y
        width=1.0f
        height=1.0f
        val widthInPixels= engine.worldToScreenX(width)
        val heightInPixels= engine.worldToScreenX(height)
        bitmap=BitmapUtils.loadScaledBitmap(
            engine.context,
            sprite,
            widthInPixels,
            heightInPixels
        )
    }

    override fun render(canvas: Canvas, transform: Matrix, paint: Paint) {
        canvas.drawBitmap(bitmap, transform,paint)
    }
}