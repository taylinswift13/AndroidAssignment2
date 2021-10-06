package com.example.platformer


import android.content.Context
import android.graphics.*
import android.os.SystemClock.uptimeMillis
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.random.Random

const val STAGE_WIDTH=1080
const val STAGE_HEIGHT=720
const val pixelsPerMeters=50
const val METERS_TO_SHOW_X=20F
const val METERS_TO_SHOW_Y=0F
val RNG= Random(uptimeMillis())
lateinit var engine:Game


class Game(context: Context) : SurfaceView(context), Runnable ,SurfaceHolder.Callback{

    private val tag="Game"
    private val stageHeight=getScreenHeight()/2
    private val stageWidth=getScreenWidth()/2
    private val visibleEntities=ArrayList<Entity>()
    init{
        engine=this
        holder.addCallback(this)
        holder.setFixedSize(stageWidth, stageHeight)
    }
    private val gameThread= Thread(this)
    @Volatile
    private var isRunning=false
    private var isGameOver=false
    private var levelManager=LevelManager(TestLevel())
    val paint=Paint()
    val camera=Viewport(stageWidth,stageHeight, METERS_TO_SHOW_X, METERS_TO_SHOW_Y)
    val transform=Matrix()
    val position=Point()

    fun worldToScreenX(worldDistance:Float)=(worldDistance*pixelsPerMeters).toInt()
    fun worldToScreenY(worldDistance:Float)=(worldDistance*pixelsPerMeters).toInt()
    fun screenToWorldX(pixelDistance:Float)=(pixelDistance/pixelsPerMeters).toFloat()
    fun screenToWorldY(pixelDistance:Float)=(pixelDistance/pixelsPerMeters).toFloat()

    fun getScreenHeight()=context.resources.displayMetrics.heightPixels
    fun getScreenWidth()=context.resources.displayMetrics.widthPixels

    override fun run() {
        while (isRunning){
            update()
            buildVisibleSet()
            render(visibleEntities)
        }
    }
    private fun buildVisibleSet(){
        visibleEntities.clear()
        for (e in levelManager.entities){
            if(camera.inView((e))){
                visibleEntities.add(e)
            }
        }
    }

    private fun update() {
        //levelManager.update( )
    }

    private fun render(visibleSet:ArrayList<Entity>) {
        val canvas = acquireAndLOckCanvas()?:return
        canvas.drawColor(Color.rgb(25,100,100))

        for(e in visibleEntities){
            transform.reset()
            camera.worldToScreen(e,position)
            transform.postTranslate(position.x.toFloat(),position.y.toFloat())
            e.render(canvas,transform, paint)
        }
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