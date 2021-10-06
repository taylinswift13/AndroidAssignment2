package com.example.platformer

class TestLevel:LevelData() {
    init {
        tileToBitmap.put(NO_TILE,"no_tile")
        tileToBitmap.put(1,PLAYER)
        tileToBitmap.put(2,"ground_left")
        tileToBitmap.put(3,"ground_square")
        tileToBitmap.put(4,"ground_right")
        tiles= arrayOf(
            intArrayOf(2,3,4,0,1)
        )

    }
}