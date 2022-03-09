package com.example.raiderioenhanced.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.raiderioenhanced.database.Player
import com.example.raiderioenhanced.databinding.PlayerItemLayoutBinding

class itemListAdapter(private val onItemClicked : (Player) -> Unit)
    : ListAdapter<Player, itemListAdapter.itemListHolder>(DiffCallBack){
    class itemListHolder(private val binding : PlayerItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(player : Player){
            binding.name.text = player.name
            binding.spec.text = player.spec+" "+player.cls
            binding.io.text = player.io

            binding.apply {
                if(spec.text.contains("Mage")){
                    name.setTextColor(Color.parseColor("#3FC7EB"))
                }
                if(spec.text.contains("Warrior")){
                    name.setTextColor(Color.parseColor("#C69B6D"))
                }
                if(spec.text.contains("Paladin")){
                    name.setTextColor(Color.parseColor("#F48CBA"))
                }
                if(spec.text.contains("Druid")){
                    name.setTextColor(Color.parseColor("#FF7C0A"))
                }
                if(spec.text.contains("Monk")){
                    name.setTextColor(Color.parseColor("#00FF98"))
                }
                if(spec.text.contains("Warlock")){
                    name.setTextColor(Color.parseColor("#8788EE"))
                }
                if(spec.text.contains("Priest")){
                    name.setTextColor(Color.parseColor("#FFFFFF"))
                }
                if(spec.text.contains("Shaman")){
                    name.setTextColor(Color.parseColor("#0070DD"))
                }
                if(spec.text.contains("Hunter")){
                    name.setTextColor(Color.parseColor("#AAD372"))
                }
                if(spec.text.contains("Rogue")){
                    name.setTextColor(Color.parseColor("#FFF468"))
                }
                if(spec.text.contains("Demon Hunter")){
                    name.setTextColor(Color.parseColor("#A330C9"))
                }
                if(spec.text.contains("Death Knight")){
                    name.setTextColor(Color.parseColor("#C41E3A"))
                }
            }

            binding.apply {
                if(io.text.toString().toDouble() <= 500.0){
                    io.setTextColor(Color.WHITE)
                }
                if(io.text.toString().toDouble() > 500.0){
                    io.setTextColor(Color.GREEN)
                }
                if(io.text.toString().toDouble() > 1000.0){
                    io.setTextColor(Color.parseColor("#2962ff"))
                }
                if(io.text.toString().toDouble() > 1500.0){
                    io.setTextColor(Color.parseColor("#aa00ff"))
                }
                if(io.text.toString().toDouble() > 2200.0){
                    io.setTextColor(Color.parseColor("#f57c00"))
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemListHolder {
        val view = itemListHolder(
            PlayerItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
        view.itemView.setOnClickListener {
            val position = view.adapterPosition
            onItemClicked(getItem(position))
        }
        return view
    }

    override fun onBindViewHolder(holder: itemListHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<Player>(){
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem == newItem
        }

    }
}