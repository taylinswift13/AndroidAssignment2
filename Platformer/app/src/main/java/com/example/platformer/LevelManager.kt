package com.example.platformer


class LevelManager(level:LevelData) {
    var entities=ArrayList<Entity>()
    var entitiesToAdd=ArrayList<Entity>()
    var entitiesToRemove=ArrayList<Entity>()
    init {
        loadAssets(level)
    }
    fun update(dt:Float){
        for(e in entities){
            e.update(dt)
        }
        //check collisions
        addAndRemoveEntities()
    }
    private fun loadAssets(level: LevelData){
        val levelHeight=level.getHeight()
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
        addEntity(StaticEntity(spriteName,x.toFloat(),y.toFloat()))
        if(spriteName.equals(PLAYER,ignoreCase = true)){

        }
        else{

        }
    }
    private fun addEntity(e:Entity){
        entitiesToAdd.add(e)
    }
    fun removeEntity(e:Entity){
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
    private fun cleanup(){
        addAndRemoveEntities()
        for(e in entities){
            e.destroy()
        }
        entities.clear()
    }
    private fun destroy(){
        cleanup()
    }



}