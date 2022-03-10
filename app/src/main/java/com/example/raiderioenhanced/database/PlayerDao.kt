package com.example.raiderioenhanced.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(player : Player)

    @Update
    suspend fun update(player: Player)

    @Query("select * from player")
    fun getAllPlayer() : Flow<List<Player>>

    @Query("select * from player where id = :id")
    fun getOnePlayer(id : Int) : Flow<Player>

    @Query("select name from player")
    fun getPlayerNames() : Flow<List<String>>

    @Query("select * from player where name = :name")
    fun getOnePlayerByName(name : String) : Flow<Player>

    @Query("update player set io = :io where name = :name")
    suspend fun updatePlayerIO(io : String, name : String)
}