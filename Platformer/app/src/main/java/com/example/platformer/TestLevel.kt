package com.example.platformer

import android.content.Context

class TestLevel(context: Context):LevelData(context) {
    init {
        tileToBitmap.put(NO_TILE,"no_tile")
        tileToBitmap.put(1,PLAYER)
        tileToBitmap.put(2,"ground_left")
        tileToBitmap.put(3,"ground_square")
        tileToBitmap.put(4,"ground_right")
        tileToBitmap.put(5,"mud_square")
        tileToBitmap.put(6,"coin")
//      tileToBitmap.put(8,"enemyblockiron")
        tileToBitmap.put(8,"spearsup_brown")
//        tiles= arrayOf(
//            intArrayOf(2,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,4),
//            intArrayOf(5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5),
//            intArrayOf(5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,0,0,0,0,5),
//            intArrayOf(5,0,0,6,0,0,0,0,0,6,0,0,0,3,4,0,0,0,0,0,5),
//            intArrayOf(5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5),
//            intArrayOf(5,0,0,0,0,2,3,4,0,2,3,4,0,0,0,0,0,0,6,0,5),
//            intArrayOf(5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5),
//            intArrayOf(5,0,2,3,4,0,0,0,6,0,0,0,0,0,0,0,0,2,4,2,5),
//            intArrayOf(5,1,0,0,0,0,8,0,0,0,8,0,0,0,8,0,0,0,0,0,5),
//            intArrayOf(5,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,5)
//        )
        val tileList = mutableListOf<IntArray>()
        context.assets.open("level1").reader().readLines().forEach {
            val dstArray = it.split(",")
            val intArray = IntArray(dstArray.size)
            dstArray.indices.forEach { index->
                intArray[index] = dstArray[index].toInt()
            }
            tileList.add(intArray)
        }
        tiles = tileList.toTypedArray()
    }
}