package com.example.raiderioenhanced.viewmodels

import android.text.Editable
import androidx.lifecycle.*
import com.example.raiderioenhanced.network.RioApi
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class rioViewModel : ViewModel() {

    private var _name: Editable? = null
    fun setName(name: Editable) {
        _name = name
    }

    private var _realm: Editable? = null
    fun setRealm(realm: Editable) {
        _realm = realm
    }

    private var _region: String = "US"
    fun setRegion(region: String) {
        _region = region
    }

    private val _listOfData = MutableLiveData<MutableList<String>>()
    val listOfData : LiveData<MutableList<String>> = _listOfData

    private val _listOfDataForDetails = MutableLiveData<MutableList<String>>()
    val listOfDataForDetails : LiveData<MutableList<String>> = _listOfDataForDetails
    fun setListofDataForDetails(l : MutableList<String>){
        _listOfDataForDetails.value = l
    }

    private val _connectionBad = MutableLiveData<Boolean>()
    val connectionBad : LiveData<Boolean> = _connectionBad
    fun setConnectionBad(b : Boolean){
        _connectionBad.value = b
    }

    private val _nullError = MutableLiveData<Boolean>()
    val nullError : LiveData<Boolean> = _nullError
    fun setNullError(b : Boolean){
        _nullError.value = b
    }

    fun searchPlayer() {
        val url =
            "api/v1/characters/profile?region=$_region&realm=$_realm&name=$_name&fields=mythic_plus_scores%2Cgear%2Craid_progression%2Ccovenant%2Cguild"
        viewModelScope.launch {
            try {
                val output = RioApi.retrofitService.getData(url)
                val data = JSONObject(output)
                val name = data["name"].toString()
                val mainIo = JSONObject(data["mythic_plus_scores"].toString())["all"].toString()
                val spec = data["active_spec_name"].toString()
                val cls = data["class"].toString()
                val ilvl = JSONObject(data["gear"].toString())["item_level_equipped"].toString()
                val faction = data["faction"].toString()
                val convenent = JSONObject(data["covenant"].toString())["name"].toString()
                val guild = JSONObject(data["guild"].toString())["name"].toString()
                val raidProg = JSONObject(JSONObject(data["raid_progression"].toString())["sepulcher-of-the-first-ones"].toString())["summary"].toString()
                val lastOnline = data["last_crawled_at"].toString().take(10)
                val realm = data["realm"].toString()
                val region = data["region"].toString()
                _listOfData.value = mutableListOf(name, mainIo, spec, cls, ilvl, faction, convenent, guild, raidProg, lastOnline, realm, region)
            } catch (e : HttpException) {
                _nullError.value = true
            } catch (e : IOException) {
                _connectionBad.value = true
            }  catch (e : Exception) {
                _nullError.value = true
            }
        }
    }

    fun searchPlayerForDetails() {
        val url =
            "api/v1/characters/profile?region=$_region&realm=$_realm&name=$_name&fields=mythic_plus_scores%2Cgear%2Craid_progression%2Ccovenant%2Cguild"
        viewModelScope.launch {
            try {
                val output = RioApi.retrofitService.getData(url)
                val data = JSONObject(output)
                val name = data["name"].toString()
                val mainIo = JSONObject(data["mythic_plus_scores"].toString())["all"].toString()
                val spec = data["active_spec_name"].toString()
                val cls = data["class"].toString()
                val ilvl = JSONObject(data["gear"].toString())["item_level_equipped"].toString()
                val faction = data["faction"].toString()
                val convenent = JSONObject(data["covenant"].toString())["name"].toString()
                val guild = JSONObject(data["guild"].toString())["name"].toString()
                val raidProg = JSONObject(JSONObject(data["raid_progression"].toString())["sepulcher-of-the-first-ones"].toString())["summary"].toString()
                val lastOnline = data["last_crawled_at"].toString().take(10)
                val realm = data["realm"].toString()
                val region = data["region"].toString()
                _listOfDataForDetails.value = mutableListOf(name, mainIo, spec, cls, ilvl, faction, convenent, guild, raidProg, lastOnline, realm, region)
            }  catch (e : Exception) {

            }
        }
    }
}
