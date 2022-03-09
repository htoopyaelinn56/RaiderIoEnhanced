package com.example.raiderioenhanced

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.raiderioenhanced.database.Player
import com.example.raiderioenhanced.database.PlayerApplication
import com.example.raiderioenhanced.databinding.FragmentDetailsBinding
import com.example.raiderioenhanced.network.RioApi
import com.example.raiderioenhanced.viewmodels.rioViewModel
import retrofit2.Retrofit

class Details : Fragment() {

    private var ID : Int = -1
    private val key : String = "keyyyyyy!"
    lateinit var binding : FragmentDetailsBinding

    private val viewModel: rioViewModel by activityViewModels()
    private val dbViewModel: dbViewModel by activityViewModels {
        dbViewModelFactory(
            (activity?.application as PlayerApplication).database.playerDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            ID = it.getInt(key)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbViewModel.getPlayer(ID).observe(viewLifecycleOwner){
            viewModel.setName(toEditable(it.name))
            viewModel.setRealm(toEditable(it.realm))
            viewModel.setRegion(it.region)
            viewModel.searchPlayerForDetails()
            viewModel.listOfDataForDetails.observe(viewLifecycleOwner){
                binding.apply {
                    name.text = it[0]
                    io.text = it[1]
                    spec.text = it[2]
                    cls.text = it[3]
                    ilvl.text = it[4]
                    Faction.text = it[5].capitalize()
                    Convenent.text = it[6]
                    Guild.text = it[7]
                    raidProg.text = it[8]
                    lastOnline.text = it[9]
                    realm.text = it[10]
                }
            } //name, mainIo, spec, cls, ilvl, faction, convenent, guild, raidProg, lastOnline, realm, region
        }
    }

    private fun toEditable(str : String) : Editable{
        return Editable.Factory.getInstance().newEditable(str)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.listOfData.value?.clear()
        viewModel.setListofDataForDetails(mutableListOf("...","...","...","...","...","...","...","...","...","...","...","...",))
        viewModel.setNullError(false)
        viewModel.setConnectionBad(false)
    }
}