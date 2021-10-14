package com.example.platformer

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import androidx.core.math.MathUtils
import androidx.core.math.MathUtils.clamp
import kotlin.math.abs

val COIN_MOVE_SPEED=5F
class Coins(spriteName: String, xpos: Float, ypos: Float):DynamicEntity(spriteName, xpos, ypos)
{
    /**
     * 1 mean positive
     * -1 mean negative
     */
    var direction = 1
    var offset = 0.3f
    var StartPositionY=0f
    val speed = 0.015f
    var coins = 0
    init{
        StartPositionY=y
    }

    override fun update(dt: Float) {

//        y += clamp(5f * dt, 0f, 5f)*direction

        if(direction == 1){
            y-=speed
            if(y < StartPositionY - offset){
                direction = -1
            }
        }else{
            y+=speed
            if(y > StartPositionY + offset){
                direction = 1
            }
        }

    }

    override fun onCollision(that: Entity) {
        this.destroy()
    }

    override fun render(canvas: Canvas, transform: Matrix, paint: Paint) {
        super.render(canvas, transform, paint)
    }
    fun gainCoin(){
        coins++
    }
    override fun getEntityType() = TYPE_COIN

}