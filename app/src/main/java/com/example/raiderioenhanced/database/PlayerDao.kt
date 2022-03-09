package com.example.raiderioenhanced.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert
    suspend fun insert(player : Player)

    @Update
    suspend fun update(player: Player)

    @Query("select * from player")
    fun getAllPlayer() : Flow<List<Player>>

    @Query("select * from player where id = :id")
    fun getOnePlayer(id : Int) : Flow<Player>

    @Query("select name from player")
    fun getPlayerNames() : Flow<List<String>>
}