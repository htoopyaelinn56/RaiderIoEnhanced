package com.example.raiderioenhanced.database

import androidx.room.*

@Entity(tableName = "player")
data class Player(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "realm") val realm : String,
    @ColumnInfo(name = "region") val region : String,
    @ColumnInfo(name = "spec") val spec : String,
    @ColumnInfo(name = "cls") val cls : String,
    @ColumnInfo(name = "io") val io : String,
    )