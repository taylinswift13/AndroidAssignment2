package com.example.platformer

import android.util.SparseArray

internal const val PLAYER="yellow_left1"
internal const val COIN="coin"
internal const val ENEMY="spearsup_brown"
internal const val NULLSPRITE= "nullsprite"
internal const val NO_TILE=0

abstract class LevelData {
    var tiles:  Array<IntArray> = emptyArray()
    val tileToBitmap=SparseArray<String>()

    fun getRow(y:Int):IntArray{
        return tiles[y]
    }
    fun getTile(x:Int,y: Int):Int{
        return getRow(y)[x]
    }
    fun getSpriteName(tileType:Int):String{
        val fileName=tileToBitmap[tileType]
        return fileName?: NULLSPRITE
    }
    fun getHeight():Int{
        return tiles.size

    }
    fun getWidth():Int{
        return getRow(0).size
    }



}
