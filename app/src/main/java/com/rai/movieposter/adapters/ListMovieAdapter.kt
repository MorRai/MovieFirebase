package com.rai.movieposter.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rai.movieposter.data.Movie
import com.rai.movieposter.databinding.ItemMovieDetailBinding
import com.rai.movieposter.databinding.ItemMovieDetailGridBinding


class ListMovieAdapter(
    context: Context,
    private val layoutManager: GridLayoutManager? = null,
    private val onItemClicked: (Movie) -> Unit
) :
    ListAdapter<Movie, RecyclerView.ViewHolder>(DiffCallback) {

    private val layoutInflater = LayoutInflater.from(context)

    enum class ViewType {
        SMALL,
        DETAILED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.DETAILED.ordinal -> LinearViewHolder(
                binding = ItemMovieDetailBinding.inflate(layoutInflater, parent, false)
            )
            else -> GridViewHolder(
                binding = ItemMovieDetailGridBinding.inflate(layoutInflater, parent, false)
            )
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (layoutManager?.spanCount == 1) ViewType.DETAILED.ordinal
        else ViewType.SMALL.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (layoutManager?.spanCount == 1) {
            val itemViewHolder = holder as LinearViewHolder
            itemViewHolder.bind(item)
        }else
        {
            val itemViewHolder = holder as GridViewHolder
            itemViewHolder.bind(item)
        }
        holder.itemView.setOnClickListener {
            onItemClicked(item)
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

class LinearViewHolder (private var binding: ItemMovieDetailBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
        with(binding) {
            nameFilm.text = movie.nameMovie
            imageFilm.load(movie.image)
            ratingIMDB.text = movie.ratingImbd.toString()
            ratingKinoPoisk.text = movie.ratingKinopoisk.toString()
        }
    }
}

class GridViewHolder (private var binding: ItemMovieDetailGridBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
        with(binding) {
            nameFilm.text = movie.nameMovie
            imageFilm.load(movie.image)
            ratingIMDB.text = movie.ratingImbd.toString()
            ratingKinoPoisk.text = movie.ratingKinopoisk.toString()
        }
    }
}



