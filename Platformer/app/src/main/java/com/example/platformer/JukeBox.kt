package com.example.platformer

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import java.io.IOException

object SFX{
    var coin = 0
    var start = 0
    var die =0
    var hurt = 0
}
const val MAX_STREAMS = 3
var isFirstInitialize = true

class Jukebox(private val assetManager: AssetManager) {

    private val soundPool: SoundPool
    init {
        val attr = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setAudioAttributes(attr)
            .setMaxStreams(MAX_STREAMS)
            .build()

        soundPool.setOnLoadCompleteListener { _, _, _ ->
            if(isFirstInitialize){
                play(SFX.start)
                isFirstInitialize = false
            }
        }
        SFX.coin = loadSound("Coin.wav")
        SFX.hurt = loadSound("Hurt.wav")
        SFX.start = loadSound(("start.wav"))
    }

    private fun loadSound(fileName: String): Int{
        try {
            val descriptor: AssetFileDescriptor = assetManager.openFd(fileName)
            return soundPool.load(descriptor, 1)
        }catch(e: IOException){
            Log.d(TAG, "Unable to load $fileName! Check the filename, and make sure it's in the assets-folder.")
        }
        return 0
    }

    fun play(soundID: Int) {
        val leftVolume = 1f
        val rightVolume = 1f
        val priority = 0
        val loop = 0
        val playbackRate = 1.0f
        if (soundID > 0) {
            Log.d(TAG, "soundpool play!")
            soundPool.play(soundID, leftVolume, rightVolume, priority, loop, playbackRate)
        }
    }

    companion object{
        const val TAG = "Jukebox"
    }

}