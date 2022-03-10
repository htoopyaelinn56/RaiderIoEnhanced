package com.example.raiderioenhanced

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.raiderioenhanced.database.Player
import com.example.raiderioenhanced.database.PlayerApplication
import com.example.raiderioenhanced.databinding.FragmentSearchBinding
import com.example.raiderioenhanced.viewmodels.rioViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Search : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: rioViewModel by activityViewModels()
    private lateinit var playerList: List<String>
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
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            dbViewModel.allPlayerName.observe(viewLifecycleOwner) {
                playerList = it
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.listOfData.observe(viewLifecycleOwner,
            Observer { char ->
                dialogOption(char)
            }) //(name, mainIo, spec, cls, ilvl, faction, convenent, guild, raidProg, lastOnline, realm, region)

        viewModel.connectionBad.observe(viewLifecycleOwner,
            Observer {
                if (it) {
                    Toast.makeText(
                        requireContext(),
                        "Require internet connection!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.textView.visibility = View.INVISIBLE
                }
            })

        viewModel.nullError.observe(viewLifecycleOwner,
            Observer {
                if (it) {
                    binding.textView.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.INVISIBLE
                }
            })

        binding.realmText.setText("Frostmourne")
        binding.apply {
            viewModel.setName(nameText.text!!)
            viewModel.setRealm(realmText.text!!)
            regionRadio.setOnCheckedChangeListener { radioGroup, i ->
                when (i) {
                    R.id.us -> viewModel.setRegion("US")
                    R.id.eu -> viewModel.setRegion("EU")
                    R.id.kr -> viewModel.setRegion("KR")
                    else -> viewModel.setRegion("TW")
                }
            }
        }

        binding.button.setOnClickListener {
            viewModel.searchPlayer()
            checkValidInput()
            viewModel.listOfData.value?.clear()
        }

        binding.seeList.setOnClickListener {
            viewModel.listOfData.value?.clear()
            viewModel.setNullError(false)
            viewModel.setConnectionBad(false)
            findNavController().navigate(SearchDirections.actionSearchSelf())
            findNavController().navigate(SearchDirections.actionSearchToPlayerList())
        }
    }

    fun checkValidInput() {
        binding.apply {
            if (nameText.text.toString() == "") {
                nameTextField.isErrorEnabled = true
                nameTextField.error = getString(R.string.name_error)
            } else {
                nameTextField.isErrorEnabled = false
            }
            if (realmText.text.toString() == "") {
                realmTextField.isErrorEnabled = true
                realmTextField.error = getString(R.string.realm_error)
            } else {
                realmTextField.isErrorEnabled = false
            }
            if (nameText.text.toString() != "" && realmText.text.toString() != "") {
                progressBar.visibility = View.VISIBLE
            }
        }
    }

    fun dialogOption(char: MutableList<String>) {
        if (char.size > 1) {
            binding.textView.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.INVISIBLE
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("${char[0]} : ${char[2]} ${char[3]}")
                .setMessage(
                    "IO : ${char[1]}\nilvl : ${char[4]}\nFaction : ${char[5]}\n" +
                            "Convenent : ${char[6]}\nGuild : ${char[7]}\nRaid Progression : ${char[8]} SOFO\n" +
                            "Last Online : ${char[9]}"
                )
                .setCancelable(false)
                .setPositiveButton("Save my info") { _, _ ->
                    if (playerList.contains(char[0])) {
                        dbViewModel.getPlayerByName(char[0]).observe(viewLifecycleOwner) {
                            if (it.io == char[1]) {
                                Toast.makeText(requireContext(), "Character already exists!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(), "Updated character!", Toast.LENGTH_SHORT).show()
                                dbViewModel.updateIO(char[0], char[1])
                            }
                        }
                    } else {
                        dbViewModel.addPlayer(char[0], char[10], char[11], char[2], char[3], char[1])
                        Toast.makeText(requireContext(), "Successfully Added!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                .setNegativeButton("Back") { _, _ ->

                }
                .show()
        }
    }
}

