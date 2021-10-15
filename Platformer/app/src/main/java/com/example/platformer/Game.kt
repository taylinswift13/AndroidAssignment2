package com.example.platformer


import android.content.Context
import android.graphics.*
import android.os.SystemClock.uptimeMillis
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import kotlin.random.Random

const val METERS_TO_SHOW_X=16F
const val METERS_TO_SHOW_Y=0F

const val NANOS_TO_SECOND=1.0f/1000000000f
val RNG= Random(uptimeMillis())
lateinit var engine:Game


class Game(context: Context, attrs: AttributeSet? = null) : SurfaceView(context, attrs), Runnable, SurfaceHolder.Callback {
    private val jukebox = Jukebox(context.assets)
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
    val camera=Viewport(stageWidth,stageHeight, METERS_TO_SHOW_X, METERS_TO_SHOW_Y)
    val pool = BitmapPool(this)
    private var levelManager=LevelManager(TestLevel(context),context)

    private val paint by lazy {
        val paint = Paint()
        paint.isDither = true
        paint.color = Color.RED
        paint.isAntiAlias = true
        paint
    }
    val transform=Matrix()
    val position=PointF()
    var inputs = InputManager()

    fun getControls() = inputs
    fun worldHeight()=levelManager.levelHeight
    fun worldToScreenX(worldDistance:Float)= camera.worldToScreenX(worldDistance)
    fun worldToScreenY(worldDistance:Float)= camera.worldToScreenY(worldDistance)
    //fun screenToWorldX(pixelDistance:Float)=(pixelDistance/ pixelsPerMeters).toFloat()
    //fun screenToWorldY(pixelDistance:Float)=(pixelDistance/ pixelsPerMeters).toFloat()

    fun getScreenHeight()=context.resources.displayMetrics.heightPixels
    fun getScreenWidth()=context.resources.displayMetrics.widthPixels

    override fun run() {
        var lastFrame= System.nanoTime()
        while (isRunning){
            val deltaTime=(System.nanoTime()-lastFrame)*NANOS_TO_SECOND
            lastFrame= System.nanoTime()
            update(deltaTime)
            buildVisibleSet()
            render(visibleEntities)
            //jukebox.play(SFX.BGM)
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

    private fun update(deltaTime:Float) {
        levelManager.update(deltaTime)
        camera.lookAt(levelManager.player)
        if (levelManager.player.health == 0 && !isGameOver) {
            jukebox.play(SFX.die)
            isGameOver = true
        }
        //jukebox.play(SFX.BGM)
    }

    private fun render(visibleSet:ArrayList<Entity>) {
        val canvas = acquireAndLOckCanvas()?:return
        canvas.drawColor(Color.rgb(25,100,100))

        for(e in visibleSet){
            transform.reset()
            camera.worldToScreen(e,position)
            transform.postTranslate(position.x.toFloat(),position.y.toFloat())
            e.render(canvas,transform, paint)
        }
        renderHud(canvas,paint)

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

    fun setControls(input: InputManager) {
        inputs.onPause()
        inputs.onStop()
        inputs = input
        inputs.onResume()
        inputs.onStart()
    }
    private fun renderHud(canvas: Canvas, paint: Paint) {
        val textSize = 40f
        val margin = 10f
        paint.textAlign=Paint.Align.LEFT
        paint.textSize=textSize

        if(isGameOver){
            levelManager.cleanup()
            val context = context
            if(context is IGameInterface){
                context.onGameOver()
            }
        } else{
            canvas.drawText("Health:${levelManager.player.health}",margin,textSize,paint)
            canvas.drawText("Score:${levelManager.coin.coins}/5",margin,textSize*2,paint)
        }

    }

    fun onGameRestart() {
        isGameOver = false
        levelManager.onGameRestart()
    }
}