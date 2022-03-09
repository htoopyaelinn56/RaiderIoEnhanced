package com.example.raiderioenhanced.database

import android.app.Application

class PlayerApplication : Application() {
    val database : PlayerDatabase by lazy {
        PlayerDatabase.getDatabase(this)
    }
}