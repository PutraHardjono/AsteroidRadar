package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.ItemListAsteroidBinding

class MainFragmentAdapter(private val asteroidListener: AsteroidListener)
    : ListAdapter<Asteroid, MainFragmentAdapter.ViewHolder>(AsteroidDiffUtil())  {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(asteroidListener, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(private val binding: ItemListAsteroidBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroidListener: AsteroidListener, item: Asteroid) {
            binding.item = item
            binding.root.setOnClickListener {
                asteroidListener.clickListener(item)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                return ViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_list_asteroid,
                        parent,
                        false
                    )
                )
            }
        }

    }
}

class AsteroidDiffUtil : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem == newItem
    }
}

class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit)