package com.example.platformer

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint

class Coins(spriteName: String, xpos: Float, ypos: Float):DynamicEntity(spriteName, xpos, ypos)
{
    override fun update(dt: Float) {

    }

    override fun onCollision(that: Entity) {
        this.destroy()
    }

    override fun render(canvas: Canvas, transform: Matrix, paint: Paint) {
        super.render(canvas, transform, paint)
    }

    override fun getEntityType() = TYPE_COIN

}