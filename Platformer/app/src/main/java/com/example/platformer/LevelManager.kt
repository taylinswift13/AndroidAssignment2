package com.example.platformer

import android.content.Context


class LevelManager(val level: LevelData, context: Context) {
    private val jukebox = Jukebox(context.assets)
    var levelHeight=0
    lateinit var player: Player
    lateinit var coin:Coins
    lateinit var enemy: Enemy
    val entities=ArrayList<Entity>()
    val entitiesToAdd=ArrayList<Entity>()
    val entitiesToRemove=ArrayList<Entity>()
    init {
        loadAssets(level)
    }
    fun update(dt:Float){
        for(e in entities){
            e.update(dt)
        }
        doCollisionChecks()
        addAndRemoveEntities()
    }

    private fun doCollisionChecks() {
        for(e in entities){
            if(e == player){
                continue
            }
            if(isColliding(player,e)){
                when(e.getEntityType()){
                    TYPE_COIN -> {
                        coin.gainCoin()
                        destroyEntity(e)
                        removeEntity(e)
                        jukebox.play(SFX.coin)
                    }
                    TYPE_ENEMY-> {
                        destroyEntity(e)
                        if(player.isInvincible==false)
                        {player.loseHealth()
                        jukebox.play(SFX.hurt)}
                    }
                    else -> player.onCollision(e)
                }
                e.onCollision(player)
            }
        }
    }

    private fun destroyEntity(e:Entity){
        e.destroy()
        removeEntity(e)
    }

    private fun loadAssets(level: LevelData){
        levelHeight=level.getHeight()
        for (y in 0 until levelHeight){
            val row=level.getRow(y)
            for(x in row.indices){
                val tileID=row[x]
                if(tileID== NO_TILE)continue
                val spriteName=level.getSpriteName(tileID)
                createEntity(spriteName,x,y)
            }
        }
        addAndRemoveEntities()
    }
    private fun createEntity(spriteName: String, x:Int, y:Int){
        if(spriteName.equals(PLAYER,ignoreCase = true)){
            player = Player(spriteName,x.toFloat(),y.toFloat())
            addEntity(player)
        }
        else if(spriteName.equals(COIN,ignoreCase = true)){
            coin = Coins(spriteName,x.toFloat(),y.toFloat())
            addEntity(coin)
        }
        else if(spriteName.equals(ENEMY,ignoreCase = true)){
            enemy = Enemy(spriteName,x.toFloat(),y.toFloat())
            addEntity(enemy)
        }
        else{
            addEntity(StaticEntity(spriteName,x.toFloat(),y.toFloat()))
        }
    }
    private fun addEntity(e:Entity){
        entitiesToAdd.add(e)
    }
    private fun removeEntity(e:Entity){
        entitiesToRemove.add(e)
    }
    private fun addAndRemoveEntities(){
        for(e in entitiesToRemove){
            entities.remove(e)
        }
        for(e in entitiesToAdd){
            entities.add(e)
        }
        entitiesToRemove.clear()
        entitiesToAdd.clear()
    }
    fun cleanup(){
        for(e in entities){
            e.destroy()
        }
        entities.clear()
    }
    private fun destroy(){
        cleanup()
    }

    fun onGameRestart() {
        loadAssets(level)
        player.revive()
    }


}