package com.example.platformer

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.util.Log

const val TYPE_UNDEFINED = -1
const val TYPE_COIN = 0
const val TYPE_TILE = 1
const val TYPE_ENEMY = 2
const val TYPE_PLAYER = 3

abstract class Entity {
    private val TAG = "Entity"
    var x = 0f
    var y = 0f
    var width = 0f
    var height = 0f
    var type = TYPE_UNDEFINED

    init {
        Log.d(TAG, "Entity created")
    }

    open fun update(dt: Float) {}
    open fun render(canvas: Canvas, transform: Matrix, paint: Paint) {}
    open fun onCollision(that: Entity) {} //notify the Entity about collisions
    open fun destroy() {}
    open fun getEntityType() = type

    fun left() = x
    fun right() = x + width
    fun top() = y
    fun bottom() = y + height
    fun centerX() = x + (width * 0.5f)
    fun centerY() = y + (height * 0.5f)

    fun setLeft(leftEdgePosition: Float) {
        x = leftEdgePosition
    }

    fun setRight(rightEdgePosition: Float) {
        x = rightEdgePosition - width
    }

    fun setTop(topEdgePosition: Float) {
        y = topEdgePosition
    }

    fun setBottom(bottomEdgePosition: Float) {
        y = bottomEdgePosition - height
    }

    fun setCenter(x: Float, y: Float) {
        this.x = x - width * 0.5f
        this.y = y - height * 0.5f
    }

}

//a basic axis-aligned bounding box intersection test.
//https://gamedev.stackexchange.com/questions/586/what-is-the-fastest-way-to-work-out-2d-bounding-box-intersection
fun isColliding(a: Entity, b: Entity): Boolean {
    return !(a.right() < b.left() || b.right() < a.left() || a.bottom() < b.top() || b.bottom() < a.top())
}

//a more refined AABB intersection test
//returns true on intersection, and sets the least intersecting axis in overlap
val overlap = PointF(0f, 0f); //re-usable PointF for collision detection. Assumes single threading.

@SuppressWarnings("UnusedReturnValue")
fun getOverlap(a: Entity, b: Entity, overlap: PointF): Boolean {
    overlap.x = 0.0f;
    overlap.y = 0.0f;
    val centerDeltaX = a.centerX() - b.centerX();
    val halfWidths = (a.width + b.width) * 0.5f;
    var dx = Math.abs(centerDeltaX); //cache the abs, we need it twice

    if (dx > halfWidths) return false; //no overlap on x == no collision

    val centerDeltaY = a.centerY() - b.centerY();
    val halfHeights = (a.height + b.height) * 0.5f;
    var dy = Math.abs(centerDeltaY);

    if (dy > halfHeights) return false; //no overlap on y == no collision

    dx = halfWidths - dx; //overlap on x
    dy = halfHeights - dy; //overlap on y
    if (dy < dx) {
        overlap.y = if (centerDeltaY < 0f) -dy else dy;
    } else if (dy > dx) {
        overlap.x = if (centerDeltaX < 0) -dx else dx;
    } else {
        overlap.x = if (centerDeltaX < 0) -dx else dx;
        overlap.y = if (centerDeltaY < 0) -dy else dy;
    }
    return true;
}