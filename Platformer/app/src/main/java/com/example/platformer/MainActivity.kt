package com.example.platformer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.view.WindowCompat
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

class MainActivity : AppCompatActivity(),IGameInterface {
    private val tag="GameActivity"
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var game:Game
    private val mediaPlayer = MediaPlayer()
    private val bgmCachePath by lazy {
        filesDir.absolutePath + File.separator + "cache.mp3"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        game = findViewById(R.id.game)
        val input = TouchController(findViewById(R.id.touch_controller))
        game.setControls(input)

        if(!File(bgmCachePath).exists()){
            assets.open("BGM.mp3").copy()
        }
        mediaPlayer.setDataSource(bgmCachePath)
        mediaPlayer.setOnPreparedListener {
            it.start()
        }
        mediaPlayer.isLooping = true
        mediaPlayer.prepareAsync()
    }

    override fun onPause() {
        game.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        game.resume()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) { //handle older SDKs, using the deprecated systemUiVisbility API
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        } else {
            // Tell the Window that our app is going to responsible for fitting for any system windows.
            // This is similar to: view.setSystemUiVisibility(LAYOUT_STABLE | LAYOUT_FULLSCREEN)
                window.setDecorFitsSystemWindows(false)
//            WindowCompat.setDecorFitsSystemWindows(window, false)
            //getWindowInsetsController from our root View, the game:
            val controller = game.windowInsetsController
            // Hide the keyboard (IME = "input method editor")
            controller?.hide(WindowInsets.Type.ime())
            // Sticky Immersive Mode is now written:
            controller?.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            // hide the all the system bars:
            controller?.hide(WindowInsets.Type.systemBars())
            val flag = WindowInsets.Type.statusBars()
            WindowInsets.Type.navigationBars()
            WindowInsets.Type.captionBar()
            window?.insetsController?.hide(flag)
        }
    }

    override fun onGameOver() {
        val btn = findViewById<Button>(R.id.btn_game)
        btn.post {
            btn.visibility = View.VISIBLE
        }
        btn.setOnClickListener {
            btn.visibility = View.GONE
            onGameRestart()
        }

    }

    private fun onGameRestart() {
        game.onGameRestart()
    }


    private fun InputStream.copy():String?{
        return runCatching {
            this.use {
                FileOutputStream(bgmCachePath).use { output->
                    output.write(it.readBytes())
                }
            }
            bgmCachePath
        }.getOrNull()

    }

}