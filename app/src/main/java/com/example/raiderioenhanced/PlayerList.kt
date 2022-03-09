package com.example.raiderioenhanced

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.raiderioenhanced.adapter.itemListAdapter
import com.example.raiderioenhanced.database.PlayerApplication
import com.example.raiderioenhanced.databinding.FragmentPlayerListBinding
import com.example.raiderioenhanced.network.RioApi
import com.example.raiderioenhanced.network.RioApiService
import com.example.raiderioenhanced.viewmodels.rioViewModel
import kotlinx.coroutines.*

class PlayerList : Fragment() {

    lateinit var binding: FragmentPlayerListBinding
    private val viewModel: rioViewModel by activityViewModels()
    private val dbViewModel: dbViewModel by activityViewModels {
        dbViewModelFactory(
            (activity?.application as PlayerApplication).database.playerDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = itemListAdapter {
            dbViewModel.getPlayer(it.id).observe(viewLifecycleOwner) {
                findNavController().navigate(PlayerListDirections.actionPlayerListToDetails(it.id))
            }
        }
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                dbViewModel.allPlayer.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
            }
        }
    }

    override fun onDestroy() {
        viewModel.listOfData.value?.clear()
        super.onDestroy()
    }
}