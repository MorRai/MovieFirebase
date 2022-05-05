package com.rai.movieposter.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.load
import com.rai.movieposter.data.Movie
import com.rai.movieposter.databinding.ItemMovieDetailBinding


class ListMovieAdapter(
    context: Context,
    private val onItemClicked: (Movie) -> Unit
) :
    ListAdapter<Movie, ListMovieAdapter.ItemViewHolder>(DiffCallback) {

    val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
           binding = ItemMovieDetailBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
    }

    class ItemViewHolder (private var binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            (binding as ItemMovieDetailBinding).apply {
                nameFilm.text = movie.nameMovie
                imageFilm.load(movie.image)
                ratingIMDB.text = movie.ratingImbd.toString()
                ratingKinoPoisk.text = movie.ratingKinopoisk.toString()

            }
        }
    }



    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}


