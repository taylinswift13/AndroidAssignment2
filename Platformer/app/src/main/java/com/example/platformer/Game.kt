package com.example.platformer


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.SystemClock.uptimeMillis
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.random.Random

const val STAGE_WIDTH=1080
const val STAGE_HEIGHT=720
val RNG= Random(uptimeMillis())

class Game(context: Context) : SurfaceView(context), Runnable ,SurfaceHolder.Callback{

    private val tag="Game"
    private val gameThread= Thread(this)
    @Volatile
    private var isRunning=false
    private var isGameOver=false

    init{
        holder.addCallback(this)
        holder.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT)

    }
    override fun run() {
        while (isRunning){
            update()
            render()
        }
    }
    private fun update() {

    }

    private fun render() {
        val canvas = acquireAndLOckCanvas()?:return
        canvas.drawColor(Color.rgb(25,25,112))


        holder.unlockCanvasAndPost(canvas)
    }


    private fun acquireAndLOckCanvas():Canvas?{
        if(holder?.surface?.isValid==false){
            return null
        }
        return  holder.lockCanvas()
    }



    fun pause() {
        Log.d(tag,"pause")
        isRunning=false
        gameThread.join()
    }

    fun resume() {
        Log.d(tag,"resume")
        isRunning=true

    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        Log.d(tag,"surfaceCreated")
        gameThread.start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d(tag,"surfaceChanged,width:$width,height:$height")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        Log.d(tag,"surfaceDestroyed")
    }
}