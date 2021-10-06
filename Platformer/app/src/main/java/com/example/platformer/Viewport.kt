package com.example.platformer

import android.graphics.Point
import android.graphics.PointF
import android.graphics.RectF

private const val OVERDRAW_BUFFER = 1f //draw this many meters beyond the viewport edges, to avoid visual gaps at the edge of the screen.
class Viewport(
    val screenWidth: Int,
    val screenHeight: Int,
    metersToShowX: Float,
    metersToShowY: Float) {
    private val lookAt = PointF(0f, 0f)
    private var pixelsPerMeterX = 0
    private var pixelsPerMeterY = 0
    private val screenCenterX = screenWidth / 2
    private val screenCenterY = screenHeight / 2
    private var horizontalView  = 0f
    private var verticalView = 0f
    private var halfDistX = 0f //cached value (0.5*Field of View)
    private var halfDistY = 0f

    init {
        setMetersToShow(metersToShowX, metersToShowY)
    }

    //setMetersToShow calculates the number of physical pixels per meters
    //so that we can translate our game world (meters) to the screen (pixels)
    //provide the dimension(s) you want to lock. The viewport will automatically
    // size the other axis to fill the screen perfectly.
    private fun setMetersToShow(metersToShowX: Float, metersToShowY: Float) {
        require(!(metersToShowX <= 0f && metersToShowY <= 0f)) { "One of the dimensions must be provided!" }
        //formula: new height = (original height / original width) x new width
        horizontalView = metersToShowX
        verticalView = metersToShowY
        if (metersToShowX == 0f || metersToShowY == 0f) {
            if (metersToShowY > 0f) { //if Y is configured, calculate X
                horizontalView = screenWidth.toFloat() / screenHeight * metersToShowY
            } else { //if X is configured, calculate Y
                verticalView = screenHeight.toFloat() / screenWidth * metersToShowX
            }
        }
        halfDistX = (horizontalView + OVERDRAW_BUFFER) * 0.5f
        halfDistY = (verticalView + OVERDRAW_BUFFER) * 0.5f
        pixelsPerMeterX = (screenWidth / horizontalView).toInt()
        pixelsPerMeterY = (screenHeight / verticalView).toInt()
    }

    fun lookAt(x: Float, y: Float) {
        lookAt.x = x
        lookAt.y = y
    }

    fun lookAt(e: Entity) {
        lookAt(e.centerX(), e.centerY())
    }

    fun lookAt(pos: PointF) {
        lookAt(pos.x, pos.y)
    }

    fun worldToScreenX(worldDistance: Float) = (worldDistance * pixelsPerMeterX).toInt()
    fun worldToScreenY(worldDistance: Float) = (worldDistance * pixelsPerMeterY).toInt()

    fun worldToScreen(worldPosX: Float, worldPosY: Float, screenPos: Point) {
        screenPos.x = (screenCenterX - (lookAt.x - worldPosX) * pixelsPerMeterX).toInt()
        screenPos.y = (screenCenterY - (lookAt.y - worldPosY) * pixelsPerMeterY).toInt()
    }

    fun worldToScreen(worldPos: PointF, screenPos: Point) {
        worldToScreen(worldPos.x, worldPos.y, screenPos)
    }

    fun worldToScreen(e: Entity, screenPos: Point) {
        worldToScreen(e.x, e.y, screenPos)
    }

    fun inView(e: Entity): Boolean {
        val maxX = lookAt.x + halfDistX
        val minX = lookAt.x - halfDistX - e.width
        val maxY = lookAt.y + halfDistY
        val minY = lookAt.y - halfDistY - e.height
        return (e.x > minX && e.x < maxX
                && e.y > minY && e.y < maxY)
    }

    fun inView(bounds: RectF): Boolean {
        val right = lookAt.x + halfDistX
        val left = lookAt.x - halfDistX
        val bottom = lookAt.y + halfDistY
        val top = lookAt.y - halfDistY
        return (bounds.left < right && bounds.right > left
                && bounds.top < bottom && bounds.bottom > top)
    }

    override fun toString(): String {
        return "Viewport [${screenWidth}px, ${screenHeight}px / ${horizontalView}m, ${verticalView}m]"
    }
}
