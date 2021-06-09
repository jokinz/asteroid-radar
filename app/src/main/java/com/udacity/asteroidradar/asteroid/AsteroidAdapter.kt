package com.udacity.asteroidradar.asteroid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.LayoutItemBinding

class AsteroidAdapter(val clickListener: AsteroidListener) : ListAdapter<DatabaseAsteroid, AsteroidAdapter.ViewHolder>(AsteroidDiffCallback()){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: LayoutItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DatabaseAsteroid, clickListener: AsteroidListener) {
            binding.asteroid = item
            binding.codename.text = item.codename
            binding.date.text = item.closeApproachDate
            if (item.isPotentiallyHazardous) {
                binding.hazardousImage.setImageResource(R.drawable.ic_status_potentially_hazardous)
            } else
                binding.hazardousImage.setImageResource(R.drawable.ic_status_normal)
            binding.clickListener = clickListener
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class AsteroidDiffCallback : DiffUtil.ItemCallback<DatabaseAsteroid>(){
    override fun areItemsTheSame(oldItem: DatabaseAsteroid, newItem: DatabaseAsteroid): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DatabaseAsteroid, newItem: DatabaseAsteroid): Boolean {
        return oldItem == newItem
    }
}

class AsteroidListener(val clickListener: (asteroid : DatabaseAsteroid)-> Unit){
    fun onClick(asteroid: DatabaseAsteroid) = clickListener(asteroid)
}