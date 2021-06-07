package com.udacity.asteroidradar.asteroid

import AsteroidItemViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R

class AsteroidAdapter: RecyclerView.Adapter<AsteroidItemViewHolder>() {
    var data = listOf<Asteroid>()
    set(value){
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AsteroidItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.codename
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.layout_item, parent, false) as TextView
        return AsteroidItemViewHolder(view)
    }
}