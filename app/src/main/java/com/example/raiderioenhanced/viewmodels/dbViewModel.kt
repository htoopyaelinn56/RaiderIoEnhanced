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

    fun update(name : String, realm : String, region : String, spec : String, cls : String, io : String){
        val player = Player(name = name, realm = realm, region = region, spec = spec, cls = cls, io = io)
        viewModelScope.launch {
            playerDao.update(player)
        }
    }

    fun updateIO(name : String, io : String, spec : String){
        viewModelScope.launch {
            playerDao.updatePlayerIO(io, name, spec)
        }
    }

    fun getPlayer(id : Int) : LiveData<Player> = playerDao.getOnePlayer(id).asLiveData()
    fun getPlayerByName(name : String) : LiveData<Player> = playerDao.getOnePlayerByName(name).asLiveData()

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