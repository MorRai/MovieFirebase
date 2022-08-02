package com.rai.movieposter.ui.detailMovie

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.rai.movieposter.R
import com.rai.movieposter.databinding.FragmetMovieDetailBinding

class MovieDetailFragment : Fragment() {

    private var _binding: FragmetMovieDetailBinding? = null
    private val binding get() = requireNotNull(_binding) {
        "View was destroyed"
    }


    private val navigationArgs: MovieDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmetMovieDetailBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie = navigationArgs.movie
        with(binding){
            nameMovie.text = movie.nameMovie.toString()
            certificateYearCountry.text = movie.certificate.toString() + movie.movieDate.toString() + movie.countryOfOrigin.toString()
            genres.text = movie.genres.toString()
                .replace("[", "")
                .replace("]", "")
            runtime.text = movie.runtime.toString()
            storyline.text = movie.storyline.toString()
            imageMovie.load(movie.image)
            val mediaController = MediaController(requireContext())
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)
            videoView.setVideoPath(movie.trailer)
            editItem.setOnClickListener {
                val action = MovieDetailFragmentDirections.actionMovieDetailFragmentToAddMovieFragment(
                    getString(R.string.edit_fragment_title),
                    movie
                )
                findNavController().navigate(action)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}