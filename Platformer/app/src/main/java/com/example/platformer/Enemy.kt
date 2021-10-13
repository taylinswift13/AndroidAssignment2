package com.example.platformer

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint

class Enemy(spriteName: String, xpos: Float, ypos: Float):StaticEntity(spriteName, xpos, ypos) {

    override fun getEntityType() = TYPE_ENEMY

    override fun update(dt: Float) {
        super.update(dt)
    }

    override fun onCollision(that: Entity) {
        super.onCollision(that)
    }

    override fun destroy() {
        super.destroy()
    }

    override fun render(canvas: Canvas, transform: Matrix, paint: Paint) {
        super.render(canvas, transform, paint)
    }
}