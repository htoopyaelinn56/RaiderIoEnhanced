package com.example.raiderioenhanced

import androidx.lifecycle.*
import com.example.raiderioenhanced.database.Player
import com.example.raiderioenhanced.database.PlayerDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class dbViewModel(private val playerDao : PlayerDao) : ViewModel() {

    fun addPlayer(name : String, realm : String, region : String, spec : String, cls : String, io : String){
        val player = Player(name = name, realm = realm, region = region, spec = spec, cls = cls, io = io)
        viewModelScope.launch {
            playerDao.insert(player)
        }
    }

    fun getPlayer(id : Int) : LiveData<Player> = playerDao.getOnePlayer(id).asLiveData()
    val allPlayer : LiveData<List<Player>> = playerDao.getAllPlayer().asLiveData()
    val allPlayerName : LiveData<List<String>> = playerDao.getPlayerNames().asLiveData()
}

class dbViewModelFactory(private val playerDao: PlayerDao)
    :ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(dbViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return dbViewModel(playerDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}